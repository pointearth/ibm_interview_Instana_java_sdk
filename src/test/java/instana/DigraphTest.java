package instana;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DigraphTest {
    private static Digraph digraph;

    @BeforeAll
    static void setUp() {
        digraph = new Digraph("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7");
    }

    @ParameterizedTest
    @CsvSource({"A-B-C, 9", "A-D, 5", "A-D-C, 13", "A-E-B-C-D, 22", "A-E-D, -1", "A-Z, -1", ", -2", " , -2", " A- D, 5", " #A- D, -1"})
    void getLatency(String pathInfo, int expectLatency) {
        int latency = digraph.getLatency(pathInfo);
        assertEquals(expectLatency, latency);
    }

    @ParameterizedTest
    @CsvSource({"C, C, 3, 2", "C, C, 2, 1","A, D, 3, 4"})
    void getTraceNumberInHops(String startNodeName, String endNodeName, int maxHops, int expectTraceNumber) {
        Digraph digraphLocal = new Digraph("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7, DA6");
        int traceNumber = digraphLocal.getTraceNumberInHops(startNodeName, endNodeName, maxHops);
        assertEquals(expectTraceNumber, traceNumber);
    }

    @ParameterizedTest
    @CsvSource({"A, C, 4, 3"})
    void getTraceNumberEqualHops(String startNodeName, String endNodeName, int exactlyHops, int expectTraceNumber) {
        int traceNumber = digraph.getTraceNumberEqualHops(startNodeName, endNodeName, exactlyHops);
        assertEquals(expectTraceNumber, traceNumber);
    }

    @ParameterizedTest
    @CsvSource({"A, C, 9", "B, B, 9"})
    void dijkstraGetMinDistance(String startNodeName, String endNodeName, int expectMinDistance ) {
        int minDistance = digraph.dijkstraGetMinDistance(startNodeName,endNodeName);
        assertEquals(expectMinDistance, minDistance);
    }
}