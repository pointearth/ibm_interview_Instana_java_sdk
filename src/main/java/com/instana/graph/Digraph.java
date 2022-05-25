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
        if (!nodes.containsKey(edge.previous)) {
            nodes.put(edge.previous, new AdjacencyList(edge.previous));
        }
        AdjacencyList curNode = nodes.get(edge.previous);
        curNode.children.put(edge.destination, edge.weight);
    }

    @Override
    public void removeEdge(Edge edge) {
        if (nodes.containsKey(edge.previous)) {
            nodes.get(edge.previous).children.remove(edge.destination);
            if (0 == nodes.get(edge.previous).children.size()) {
                nodes.remove(edge.previous);
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
        // distances array presents the distances from source to vertices
        int[] distances = new int[nodes.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        // visited array presents whether the edge is visited
        boolean[] visited = new boolean[nodes.size()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        // for each vertex, save its previous vertex index(name), for SPT
        int[] explores = new int[nodes.size()];
        for (int i = 0; i < explores.length; i++) {
            // -1 means the 'previous' is unknown yet
            explores[i] = ErrorCode.NOT_EXIST.getValue();
        }

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.weight));
        queue.add(new Vertex(null, source, 0));
        boolean isFirstEdge = true;
        while (!queue.isEmpty()) {
            Vertex minVertex = queue.poll();
            // ignore if it is already visited
            if (visited[minVertex.destination - Const.A]) {
                continue;
            }
            // avoid source == destination
            if (minVertex.destination.equals(destination) && visited[source - Const.A]) {
                explores[destination - Const.A] = minVertex.source - Const.A;
                break;
            }
            // the trace doesn't exist
            if (!nodes.containsKey(minVertex.destination)) {
                throw new GraphException("can't find destination in priority queue");
            }
            AdjacencyList node = nodes.get(minVertex.destination);

            for (Map.Entry<Character, Integer> subEdge : node.children.entrySet()) {
                Character subDestination = subEdge.getKey();
                Integer subDistance = subEdge.getValue();
                // update distances array
                if (!visited[subDestination - Const.A] && subDistance + minVertex.weight < distances[subDestination - Const.A]) {
                    distances[subDestination - Const.A] = subDistance + minVertex.weight;
                    queue.add(new Vertex(minVertex.destination, subDestination, subDistance + minVertex.weight));

                    explores[subDestination - Const.A] = minVertex.destination - Const.A;
                }
            }

            if (!isFirstEdge || !source.equals(minVertex.destination)) {
                visited[minVertex.destination - Const.A] = true;
            }
            isFirstEdge = false;
        }
        List<Character> pathList = getPathInfoFromArray(explores, destination);
        if (Integer.MAX_VALUE != distances[destination - Const.A]) {
            Map.Entry<Integer,List<Character>> shortestPath = new AbstractMap.SimpleEntry<>(distances[destination - Const.A],pathList );
            return Optional.of(shortestPath);
        } else {
            return Optional.empty();
        }
    }
    public List<Character> getPathInfoFromArray(int[] explores, Character destination) {
        if (0 == explores.length) {
            return new LinkedList<>();
        }
        List<Character> pathList = new LinkedList<>();
        pathList.add(destination);
        int previousIndex = explores[destination - Const.A];
        while (previousIndex != -1) {
            pathList.add(0,(char) (previousIndex + Const.A));
////            String previousPath = String.format("%c-", (char) (previousIndex + Const.A));
//            sb.insert(0, previousPath);
            // it is a loop, means reach the start node
            if (previousIndex == destination - Const.A) {
                break;
            }
            previousIndex = explores[previousIndex];
        }
        return pathList;
    }
//    public List<List<Character>> getTraceNumberInLatency(Character source, Character destination ) {
//
//    }
}
