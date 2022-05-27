package com.instana.graph;

import org.junit.jupiter.api.*;

import java.util.LinkedList;
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
        assertEquals(1, iGraph.getAmountOfSourceNodes());
        assertEquals(1, iGraph.getAmountOfEdges());
    }

    @Test
    @Order(2)
    void removeEdge() {
        iGraph.removeEdge(new Edge('A', 'B', 5));
        assertEquals(0, iGraph.getAmountOfSourceNodes());
        assertEquals(0, iGraph.getAmountOfEdges());
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
        assertEquals(5, iGraph.getAmountOfSourceNodes());
        assertEquals(9, iGraph.getAmountOfEdges());
    }
}