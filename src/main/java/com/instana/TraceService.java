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
//        final Integer[] totalLatency = {0};
        Integer totalLatency = 0;
        for (int i = 1; i < pathNodes.length; i++) {
            Character fromService = pathNodes[i - 1];
            Character toService = pathNodes[i];
            Optional<Integer> latency = iGraph.getInstance(fromService, toService);
            if (Optional.empty().equals(latency)) {
                throw new TraceNotFoundException(String.format("Don't find trace from %c to %c",fromService, toService));
            } else {
                totalLatency += latency.get();
            }
//            latency.ifPresentOrElse(
//                    curLatency -> totalLatency[0] += curLatency, () -> {
//                        try {
//                            throw new TraceNotFoundException(String.format("Don't find trace from %c to %c",fromService, toService));
//                        } catch (TraceNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    });
        }
        return totalLatency;
    }


    /**
     *
     * @param startNodeName - starNodeName start service name
     * @param endNodeName - endNodeName  end service name
     * @param maxHops - limitation of max hops
     * @return the number of the trace
     * @throws InputFormatException startNode doesn't exist
     * @throws NotFoundException
     * @throws GraphException
     */
    public int getTraceNumInHops(Character startNodeName, Character endNodeName, int maxHops)
            throws InputFormatException, NotFoundException, GraphException {
        if (!tools.isValidNodeName(startNodeName) ) {
            throw new InputFormatException("starNodeName is invalid");
        }
        return iGraph.getPathNumInEdgeNum(startNodeName, endNodeName, maxHops);
    }

    /**
     * get the number of traces from service startNodeName to service endNodeName equal the expected Hops
     *
     * @param starNodeName start service name
     * @param endNodeName  end service name
     * @param exactlyHops  expect number of hops
     * @return
     */
    public int getTraceNumEqualHops(Character starNodeName, Character endNodeName, int exactlyHops)
            throws InputFormatException, NotFoundException, GraphException{
        if (!tools.isValidNodeName(starNodeName) ) {
            throw new InputFormatException("starNodeName is invalid");
        }
        return iGraph.getPathNumEqualEdgeNum(starNodeName, endNodeName, exactlyHops);
    }

    public Integer getLenShortestTrace(Character fromService, Character toService)
            throws InputFormatException, NotFoundException,GraphException,TraceNotFoundException {
        if (null == fromService || null == toService) {
            throw new InputFormatException(String.format("dijkstraGetMinDistance argument is null"
                    , new NullPointerException("source/destination is null")));
        }
        Optional<Map.Entry<Integer,List<Character>>> shortestPathInfo =  iGraph.getShortestPath(fromService, toService);
        final Integer[] length = {0};
        shortestPathInfo.ifPresentOrElse(
                 pathInfo -> length[0] = pathInfo.getKey(), () -> {
                    try {
                        throw new TraceNotFoundException(String.format("Don't find trace from %c to %c",fromService, toService));
                    } catch (TraceNotFoundException e) {
                        e.printStackTrace();
                    }
                });
        return length[0];
    }
}
