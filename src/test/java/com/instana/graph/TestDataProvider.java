package com.instana.graph;

import org.junit.jupiter.params.provider.Arguments;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestDataProvider {
    public static List<Edge> createData() {
        List<Edge> edgeList = new LinkedList<>();
        edgeList.add(new Edge('A', 'B', 5));
        edgeList.add(new Edge('B', 'C', 4));
        edgeList.add(new Edge('C', 'D', 8));
        edgeList.add(new Edge('D', 'C', 8));
        edgeList.add(new Edge('D', 'E', 6));
        edgeList.add(new Edge('A', 'D', 5));
        edgeList.add(new Edge('C', 'E', 2));
        edgeList.add(new Edge('E', 'B', 3));
        edgeList.add(new Edge('A', 'E', 7));
        return edgeList;
    }

    public static List<Edge> createDataWithoutDestinationC() {
        List<Edge> edgeList = new LinkedList<>();
        edgeList.add(new Edge('A', 'B', 5));
        edgeList.add(new Edge('C', 'D', 8));
        edgeList.add(new Edge('D', 'E', 6));
        edgeList.add(new Edge('A', 'D', 5));
        edgeList.add(new Edge('C', 'E', 2));
        edgeList.add(new Edge('E', 'B', 3));
        edgeList.add(new Edge('A', 'E', 7));
        return edgeList;
    }
}
