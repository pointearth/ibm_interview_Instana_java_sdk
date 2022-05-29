package com.instana;

import com.instana.common.Const4Test;
import com.instana.exception.GraphException;
import com.instana.exception.NotFoundException;
import com.instana.graph.Digraph;
import com.instana.graph.Edge;
import com.instana.graph.IGraph;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class TraceServiceTest {
    private static TraceService traceService;
    private static IGraph iGraph;
    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        traceService = new TraceService();
        mockGraph();
        FieldSetter.setField(traceService, traceService.getClass().getDeclaredField("iGraph"), iGraph);
    }
    private static void mockGraph() {
        iGraph = mock(Digraph.class);
        Stream<Arguments> mockedEdges = mockDataProvider();
        mockedEdges.forEach(
                curEdge -> {
                    try {
                        when(iGraph.getDirectInstance((Character) curEdge.get()[0], (Character) curEdge.get()[1])).thenReturn((Optional<Integer>) curEdge.get()[2]);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
    static Stream<Arguments> mockDataProvider() {
        return Stream.of(
                Arguments.of('A', 'B', Optional.of(5))
                ,Arguments.of('B', 'C', Optional.of(4))
                , Arguments.of('C', 'D', Optional.of(8))
                , Arguments.of('D', 'C', Optional.of(8))
                , Arguments.of('D', 'E', Optional.of(6))
                , Arguments.of('A', 'D', Optional.of(5))
                , Arguments.of('C', 'E', Optional.of(2))
                , Arguments.of('E', 'B', Optional.of(3))
                , Arguments.of('A', 'E', Optional.of(7))
        );
    }
    @Test
    void loadData() {
        List<Edge> edgeList = traceService.loadData(Const4Test.DATA_FILE);
        assertEquals(9, edgeList.size());
    }

    @ParameterizedTest
    @MethodSource("getDirectInstanceDataProvider")
    void getLatency(String pathInfo, int totalLatency) throws NotFoundException {
        assertEquals(totalLatency, traceService.getLatency(pathInfo));
    }
    static Stream<Arguments> getDirectInstanceDataProvider() {
        return Stream.of(
                Arguments.of("A-B-C", 9)
                , Arguments.of("A-D", 5)
                , Arguments.of("A-D-C", 13)
                , Arguments.of("A-E-B-C-D",22)
        );
    }
    @Test
    void getTraceNumInHops() throws GraphException, NotFoundException {
        when(iGraph.getPathNumInEdgeNum('C','C', 3)).thenReturn(2);
        assertEquals(2,traceService.getTraceNumInHops('C', 'C', 3));
    }

    @Test
    void getTraceNumEqualHops() throws GraphException, NotFoundException {
        when(iGraph.getPathNumEqualEdgeNum('A','C', 4)).thenReturn(3);
        assertEquals(3,traceService.getTraceNumEqualHops('A', 'C', 4));
    }

    @Test
    void getLenShortestTrace() throws GraphException, NotFoundException {
        when(iGraph.getShortestPath('A','C')).thenReturn(
                Optional.of(new AbstractMap.SimpleEntry<>(9, Arrays.asList('A','B','C')))
        );
        assertEquals(9,traceService.getLenShortestTrace('A', 'C'));
    }
}