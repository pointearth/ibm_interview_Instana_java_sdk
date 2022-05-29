package com.instana.common;

import com.instana.graph.Edge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {
    private Tools tools = new Tools();
    @ParameterizedTest
    @CsvSource({"A-D-C, 3"
            , "A-D-C-D-E-B-A, 7"
            ," A- D, 2"})
    void parsePath(String pathInfo, int expectNodeSize) {
        Character[] result = tools.parsePath(pathInfo);
        int nodeSize = result.length;
        assertEquals(expectNodeSize, nodeSize);
    }
    @ParameterizedTest
    @ValueSource(strings = {" ",""," #A- D"})
    void parsePathExpectThrowException(String pathInfo) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tools.parsePath(pathInfo);
        });
    }
    @ParameterizedTest
    @CsvSource(delimiter = ';'
            , value = {"AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7; 9 "
            , "AB334; 1"})
    void parseInput(String edgesInfo, int expectSize) {
        List<Edge> result = tools.parseInput(edgesInfo);
        int edgeSize = result.size();
        assertEquals(expectSize, edgeSize);
    }
    @ParameterizedTest
    @ValueSource(strings = {"AB3, DE5, 32","ab3","ABC"})
    void parseInputExpectException(String edgesInfo) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () ->{
                    tools.parseInput(edgesInfo);
                }
        );
    }


    @ParameterizedTest
    @CsvSource(value = {"AD5, D"})
    void parseNodes(String edgesInfo, Character expectDestination) {
        Edge edge = tools.parseEdge(edgesInfo);
        Character destination = edge.to;
        assertEquals(expectDestination, destination);
    }

    @ParameterizedTest
    @ValueSource(strings = {"","ab2"})
    void parseNodesExpectException(String edgesInfo) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () ->{
                    tools.parseEdge(edgesInfo);
                }
        );
    }
}