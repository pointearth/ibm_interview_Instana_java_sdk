package com.instana.graph;

import com.instana.exception.*;
import com.instana.common.Const;
import com.instana.common.Tools;
import java.util.*;

/**
 * @author Simon
 */
public class Digraph implements IGraph {
    private Tools tools;
    private Map<Character, AdjacencyList> nodes;

    /***
     * Construct Directed Graph with edges
     * @param edgesInfo
     */
    public Digraph() {
        tools = new Tools();
        cleanData();
    }
    @Override
    public void cleanData() {
        nodes = new HashMap<>();
    }
    @Override
    public int getNumOfVertices(){
        return nodes.size();
    }
    @Override
    public int getNumOfEdges() {
        final Integer[] num = {0};
        nodes.values().forEach(
                adjacencyList -> {
                    num[0] += adjacencyList.children.size();
                }
        );
        return num[0];
    }
    @Override
    public void addEdge(Edge edge){
        if (!nodes.containsKey(edge.from)) {
            nodes.put(edge.from, new AdjacencyList(edge.from));
        }
        AdjacencyList curNode = nodes.get(edge.from);
        curNode.children.put(edge.to, edge.instance);
    }

    @Override
    public void removeEdge(Edge edge) {
        if (nodes.containsKey(edge.from)) {
            nodes.get(edge.from).children.remove(edge.to);
            if (0 == nodes.get(edge.from).children.size()) {
                nodes.remove(edge.from);
            }
        }
    }

    @Override
    public void loadData(List<Edge> edgeList) throws NullPointerException {
        if (0 != nodes.size()) {
            cleanData();
        }
        edgeList.forEach(edge -> {
            addEdge(edge);
        });
    }

