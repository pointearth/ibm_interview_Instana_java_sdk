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
     * @throws IllegalArgumentException - pathInfo is null/empty or in a wrong format
     */
    public Character[] parsePath(String pathInfo) throws IllegalArgumentException {
        if (null == pathInfo || pathInfo.trim().isEmpty()) {
           throw new IllegalArgumentException("pathInfo is null or empty");
        }
        String[] paths = pathInfo.split(Const.PATH_SEPARATOR);
        if ( 0 == paths.length) {
            throw new IllegalArgumentException("pathInfo format is wrong");
        }
        Character[] nodes = new Character[paths.length];
        for (int i = 0; i < paths.length; i++) {
            if (isValidNodeName(paths[i].trim().charAt(0))) {
                nodes[i] = paths[i].trim().charAt(0);
            } else {
                throw new IllegalArgumentException("pathInfo format is wrong");
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
     * @throws IllegalArgumentException - edgesInfo is null/empty or in a wrong format
     */
    public List<Edge> parseInput(String edgesInfo) throws IllegalArgumentException{
        if (null == edgesInfo || edgesInfo.trim().isEmpty()) {
            throw new IllegalArgumentException("edgesInfo is null or empty");
        }
        String[] edges = edgesInfo.split(Const.INPUT_SEPARATOR);
        if (0 == edges.length) {
            throw new IllegalArgumentException("pathInfo format is wrong");
        }
        List<Edge> list = new ArrayList<>();
        for (String s : edges) {
            Edge edge = parseEdge(s.trim());
            if (null != edge) {
                list.add(edge);
            } else {
                throw new IllegalArgumentException("edgesInfo format is wrong");
            }
        }
        return list;
    }

    /**
     * parse String into edges
     *
     * @param edgeInfo: like "AD5"
     * @return a edge
     * IllegalArgumentException - edgeInfo is null/empty or in a wrong format
     */
    public Edge parseEdge(String edgeInfo) throws IllegalArgumentException {
        if (null == edgeInfo || edgeInfo.trim().length() < 3) {
            throw new IllegalArgumentException("edgeInfo is null or empty");
        }
        Edge edge = new Edge();
        try {
            if (isValidNodeName(edgeInfo.trim().charAt(0))) {
                edge.from = edgeInfo.charAt(0);
            } else {
                throw new IllegalArgumentException("edgeInfo format is wrong at part 1");
            }
            if (isValidNodeName(edgeInfo.trim().charAt(1))) {
                edge.to = edgeInfo.trim().charAt(1);
            } else {
                throw new IllegalArgumentException("edgeInfo format is wrong at part 2");
            }
            String weightStr = edgeInfo.substring(2, edgeInfo.length());
            edge.distance = Integer.parseInt(weightStr);
            return edge;
        } catch (Exception ex) {
            throw new IllegalArgumentException("edgeInfo format is wrong",ex);
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
