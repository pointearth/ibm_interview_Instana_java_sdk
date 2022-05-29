package com.instana.common;

import com.instana.graph.Edge;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Common tool to validate and serialize data
 *
 * @author Simon
 */
public class Tools {
    private static final String SLASH = "/";

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
            if (isValidNodeName(paths[i].trim().charAt(0))) {
                nodes[i] = paths[i].trim().charAt(0);
            } else {
                return null;
            }
        }
        return nodes;
    }

    /**
     * Parse Input graph into an edge list
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
        if (0 == edges.length) {
            return null;
        }
        List<Edge> list = new ArrayList<>();
        for (String s : edges) {
            Edge edge = parseEdge(s.trim());
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
            if (isValidNodeName(edgeInfo.trim().charAt(0))) {
                edge.from = edgeInfo.charAt(0);
            } else {
                return null;
            }
            if (isValidNodeName(edgeInfo.trim().charAt(1))) {
                edge.to = edgeInfo.trim().charAt(1);
            } else {
                return null;
            }
            String weightStr = edgeInfo.substring(2, edgeInfo.length());
            edge.distance = Integer.parseInt(weightStr);
            return edge;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isValidNodeName(Character nodeName) {
        return null != nodeName && Const.A <= nodeName && nodeName <= Const.Z;
    }

    public String readFromInputStream(String fileName) throws IOException {
        InputStream inputStream;
        if (!fileName.contains(SLASH)) {
            ClassLoader classLoader = getClass().getClassLoader();
            inputStream = classLoader.getResourceAsStream(fileName);
        } else {
            inputStream = new FileInputStream(fileName);
        }
        if (null == inputStream) {
            throw new IOException("Can't find the file");
        }

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