    /***
     *
     * @param pathInfo: nodes in the trace, such as A-B-C
     * @return
     *  -2: pathInfo' format error
     *  -1: no such trace
     *  >=0: average Latencies
     */
    @Override
    public Optional<Integer> getInstance(Character source, Character destination) throws NotFoundException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException("source does not exist");
        }
        AdjacencyList sourceNode = nodes.get(source);
        if (!sourceNode.children.containsKey(destination)) {
            return Optional.empty();
        } else {
            Integer wight = sourceNode.children.get(destination);
            return Optional.of(wight);
        }
    }

    /**
     * get the number of traces from service startNodeName to service endNodeName with maxHops limitation
     *
     * @param starNodeName start service name
     * @param endNodeName  end service name
     * @param maxHops      limitation of max hops
     * @return -2: format error
     * 0<=: trace number
     */
    public int getTraceNumberInMaxHops(Character starNodeName, Character endNodeName, int maxHops) {
        if (null == starNodeName || null == endNodeName) {
            return ErrorCode.INPUT_ERROR.getValue();
        }
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        AdjacencyList startNode = nodes.get(starNodeName);
        String path = starNodeName.toString();
        int traceNumber = bFSGetTraceNumberWithHops(startNode, endNodeName, false, maxHops, path);
        return traceNumber;
    }

    /**
     * get the number of traces from service startNodeName to service endNodeName equal the expected Hops
     *
     * @param starNodeName start service name
     * @param endNodeName  end service name
     * @param exactlyHops  expect number of hops
     * @return
     */
    public int getTraceNumberExactlyHops(Character starNodeName, Character endNodeName, int exactlyHops) {
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        AdjacencyList startNode = nodes.get(starNodeName);
        String path = starNodeName.toString();
        int traceNumber = bFSGetTraceNumberWithHops(startNode, endNodeName, true, exactlyHops, path);
        return traceNumber;
    }

    /**
     * recursive BSF method to
     *
     * @param startNode   start service
     * @param endNodeName end service name
     * @param restHops    rest number of hops
     * @param path        help to show path
     * @return number of trace
     * @bExactlyHops is exactly hops, or maxHops
     */
    private int bFSGetTraceNumberWithHops(AdjacencyList startNode, Character endNodeName, boolean bExactlyHops, int restHops, String path) {
        System.out.println("current path:" + path);
        if (restHops < 1) {
            System.out.println("---reach limitation: " + path);
            return 0;
        }
        int traceNumber = 0;
        if (startNode.children.containsKey(endNodeName)) {
            if ((bExactlyHops && restHops == 1) || !bExactlyHops) {
                traceNumber = 1;
                System.out.println("===success " + path + "-" + endNodeName);
            }
        }
        for (Character nodeName : startNode.children.keySet()) {
            if (nodes.containsKey(nodeName)) {
                AdjacencyList curStartNode = nodes.get(nodeName);
                traceNumber += bFSGetTraceNumberWithHops(curStartNode, endNodeName, bExactlyHops, restHops - 1, path + "-" + nodeName);
            }
        }
        return traceNumber;
    }


    /**
     * Calculate the shortest path between 2 nodes in the graph with dijkstra algorithm
     *
     * @param source      - source node
     * @param destination - destination node
     * @return - the instance of the shortest path, return empty if there is no trace between source and destination
     * @throws NotFoundException - source/destination node does not exist in the graph
     * @throws GraphException    - Graphic calculation error
     */
    @Override
    public Optional<Map.Entry<Integer,List<Character>>> getShortestPath(Character source, Character destination)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source) || !nodes.containsKey(destination)) {
            throw new NotFoundException("can't find the node in the graph");
        }

        Vertex[] vertices = new Vertex[nodes.size()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(null
                    ,(char) (i + Const.A)
                    ,Integer.MAX_VALUE
            );
        }

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(v -> v.instance));
        vertices[source- Const.A].instance = 0;
        queue.add(vertices[source- Const.A]);
        boolean isFirstEdge = true;
        while (!queue.isEmpty()) {
            Vertex minVertex = queue.poll();
            if (!isFirstEdge) {
                minVertex.visited = true;
                // means reach the destination, have to avoid source == destination && first node
                if (minVertex.destination.equals(destination)) {
                    vertices[destination - Const.A].previous = minVertex.previous;
                    break;
                }
                isFirstEdge = false;
            }

            // the trace doesn't exist
            if (!nodes.containsKey(minVertex.destination)) {
                throw new GraphException("can't find destination in priority queue");
            }
            AdjacencyList adjacencyList = nodes.get(minVertex.destination);

            for (Map.Entry<Character, Integer> subEdge : adjacencyList.children.entrySet()) {
                Character subDestination = subEdge.getKey();
                Integer subDistance = subEdge.getValue();

                // update distances array
                Vertex curVertex = vertices[subDestination - Const.A];
                if (curVertex.visited) {
                    continue;
                }
                // update is required in TWO seniors:
                //      1: new instance < current instance
                //      2: current instance == 0 and current vertex is destination vertex
                if ((subDistance + minVertex.instance < curVertex.instance) || (0 == curVertex.instance && destination.equals(curVertex.destination)) ) {
                    curVertex.previous= minVertex.destination;
                    curVertex.instance = subDistance + minVertex.instance;

                    if (!queue.contains(curVertex)) {
                        queue.add(curVertex);
                    }
                }
            }
            // avoid senior source == destination && first node, if not, set the vertex as visited
//            if (!isFirstEdge  && !source.equals(minVertex.destination)) {

//            isFirstEdge = false;
        }
        List<Character> pathList = getPathInfoFromArray(vertices, destination);
        // means find the answer
        if (Integer.MAX_VALUE != vertices[destination - Const.A].instance) {
            Map.Entry<Integer,List<Character>> shortestPath = new AbstractMap.SimpleEntry<>(vertices[destination - Const.A].instance,pathList );
            return Optional.of(shortestPath);
        } else {
            return Optional.empty();
        }
    }
    public List<Character> getPathInfoFromArray(Vertex[] vertexArray, Character destination) {
        if (0 == vertexArray.length) {
            return new LinkedList<>();
        }
        List<Character> pathList = new LinkedList<>();
        pathList.add(destination);
        Character previous = vertexArray[destination - Const.A].previous;
        while (null != previous) {
            pathList.add(0,previous);
////            String previousPath = String.format("%c-", (char) (previousIndex + Const.A));
//            sb.insert(0, previousPath);
            // it is a loop, means reach the start node
            if (destination.equals(previous)) {
                break;
            }
            previous = vertexArray[previous- Const.A].previous;
        }
        return pathList;
    }
//    public List<List<Character>> getTraceNumberInLatency(Character source, Character destination ) {
//
//    }
}
