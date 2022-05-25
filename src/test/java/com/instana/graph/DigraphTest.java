package com.instana.graph;

import com.instana.common.Const4Test;
import com.instana.common.Tools;
import com.instana.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DigraphTest {
    private static IGraph iGraph;

    @BeforeAll
    static void setUp() {
        iGraph = new Digraph();
    }

    @Test
    @Order(1)
    void addEdge() {
        iGraph.addEdge(new Edge('A', 'B', 5));
        assertEquals(1, iGraph.getNumOfVertices());
        assertEquals(1, iGraph.getNumOfEdges());
    }

    @Test
    @Order(2)
    void removeEdge() {
        iGraph.removeEdge(new Edge('A', 'B', 5));
        assertEquals(0, iGraph.getNumOfVertices());
        assertEquals(0, iGraph.getNumOfEdges());
    }

    @Test
    @Order(3)
    void loadData() {
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
        iGraph.loadData(edgeList);
        assertEquals(5, iGraph.getNumOfVertices());
        assertEquals(9, iGraph.getNumOfEdges());
    }

    @ParameterizedTest
    @Order(4)
    @MethodSource("getInstanceDataProvider")
    void getInstance(Character source, Character destination, Optional<Integer> expectLatency) throws NotFoundException {
        Optional<Integer> instance = iGraph.getInstance(source, destination);
        instance.ifPresentOrElse(
                curIntance -> assertEquals(expectLatency.get(), curIntance), () -> assertTrue(instance.isEmpty()));
    }

    static Stream<Arguments> getInstanceDataProvider() {
        return Stream.of(
                Arguments.of('A', 'B', Optional.of(5))
                , Arguments.of('A', 'D', Optional.of(5))
                , Arguments.of('A', 'E', Optional.of(7))
                , Arguments.of('E', 'A', Optional.empty())
                , Arguments.of('A', 'C', Optional.empty())
                , Arguments.of('A', 'Z', Optional.empty())
        );
    }

    @Test
    void getTraceNumberInMaxHops() {
    }

    @Test
    void getTraceNumberExactlyHops() {
    }

    @Test
    void getShortestPath() {
    }

    @Test
    void getPathInfoFromArray() {
    }
}