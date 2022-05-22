package instana;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;


/**
 * Common tool to validate and serialize data
 *
 * @author Simon
 */
public class CommonTools {

    /**
     * Parse pathInfo
     *
     * @param pathInfo: presenting path, i.e:  "A-B-C"
     * @return: a Character array presenting nodes in the path
     * i.e: ['A', 'B', 'C']
     * null : input error
     */
    public static Character[] parsePath(String pathInfo) {
        if (null == pathInfo || pathInfo.isEmpty()) {
            return null;
        }
        String[] paths = pathInfo.split(Const.PATH_SEPARATOR);
        if (null == paths || 0 == paths.length) {
            return null;
        }
        Character[] nodes = new Character[paths.length];
        for (int i = 0; i < paths.length; i++) {
            if (isValidNodeName(paths[i])) {
                nodes[i] = paths[i].trim().charAt(0);
            } else {
                return null;
            }
        }
        return nodes;
    }

    /**
     * Parse Input graph into a vertex list
     *
     * @param edgesInfo: input information to create the graph,
     *                   i.e: "AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7"
     * @return a list presenting all the edges
     * null: edgesInfo is wrong
     */
    public static List<Vertex> parseInput(String edgesInfo) {
        if (null == edgesInfo || edgesInfo.isEmpty()) {
            return null;
        }
        String[] edges = edgesInfo.split(Const.INPUT_SEPARATOR);
        if (null == edges || 0 == edges.length) {
            return null;
        }
        List<Vertex> list = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            Vertex vertex = parseNodes(edges[i].trim());
            if (null != vertex) {
                list.add(vertex);
            } else {
                return null;
            }
        }
        return list;
    }

    /**
     * parse String into vertex
     *
     * @param edge: like "AD5"
     * @return a vertex
     * null: input error
     */
    public static Vertex parseNodes(String edge) {
        if (null == edge || edge.length() < 3) {
            return null;
        }
        Vertex vertex = new Vertex();
        try {
            if (isValidNodeName(edge.substring(0, 1))) {
                vertex.previous = edge.charAt(0);
            } else {
                return null;
            }
            if (isValidNodeName(edge.substring(1, 2))) {
                vertex.destination = edge.charAt(1);
            } else {
                return null;
            }
            String weightStr = edge.substring(2, edge.length());
            vertex.weight = Integer.parseInt(weightStr);
            return vertex;
        } catch (Exception ex) {
            // log
            return null;
        }
    }

    private static boolean isValidNodeName(String nodeName) {
        return 1 == nodeName.trim().length()
                && Const.A <= nodeName.trim().charAt(0)
                && nodeName.trim().charAt(0) <= Const.Z;
    }

    public static String getPathInfoFromArray(int[] explores, Character destination) {
        if (0 == explores.length) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(destination);
        int previousIndex = explores[destination - Const.A];
        while (previousIndex != -1) {
            String previousPath = String.format("%c-", (char) (previousIndex + Const.A));
            sb.insert(0, previousPath);
            // it is a loop, means reach the start node
            if (previousIndex == destination - Const.A) {
                break;
            }
            previousIndex = explores[previousIndex];
        }
        return sb.toString();
    }
}
