package instana;

import java.util.*;

public class Digraph {
    private static final String INPUT_SEPARATOR = ",";
    private static final String PATH_SEPARATOR = "-";
    private Map<String, Node> nodes;

    public Digraph(String edgesInfo) {
        nodes = new HashMap<>();
        List<Map.Entry<String, Node>> list = ParserInput(edgesInfo);
        if (null == list) {
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
            Map.Entry<String, Node> entry = parserNode(edges[i]);
            if (null != entry) {
                list.add(entry);
            }
        }
        return list;
    }

    private Map.Entry<String, Node> parserNode(String edge) {
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
            if (!nodes.containsKey(paths[i - 1])) {
                return -1;
            }
            Node startNode = nodes.get(paths[i - 1]);
            if (!startNode.children.containsKey(paths[i])) {
                return -1;
            } else {
                Node endNode = startNode.children.get(paths[i]);
                latency += endNode.weight;
            }
        }
        return latency;
    }
}
