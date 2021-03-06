package com.instana.graph;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IGraphModifyTest {

    private static IGraph iGraph;

    @BeforeAll
    static void setUp() {
        iGraph = new Digraph();
    }

    @Test
    @Order(1)
    void addEdge() {
        iGraph.addEdge(new Edge('A', 'B', 5));
        assertEquals(2, iGraph.getAmountOfSourceNodes());
        assertEquals(1, iGraph.getAmountOfEdges());
    }

    @Test
    @Order(2)
    void removeEdge() {
        iGraph.removeEdge(new Edge('A', 'B', 5));
        assertEquals(2, iGraph.getAmountOfSourceNodes());
        assertEquals(0, iGraph.getAmountOfEdges());
    }
    @Test
    @Order(3)
    void loadData() {
        List<Edge> edgeList = TestDataProvider.createData();
        iGraph.loadData(edgeList);
        assertEquals(5, iGraph.getAmountOfSourceNodes());
        assertEquals(9, iGraph.getAmountOfEdges());
    }
}