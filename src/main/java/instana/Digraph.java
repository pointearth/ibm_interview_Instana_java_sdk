package instana;

import java.util.*;

public class Digraph {
    private static final String INPUT_SEPARATOR = ",";
    private static final String PATH_SEPARATOR = "-";
    private Map<String, Node> nodes;

    /***
     * Construct Directed Graph with edges
     * @param edgesInfo
     */
    public Digraph(String edgesInfo) {
        nodes = new HashMap<>();
        List<Map.Entry<String, Node>> list = ParserInput(edgesInfo);
        if (null == list) {
            // log
            return;
        }
        for (Map.Entry<String, Node> entry : list) {
            if (!nodes.containsKey(entry.getKey())) {
                nodes.put(entry.getKey(), new Node(entry.getKey(), 0));
            }
            Node curNode = nodes.get(entry.getKey());
            curNode.children.put(entry.getValue().name, entry.getValue());
        }
    }

    private List<Map.Entry<String, Node>> ParserInput(String edgesInfo) {
        if (null == edgesInfo || edgesInfo.isEmpty()) {
            return null;
        }
        String[] edges = edgesInfo.split(INPUT_SEPARATOR);
        if (null == edges || 0 == edges.length) {
            return null;
        }
        List<Map.Entry<String, Node>> list = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            Map.Entry<String, Node> entry = parseNodes(edges[i].trim());
            if (null != entry) {
                list.add(entry);
            } else {
                return null;
            }
        }
        return list;
    }

    /**
     * parse String into nodes
     * @param edge: like "AD5"
     * @return
     */
    private Map.Entry<String, Node> parseNodes(String edge) {
        try {
            String parentNodeName = edge.substring(0, 1);
            String nodeName = edge.substring(1, 2);
            String weightStr = edge.substring(2, edge.length());
            int weight = Integer.parseInt(weightStr);
            return new AbstractMap.SimpleEntry<>(parentNodeName, new Node(nodeName, weight));
        } catch (Exception ex) {
            // log
            return null;
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
        if (null == pathInfo || pathInfo.isEmpty()) {
            return -2;
        }
        String[] paths = pathInfo.split(PATH_SEPARATOR);
        if (null == paths || 0 == paths.length) {
            return -2;
        }

        int latency = 0;
        for (int i = 1; i < paths.length; i++) {
            String preNodeName = paths[i].trim();
            String nodeName = paths[i - 1].trim();
            if (!nodes.containsKey(nodeName)) {
                return -1;
            }
            Node startNode = nodes.get(nodeName);
            if (!startNode.children.containsKey(preNodeName)) {
                return -1;
            } else {
                Node endNode = startNode.children.get(preNodeName);
                latency += endNode.weight;
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
     */
    public int getTraceNumberInHops(String starNodeName, String endNodeName, int maxHops) {
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        Node startNode = nodes.get(starNodeName);
        String path = starNodeName;
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
    public int getTraceNumberEqualHops(String starNodeName, String endNodeName, int exactlyHops) {
        if (!nodes.containsKey(starNodeName)) {
            return 0;
        }
        Node startNode = nodes.get(starNodeName);
        String path = starNodeName;
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
    private int bFSGetTraceNumberWithHops(Node startNode, String endNodeName, boolean bExactlyHops, int restHops, String path) {
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
        for (Node subNode : startNode.children.values()) {
            if (nodes.containsKey(subNode.name)) {
                Node curStartNode = nodes.get(subNode.name);
                traceNumber += bFSGetTraceNumberWithHops(curStartNode, endNodeName, bExactlyHops,restHops - 1, path+"-"+subNode.name);
            }
        }
        return traceNumber;
    }
    public int dijkstraGetMinDistance(String startNodeName, String endNodeName ) {
        return -1;
    }
}
