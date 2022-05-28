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

    public Digraph() {
        tools = new Tools();
        cleanData();
    }
    @Override
    public void cleanData() {
        nodes = new HashMap<>();
    }
    @Override
    public int getAmountOfSourceNodes(){
        return nodes.size();
    }
    @Override
    public int getAmountOfEdges() {
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
        curNode.children.put(edge.to, edge.distance);
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

    @Override
    public Optional<Integer> getDirectInstance(Character source, Character destination) throws NotFoundException {
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
    @Override
    public int getPathNumInEdgeNum(Character source, Character destination, int maxEdgeNum)
        throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException(String.format("Can't find the %c", source));
        }
        return getNumOfPathsWithLimitedEdges(source, destination,false, maxEdgeNum);
    }
    @Override
    public int getPathNumEqualEdgeNum(Character source, Character destination, int exactlyEdges)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException(String.format("Can't find the %c", source));
        }
        return getNumOfPathsWithLimitedEdges(source, destination,true, exactlyEdges);
    }

    /**
     * recursive BSF method to cal
     * @param source   source node name
     * @param destination destination node name
     * @param numOfEdges    rest number of hops
     * @return number of trace
     * @bExactlyHops is exactly hops, or max hops
     */
    private int getNumOfPathsWithLimitedEdges(Character source, Character destination, boolean isExactly, int numOfEdges)
        throws GraphException
    {
        AdjacencyList startNode = nodes.get(source);
        if ( null == startNode) {
            throw new GraphException(String.format("starNodeName %c doesn't exist ", source));
        }
        if (numOfEdges < 1) {
            return 0;
        }
        int[] traceNumber = {0};
        if (startNode.children.containsKey(destination)) {
            if ((isExactly && numOfEdges == 1) || !isExactly) {
                traceNumber[0] = 1;
            }
        }
        startNode.children.keySet().forEach(
                nodeName -> {
                    int curNumOfEdges = 0;
                    try {
                        curNumOfEdges = getNumOfPathsWithLimitedEdges(nodeName, destination, isExactly, numOfEdges - 1);
                    } catch (GraphException e) {
                        e.printStackTrace();
                    }
                    traceNumber[0] += curNumOfEdges;
                }
        );
        return traceNumber[0];
    }


    /**
     * Get the shortest path between source and destination, Implemented with Dijkstra's Algorithm
     * @param source - source node name
     * @param destination - destination node name
     * @return the shortest path
     * 1. Optional.empty() : there is no path between source node and destination node
     * 2. not empty: Map.Entry<Integer, List<Character>> - description the shortest path, including:
     *      @Integer - the instance of the path
     *      @List<Character> - serious of nodes in the Path. i.e: ['A', 'B' 'C'] means the shortest path is composed of nodes 'A', 'B' 'C'.
     * @throws NotFoundException - source node doesn't exist in the graph
     * @throws GraphException - internal error in the Graph
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

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        vertices[source- Const.A].distance = 0;
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
                if ((subDistance + minVertex.distance < curVertex.distance) || (0 == curVertex.distance && destination.equals(curVertex.destination)) ) {
                    curVertex.previous= minVertex.destination;
                    curVertex.distance = subDistance + minVertex.distance;

                    if (!queue.contains(curVertex)) {
                        queue.add(curVertex);
                    }
                }
            }
        }
        List<Character> pathList = getPathInfoFromArray(vertices, destination);
        // means find the answer
        if (Integer.MAX_VALUE != vertices[destination - Const.A].distance) {
            Map.Entry<Integer,List<Character>> shortestPath = new AbstractMap.SimpleEntry<>(vertices[destination - Const.A].distance,pathList );
            return Optional.of(shortestPath);
        } else {
            return Optional.empty();
        }
    }

    /**
     *
     * @param vertexArray
     * @param destination
     * @return
     */
    private List<Character> getPathInfoFromArray(Vertex[] vertexArray, Character destination) {
        if (0 == vertexArray.length) {
            return new LinkedList<>();
        }
        List<Character> pathList = new LinkedList<>();
        pathList.add(destination);
        Character previous = vertexArray[destination - Const.A].previous;
        while (null != previous) {
            pathList.add(0,previous);
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
