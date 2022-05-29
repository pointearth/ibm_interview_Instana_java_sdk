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

/**
 * Service layer
 *
 * @author Simon
 */
public class TraceService {
    private Tools tools;
    private IGraph iGraph;

    public TraceService() {
        tools = new Tools();
        iGraph = new Digraph();
    }

    public List<Edge> loadData(String fileName) throws IllegalArgumentException, NullPointerException {
        String verticesInfo;
        try {
            verticesInfo = tools.readFromInputStream(fileName);
        } catch (IOException ex) {
            throw new IllegalArgumentException("read input data error", ex);
        }
        List<Edge> edgeList = tools.parseInput(verticesInfo);
        if (null == edgeList) {
            throw new IllegalArgumentException("read input data error");
        }
        iGraph.loadData(edgeList);
        return edgeList;
    }

    public Integer getLatency(String pathInfo) throws NotFoundException, IllegalArgumentException, TraceNotFoundException {
        if (null == pathInfo) {
            throw new IllegalArgumentException("pathInfo is null");
        }
        Character[] pathNodes = tools.parsePath(pathInfo);
        int totalLatency = 0;
        for (int i = 1; i < pathNodes.length; i++) {
            Character fromService = pathNodes[i - 1];
            Character toService = pathNodes[i];
            Optional<Integer> latency = iGraph.getDirectInstance(fromService, toService);
            if (Optional.empty().equals(latency)) {
                throw new TraceNotFoundException(String.format("Don't find trace from %c to %c", fromService, toService));
            } else {
                totalLatency += latency.get();
            }
        }
        return totalLatency;
    }


    /**
     * @param startNodeName - starNodeName start service name
     * @param endNodeName   - endNodeName  end service name
     * @param maxHops       - limitation of max hops
     * @return the number of the trace
     * @throws InputFormatException startNode doesn't exist
     * @throws NotFoundException
     * @throws GraphException
     */
    public int getTraceNumInHops(Character startNodeName, Character endNodeName, int maxHops)
            throws IllegalArgumentException, NotFoundException, GraphException {
        if (!tools.isValidNodeName(startNodeName)) {
            throw new IllegalArgumentException("starNodeName is invalid");
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
            throws IllegalArgumentException, NotFoundException, GraphException {
        if (!tools.isValidNodeName(starNodeName)) {
            throw new IllegalArgumentException("starNodeName is invalid");
        }
        return iGraph.getPathNumEqualEdgeNum(starNodeName, endNodeName, exactlyHops);
    }

    public Integer getLenShortestTrace(Character fromService, Character toService)
            throws IllegalArgumentException, NotFoundException, GraphException, TraceNotFoundException {
        if (null == fromService || null == toService) {
            throw new IllegalArgumentException("getLenShortestTrace argument is null"
                    , new NullPointerException("fromService/toService is null"));
        }
        Optional<Map.Entry<Integer, List<Character>>> shortestPathInfo = iGraph.getShortestPath(fromService, toService);
        if (shortestPathInfo.isEmpty() || null == shortestPathInfo.get().getKey()) {
            throw new TraceNotFoundException(String.format("Don't find trace from %c to %c", fromService, toService));
        } else {
            return shortestPathInfo.get().getKey();
        }
    }

    public int getTraceNumInDistance(Character fromService, Character toService, int lessThanDistance) throws GraphException, NotFoundException {
        if (null == fromService || null == toService) {
            throw new IllegalArgumentException("getTraceNumInDistance argument is null"
                    , new NullPointerException("fromService/toService is null"));
        }
        return iGraph.getTraceNumInDistance(fromService, toService, lessThanDistance);
    }
}
