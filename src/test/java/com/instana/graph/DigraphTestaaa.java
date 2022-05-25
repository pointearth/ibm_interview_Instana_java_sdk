//package com.instana.graph;
//
//import com.instana.common.Const4Test;
//import com.instana.common.Tools;
//import com.instana.graph.Digraph;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class DigraphTestaaa {
//    private static IGraph iGraph;
//    private @Mock static Tools tools;
//    @BeforeAll
//    static void setUp() {
//        iGraph = new Digraph();
//        List<Edge> edgeList = tools.parseInput(Const4Test.PATH_INFO);
//        iGraph.loadData(edgeList);
//    }
//
//    @ParameterizedTest
//    @CsvSource({"A-B-C, 9", "A-D, 5", "A-D-C, 13", "A-E-B-C-D, 22", "A-E-D, -1", "A-Z, -1"})
//    void getLatency(String pathInfo, int expectLatency) {
//        int latency = iGraph.getInstance(pathInfo);
//        assertEquals(expectLatency, latency);
//    }
//
//    @ParameterizedTest
//    @CsvSource({"C, C, 3, 2", "C, C, 2, 1", "A, D, 3, 4"})
//    void getTraceNumberInHops(Character startNodeName, Character endNodeName, int maxHops, int expectTraceNumber) {
//        Digraph digraphLocal = new Digraph("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7, DA6");
//        int traceNumber = digraphLocal.getTraceNumberInHops(startNodeName, endNodeName, maxHops);
//        assertEquals(expectTraceNumber, traceNumber);
//    }
//
//    @ParameterizedTest
//    @CsvSource({"A, C, 4, 3"})
//    void getTraceNumberEqualHops(Character startNodeName, Character endNodeName, int exactlyHops, int expectTraceNumber) {
//        int traceNumber = iGraph.getTraceNumberEqualHops(startNodeName, endNodeName, exactlyHops);
//        assertEquals(expectTraceNumber, traceNumber);
//    }
//
//    @ParameterizedTest
//    @CsvSource({"A, C, 9", "B,B,9"})
//    void dijkstraGetMinDistance(Character startNodeName, Character endNodeName, int expectMinDistance) {
//        int minDistance = iGraph.getShortestPath(startNodeName, endNodeName);
//        assertEquals(expectMinDistance, minDistance);
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("getDataProvider")
//    void getPathInfoFromArray(int[] exploreInfo, Character destination, String expectPathInfo) {
//        String pathInfo = getPathInfoFromArray(exploreInfo, destination);
//        assertEquals(expectPathInfo, pathInfo);
//    }
//static Stream<Arguments> getDataProvider() {
//        return Stream.of(
//        Arguments.of(new int[]{-1, 0, 1, 0, 2}, 'C', "A-B-C")
//        , Arguments.of(new int[]{0, 0, 1, 0, 2}, 'C', "")
//        );
//        }
//}