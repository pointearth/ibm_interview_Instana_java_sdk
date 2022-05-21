package instana;

import java.util.*;

/**
 * @author Simon
 */
public class Digraph {


    private Map<Character, AdjacencyListNode> nodes;

    /***
     * Construct Directed Graph with edges
     * @param edgesInfo
     */
    public Digraph(String edgesInfo) {
        nodes = new HashMap<>();
        List<Vertex> list = CommonTools.parseInput(edgesInfo);
        if (null == list) {
            // log
            return;
        }
        for (Vertex vertex : list) {
            if (!nodes.containsKey(vertex.previous)) {
                nodes.put(vertex.previous, new AdjacencyListNode(vertex.destination, 0));
            }
            AdjacencyListNode curNode = nodes.get(vertex.previous);
            curNode.children.put(vertex.destination, vertex.weight);
        }
    }

    /***
     *
     * @param pathInfo: nodes in the trace, such as A-B-C
     * @return
     *  -2: pathInfo' format error
     *  -1: no such trace
     *  >=0: average Latencies
     */
    public int getLatency(String pathInfo) {
        int latency = 0;
        Character[] pathNodes = CommonTools.parsePath(pathInfo);
        if (null == pathNodes) {
            return ErrorCode.INPUT_ERROR;
        }
        for (int i = 1; i < pathNodes.length; i++) {
            if (!nodes.containsKey(pathNodes[i - 1])) {
                return ErrorCode.NOT_EXIST;
            }
            AdjacencyListNode startNode = nodes.get(pathNodes[i - 1]);
            if (!startNode.children.containsKey(pathNodes[i])) {
                return ErrorCode.NOT_EXIST;
            } else {
                Integer endNodeWight = startNode.children.get(pathNodes[i]);
                latency += endNodeWight;
            }
        }
        return latency;
    }

    /**
     * get the number of traces from service startNodeName to service endNodeName with maxHops limitation
     * @param starNodeName start service name
     * @param endNodeName end service name
     * @param maxHops limitation of max hops
     * @return
     * -2: format error
     * 0<=: trace number
     */
    public int getTraceNumberInHops(Character starNodeName, Character endNodeName, int maxHops) {
        if (null == starNodeName || null == endNodeName) {
            return ErrorCode.INPUT_ERROR;
        }
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        AdjacencyListNode startNode = nodes.get(starNodeName);
        String path = starNodeName.toString();
        int traceNumber = bFSGetTraceNumberWithHops(startNode, endNodeName,false, maxHops, path);
        return traceNumber;
    }
    /**
     * get the number of traces from service startNodeName to service endNodeName equal the expected Hops
     * @param starNodeName start service name
     * @param endNodeName end service name
     * @param exactlyHops expect number of hops
     * @return
     */
    public int getTraceNumberEqualHops(Character starNodeName, Character endNodeName, int exactlyHops) {
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        AdjacencyListNode startNode = nodes.get(starNodeName);
        String path = starNodeName.toString();
        int traceNumber = bFSGetTraceNumberWithHops(startNode, endNodeName,true, exactlyHops, path);
        return traceNumber;
    }

    /**
     * recursive BSF method to
     * @param startNode   start service
     * @param endNodeName end service name
     * @bExactlyHops is exactly hops, or maxHops
     * @param restHops rest number of hops
     * @param path help to show path
     * @return number of trace
     */
    private int bFSGetTraceNumberWithHops(AdjacencyListNode startNode, Character endNodeName, boolean bExactlyHops, int restHops, String path) {
        System.out.println("current path:" + path);
        if (restHops < 1) {
            System.out.println("---reach limitation: "+ path);
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
                AdjacencyListNode curStartNode = nodes.get(nodeName);
                traceNumber += bFSGetTraceNumberWithHops(curStartNode, endNodeName, bExactlyHops,restHops - 1, path+"-"+nodeName);
            }
        }
        return traceNumber;
    }

    /**
     *
     * @param source
     * @param destination
     * @return
     * -2: input error
     * -1: no such trace
     * 0<=: shortest trace
     */
    public int dijkstraGetMinDistance(Character source, Character destination) {
        if (null == source || null == destination
                ||  !nodes.containsKey(source)
                ||  !nodes.containsKey(destination) ) {
            return ErrorCode.INPUT_ERROR;
        }

        int[] distances = new int[nodes.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        boolean[] visited = new boolean[nodes.size()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        // for each vertex, save its previous vertex index(name)
        int[] explores = new int[nodes.size()];
        for (int i = 0; i < explores.length; i++) {
            // -1 means the 'previous' is unknown yet
            explores[i] = ErrorCode.NOT_EXIST;
        }

//        Map<Character, Vertex> exploredVertices = new HashMap<>();

        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.weight));

        distances[source - Const.A] = 0;
        queue.add(new Vertex(null, source,0));


        while (!queue.isEmpty()) {
            Vertex sVertex = queue.poll();
            distances[sVertex.destination - Const.A] = sVertex.weight;
            explores[sVertex.destination - Const.A] = sVertex.previous - Const.A;
            visited[sVertex.destination - Const.A] = true;

            if (sVertex.equals(destination)) {
                break;
            }
            if (!nodes.containsKey(sVertex.destination)) {
                return ErrorCode.NOT_EXIST;
            }
            AdjacencyListNode node = nodes.get(sVertex.destination);

//            for( AdjacencyListNode subNode : node.children.values()) {
////                subNode.
////                queue.offer( new Vertex())
//            }
        }
        return ErrorCode.NOT_EXIST;
    }
}
