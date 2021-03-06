package com.instana.graph;

import com.instana.exception.*;
import com.instana.common.Const;
import java.util.*;

/**
 * To implement a Directed Graph, calculates:
 * - get distance of 2 nodes
 * - find the shortest path between 2 nodes with limitation of edges/distance
 * - find the number of path between 2 nodes with limitation of edges/distance
 * - other features
 * @author Simon
 */
public class Digraph implements IGraph {
    private Map<Character, AdjacencyList> nodes;

    public Digraph() {
        cleanData();
    }

    @Override
    public void cleanData() {
        nodes = new HashMap<>();
    }

    @Override
    public int getAmountOfSourceNodes() {
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

    /**
     * Remove an edge from the graph
     * Important: when you remove an edge, this.nodes.key will never, ever removed
     * @param edge - the edge to be added
     */
    @Override
    public void addEdge(Edge edge) {
        if (!nodes.containsKey(edge.from)) {
            nodes.put(edge.from, new AdjacencyList(edge.from));
        }
        if (!nodes.containsKey(edge.to)) {
            nodes.put(edge.to, new AdjacencyList(edge.to));
        }
        AdjacencyList curNode = nodes.get(edge.from);
        curNode.children.put(edge.to, edge.distance);
    }

    @Override
    public void removeEdge(Edge edge) {
        if (nodes.containsKey(edge.from)) {
            nodes.get(edge.from).children.remove(edge.to);
        }
    }

    @Override
    public void loadData(List<Edge> edgeList) {
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
        if (!nodes.containsKey(destination)) {
            throw new NotFoundException("destination does not exist");
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
     *
     * @param source      - source node name
     * @param destination - destination node name
     * @param maxEdgeNum  - max number of the edges to be used in a path
     * @return path number
     * @throws NotFoundException - source/destination node doesn't exist
     * @throws GraphException - internal error in graph
     */
    @Override
    public int getPathNumInEdgeNum(Character source, Character destination, int maxEdgeNum)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException(String.format("Can't find the %c", source));
        }
        if (!nodes.containsKey(destination)) {
            throw new NotFoundException("destination does not exist");
        }
        return getNumOfPathsWithLimitedEdges(source, destination, false, maxEdgeNum);
    }

    /**
     * Get number of paths between source node and destination node, each of paths composed in exactly number of edges
     * Implement with DFS
     * @param source       - source node name
     * @param destination  - destination node name
     * @param exactlyEdges - number of the edges to be used in a path
     * @return number of path
     * @throws NotFoundException - source/destination does not exist
     * @throws GraphException - internal error in the Graph
     */
    @Override
    public int getPathNumEqualEdgeNum(Character source, Character destination, int exactlyEdges)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException("source does not exist");
        }
        if (!nodes.containsKey(destination)) {
            throw new NotFoundException("destination does not exist");
        }
        return getNumOfPathsWithLimitedEdges(source, destination, true, exactlyEdges);
    }

    /**
     * recursive DSF method to cal
     *
     * @param source      source node name
     * @param destination destination node name
     * @param numOfEdges  rest number of hops
     * @return number of trace
     * @isExactly is exactly hops, or max hops
     */
    private int getNumOfPathsWithLimitedEdges(Character source, Character destination, boolean isExactly, int numOfEdges)
            throws GraphException {
        AdjacencyList startNode = nodes.get(source);
        if (null == startNode) {
            throw new GraphException(String.format("starNodeName %c doesn't exist ", source));
        }
        if (numOfEdges < 1) {
            return 0;
        }
        int[] traceNumber = {0};
        if (startNode.children.containsKey(destination)) {
            if (!isExactly || 1 == numOfEdges) {
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
     *
     * @param source      - source node name
     * @param destination - destination node name
     * @return the shortest path
     * 1. Optional.empty() : there is no path between source node and destination node
     * 2. not empty: Map.Entry<Integer, List<Character>> - description the shortest path, including:
     *      * @Integer - the instance of the path
     *      * @List<Character> - serious of nodes in the Path. i.e: ['A', 'B' 'C'] means the shortest path is composed of nodes 'A', 'B' 'C'.
     * @throws NotFoundException - source/destination node doesn't exist in the graph
     * @throws GraphException    - internal error in the Graph
     */
    @Override
    public Optional<Map.Entry<Integer, List<Character>>> getShortestPath(Character source, Character destination)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException("can't find the source node in the graph");
        }
        if (!nodes.containsKey(destination)) {
            throw new NotFoundException("can't find the destination node in the graph");
        }

        Vertex[] vertices = new Vertex[nodes.size()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(null
                    , (char) (i + Const.A)
                    , Integer.MAX_VALUE
            );
        }

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        vertices[source - Const.A].distance = 0;
        queue.add(vertices[source - Const.A]);
        boolean isFirstEdge = true;
        while (!queue.isEmpty()) {
            Vertex minVertex = queue.poll();

            if (!isFirstEdge) {
                minVertex.visited = true;
                // means reach the destination, have to avoid source == destination && first edge
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
                /**
                 * update is required in TWO seniors:
                 *    1: new instance < current instance
                 *    2: current instance == 0 and current vertex is destination vertex
                 */
                if ((subDistance + minVertex.distance < curVertex.distance)
                        || (0 == curVertex.distance && destination.equals(curVertex.destination))) {
                    curVertex.previous = minVertex.destination;
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
            Map.Entry<Integer, List<Character>> shortestPath = new AbstractMap.SimpleEntry<>(vertices[destination - Const.A].distance, pathList);
            return Optional.of(shortestPath);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get a list from a vertex array, to present a path to reach destination node.
     *
     * @param vertexArray - vertex array, i.e: [nullA0,AB5,BC9,AD5,AE7]
     * @param destination - destination node, i.e: 'C'
     * @return ['A','B','C']
     *
     * Description: From index of destination, to find previous node, until null
     * [nullA0,AB5,BC9,AD5,AE7], 'C' means from BC9: vertexArray[2] ->  vertexArray[1] -> vertexArray[0], and then reverse it.
     */
    private List<Character> getPathInfoFromArray(Vertex[] vertexArray, Character destination) {
        if (0 == vertexArray.length) {
            return new LinkedList<>();
        }
        List<Character> pathList = new LinkedList<>();
        pathList.add(destination);
        Character previous = vertexArray[destination - Const.A].previous;
        while (null != previous) {
            pathList.add(0, previous);
            if (destination.equals(previous)) {
                break;
            }
            previous = vertexArray[previous - Const.A].previous;
        }
        return pathList;
    }

    /**
     * Get trace number between source and destination with distance limitation, Implemented with DFS Algorithm
     *
     * @param source      - source node name
     * @param destination - destination node name
     * @param lessThanDistance - limitation of max distance
     * @return trace number
     * @throws NotFoundException - source node doesn't exist
     * @throws GraphException    - internal error in the Graph
     */
    @Override
    public int getTraceNumInDistance(Character source, Character destination, int lessThanDistance)
            throws NotFoundException, GraphException {
        if (!nodes.containsKey(source)) {
            throw new NotFoundException("can't find the node in the graph");
        }
        if (!nodes.containsKey(destination)) {
            throw new NotFoundException("destination does not exist");
        }
        return getTraceNumInDistanceInternal(source, destination, lessThanDistance - 1);
    }

    private int getTraceNumInDistanceInternal(Character source, Character finalDestination, int restDistance)
            throws GraphException {
        AdjacencyList startNode = nodes.get(source);
        if (null == startNode) {
            throw new GraphException(String.format("starNodeName %c doesn't exist ", source));
        }
        if (restDistance < 0) {
            return 0;
        }
        int[] traceNumber = {0};
        if (startNode.children.containsKey(finalDestination) && startNode.children.get(finalDestination) <= restDistance) {
            traceNumber[0] = 1;
        }
        startNode.children.entrySet().forEach(
                curDestination -> {
                    int curNumOfEdges = 0;
                    try {
                        curNumOfEdges = getTraceNumInDistanceInternal(curDestination.getKey(), finalDestination, restDistance - curDestination.getValue());
                    } catch (GraphException e) {
                        e.printStackTrace();
                    }
                    traceNumber[0] += curNumOfEdges;
                }
        );
        return traceNumber[0];
    }
}
