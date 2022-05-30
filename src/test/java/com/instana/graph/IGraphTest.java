package com.instana.graph;

import com.instana.exception.GraphException;
import com.instana.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
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
        iGraph.loadData(TestDataProvider.createData());
    }


    @ParameterizedTest
    @MethodSource("getDirectInstanceDataProvider")
    void getDirectInstance(Character source, Character destination, Optional<Integer> expectLatency) throws NotFoundException {
        Optional<Integer> instance = iGraph.getDirectInstance(source, destination);
        expectLatency.equals(instance);
    }

    static Stream<Arguments> getDirectInstanceDataProvider() {
        return Stream.of(
                Arguments.of('A', 'B', Optional.of(5))
                , Arguments.of('A', 'D', Optional.of(5))
                , Arguments.of('A', 'E', Optional.of(7))
                , Arguments.of('E', 'A', Optional.empty())
                , Arguments.of('A', 'C', Optional.empty())
        );
    }

    @ParameterizedTest
    @CsvSource({"A,Z", "Z,B"})
    void getInstanceShouldThrowException(Character source, Character destination) {
        Assertions.assertThrows(NotFoundException.class, () -> {
            iGraph.getDirectInstance(source, destination);
        });
    }

    @ParameterizedTest
    @MethodSource("getShortestPathDataProvider")
    void getShortestPath(Character startNodeName, Character endNodeName, Map.Entry<Integer, List<Character>> expectResult) throws NotFoundException, GraphException {
        Optional<Map.Entry<Integer, List<Character>>> result = iGraph.getShortestPath(startNodeName, endNodeName);
        assertNotEquals(Optional.empty(),result );
        result.ifPresent(
                realResult -> {
                    assertEquals(expectResult.getKey(), realResult.getKey());
                    assertEquals(expectResult.getValue(), realResult.getValue());
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

    /**
     * Expect There is no-path from 'A' to 'C', because removed all edges whose destination node is 'C'
     * @throws NotFoundException
     * @throws GraphException
     */
    @Test
    void getShortestPathExpectNoPath() throws NotFoundException, GraphException {
        IGraph individualGraph = new Digraph();
        individualGraph.loadData(TestDataProvider.createDataWithoutDestinationC());
        Optional<Map.Entry<Integer, List<Character>>> result = individualGraph.getShortestPath('A', 'C');
        assertEquals(Optional.empty(),result );
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