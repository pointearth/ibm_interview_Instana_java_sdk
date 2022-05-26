package com.instana.common;

import com.instana.graph.Edge;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Common tool to validate and serialize data
 *
 * @author Simon
 */
public class Tools {

    /**
     * Parse pathInfo
     *
     * @param pathInfo: presenting path, i.e:  "A-B-C"
     * @return: a Character array presenting nodes in the path
     * i.e: ['A', 'B', 'C']
     * null : input error
     */
    public Character[] parsePath(String pathInfo) {
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
     * Parse Input graph into a edge list
     *
     * @param edgesInfo: input information to create the graph,
     *                   i.e: "AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7"
     * @return a list presenting all the edges
     * null: edgesInfo is wrong
     */
    public List<Edge> parseInput(String edgesInfo) {
        if (null == edgesInfo || edgesInfo.isEmpty()) {
            return null;
        }
        String[] edges = edgesInfo.split(Const.INPUT_SEPARATOR);
        if (null == edges || 0 == edges.length) {
            return null;
        }
        List<Edge> list = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            Edge edge = parseEdge(edges[i].trim());
            if (null != edge) {
                list.add(edge);
            } else {
                return null;
            }
        }
        return list;
    }

    /**
     * parse String into edges
     *
     * @param edgeInfo: like "AD5"
     * @return a edge
     * null: input error
     */
    public Edge parseEdge(String edgeInfo) {
        if (null == edgeInfo || edgeInfo.length() < 3) {
            return null;
        }
        Edge edge = new Edge();
        try {
            if (isValidNodeName(edgeInfo.substring(0, 1))) {
                edge.from = edgeInfo.charAt(0);
            } else {
                return null;
            }
            if (isValidNodeName(edgeInfo.substring(1, 2))) {
                edge.to = edgeInfo.charAt(1);
            } else {
                return null;
            }
            String weightStr = edgeInfo.substring(2, edgeInfo.length());
            edge.instance = Integer.parseInt(weightStr);
            return edge;
        } catch (Exception ex) {
            // log
            return null;
        }
    }

    private boolean isValidNodeName(String nodeName) {
        return 1 == nodeName.trim().length()
                && Const.A <= nodeName.trim().charAt(0)
                && nodeName.trim().charAt(0) <= Const.Z;
    }

    public String readFromInputStream(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
