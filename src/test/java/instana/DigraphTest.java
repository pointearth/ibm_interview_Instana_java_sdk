package instana;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DigraphTest {
    private static Digraph digraph;
    @BeforeAll
    static void setUp() {
        digraph = new Digraph("AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7");
    }

    @Test
    void getLatency1() {
        int latency = digraph.getLatency("A-B-C");
        assertEquals(9, latency);
    }
}