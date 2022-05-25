package com.instana;

import com.instana.common.Tools;
import com.instana.exception.*;
import com.instana.graph.Digraph;
import com.instana.graph.IGraph;
import com.instana.graph.Edge;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TraceService {
    private Tools tools;
    private IGraph iGraph;

    public TraceService() {
        tools = new Tools();
        iGraph = new Digraph();
    }

    public void loadData(String fileName) throws InputFormatException,NullPointerException {
        String verticesInfo;
        try {
            verticesInfo = tools.readFromInputStream(fileName);
        } catch (IOException ex) {
            throw new InputFormatException("read input data error", ex );
        }
        List<Edge> edgeList = tools.parseInput(verticesInfo);
        if (null == edgeList) {
            throw new InputFormatException("read input data error" );
        }
        iGraph.loadData(edgeList);
    }

    public Integer getAverageLatency(String pathInfo) throws NotFoundException, TraceNotFoundException {
        if (null == pathInfo) {
            return ErrorCode.INPUT_ERROR.getValue();
        }
        Character[] pathNodes = tools.parsePath(pathInfo);
        final Integer[] totalLatency = {0};
        for (int i = 1; i < pathNodes.length; i++) {
            Character fromService = pathNodes[i - 1];
            Character toService = pathNodes[i];
            Optional<Integer> latency = iGraph.getInstance(fromService, toService);
            latency.ifPresentOrElse(
                    curLatency -> totalLatency[0] += curLatency, () -> {
                        throw new TraceNotFoundException(String.format("Don't find trace from %c to %c",fromService, toService));
                    });
        }
        return totalLatency[0];
    }

    public Integer getLenShortestTrace(Character fromService, Character toService)
            throws InputFormatException, NotFoundException,GraphException {
        if (null == fromService || null == toService) {
            throw new InputFormatException(String.format("dijkstraGetMinDistance argument is null"
                    , new NullPointerException("source/destination is null")));
        }
        Optional<Map.Entry<Integer,List<Character>>> shortestPathInfo =  iGraph.getShortestPath(fromService, toService);
        final Integer[] length = {0};
        shortestPathInfo.ifPresentOrElse(
                 pathInfo -> length[0] = pathInfo.getKey(), () -> {
                    throw new TraceNotFoundException(String.format("Don't find trace from %c to %c",fromService, toService));
                });
        return length[0];
    }
}
