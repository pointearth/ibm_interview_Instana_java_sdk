package com.instana.graph;

import com.instana.exception.GraphException;
import com.instana.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class IGraphTest {
    private static IGraph iGraph;

    @BeforeAll
    static void setUp() {
        iGraph = new Digraph();
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
    }


    @ParameterizedTest
    @MethodSource("getDirectInstanceDataProvider")
    void getDirectInstance(Character source, Character destination, Optional<Integer> expectLatency) throws NotFoundException {
        Optional<Integer> instance = iGraph.getDirectInstance(source, destination);
        instance.ifPresentOrElse(
                realInstance -> assertEquals(expectLatency.get(), realInstance), () -> assertTrue(instance.isEmpty()));
    }

    static Stream<Arguments> getDirectInstanceDataProvider() {
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
    void getInstanceShouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            iGraph.getDirectInstance('Z', 'B');
        });
    }

    @ParameterizedTest
    @MethodSource("getShortestPathDataProvider")
    void getShortestPath(Character startNodeName, Character endNodeName, Map.Entry<Integer, List<Character>> expectResult) throws NotFoundException, GraphException {
        Optional<Map.Entry<Integer, List<Character>>> result = iGraph.getShortestPath(startNodeName, endNodeName);
        result.ifPresentOrElse(
                realResult -> {
                    assertEquals(expectResult.getValue(), realResult.getValue());
                    assertEquals(expectResult.getKey(), realResult.getKey());
                }, () -> {
                    assertTrue(result.isEmpty());
                }
        );
    }

    static Stream<Arguments> getShortestPathDataProvider() {
        return Stream.of(
                Arguments.of('A', 'C', new AbstractMap.SimpleEntry<Integer, List>(9, Arrays.asList('A', 'B', 'C'))),
                Arguments.of('B', 'B', new AbstractMap.SimpleEntry<Integer, List>(9, Arrays.asList('B', 'C', 'E', 'B'))),
                Arguments.of('E', 'D', new AbstractMap.SimpleEntry<Integer, List>(15, Arrays.asList('E', 'B', 'C', 'D')))
        );
    }

    @Test
    void getPathNumInEdgeNum() throws NotFoundException, GraphException {
        assertEquals(2, iGraph.getPathNumInEdgeNum('C', 'C', 3));
    }

    @Test
    void getPathNumInEdgeNumShouldThrowNotFoundException() throws NotFoundException, GraphException {
        Assertions.assertThrows(NotFoundException.class, () -> {
            iGraph.getPathNumInEdgeNum('Z', 'C', 3);
        });
    }

    @Test
    void getPathNumEqualEdgeNum() throws NotFoundException, GraphException {
        assertEquals(3, iGraph.getPathNumEqualEdgeNum('A', 'C', 4));
    }

    @Test
    void getTraceNumInDistance() throws GraphException, NotFoundException {
        assertEquals(7, iGraph.getTraceNumInDistance('C', 'C', 30));
    }

    @Test
    void getTraceNumInDistanceShouldThrowNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            iGraph.getTraceNumInDistance('Z', 'C', 30);
        });
    }
}