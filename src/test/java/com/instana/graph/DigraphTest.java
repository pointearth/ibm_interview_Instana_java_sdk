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
                realInstance -> assertEquals(expectLatency.get(), realInstance), () -> assertTrue(instance.isEmpty()));
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
    @Order(5)
    void getInstanceShouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            iGraph.getInstance('Z', 'B');
        });
    }

    @Test
    void getTraceNumberInMaxHops() {
    }

    @Test
    void getTraceNumberExactlyHops() {
    }


    @ParameterizedTest
    @Order(4)
    @MethodSource("getShortestPathDataProvider")
    void getShortestPath(Character startNodeName, Character endNodeName, Map.Entry<Integer,List<Character>> expectResult) throws NotFoundException, GraphException {
        Optional<Map.Entry<Integer, List<Character>>> result = iGraph.getShortestPath(startNodeName, endNodeName);
        result.ifPresentOrElse(
                realResult -> {
                    assertEquals(expectResult.getValue(), realResult.getValue());
                    assertEquals(expectResult.getKey(), realResult.getKey());
                    }, () -> {assertTrue(result.isEmpty());}
        );
    }

    static Stream<Arguments> getShortestPathDataProvider() {
        return Stream.of(
                Arguments.of('A', 'C',new AbstractMap.SimpleEntry<Integer, List>(9, Arrays.asList('A', 'B', 'C'))),
                Arguments.of('B', 'B',new AbstractMap.SimpleEntry<Integer, List>(9, Arrays.asList('B', 'C', 'E', 'B'))),
                Arguments.of('E', 'D', new AbstractMap.SimpleEntry<Integer, List>(15, Arrays.asList('E', 'B', 'C', 'D')))
        );
    }

    @Test
    void getPathInfoFromArray() {
    }
}