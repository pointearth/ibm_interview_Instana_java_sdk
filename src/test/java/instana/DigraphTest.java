package instana;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DigraphTest {
    private static Digraph digraph;

    @BeforeAll
    static void setUp() {
        digraph = new Digraph("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7");
    }

    @Test
    @Order(1)
    void getDigraph() {
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource(
            {
                    "A-B-C, 9", "A-D, 5", "A-D-C, 13", "A-E-B-C-D, 22", "A-E-D, -1"
            }
    )
    void getLatency(String pathInfo, int expectLatency) {
        int latency = digraph.getLatency(pathInfo);
        assertEquals(expectLatency, latency);
    }
}