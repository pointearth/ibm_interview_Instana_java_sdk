package instana;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonToolsTest {
    @ParameterizedTest
    @CsvSource({"A-D-C, 3"
            , "A-D-C-D-E-B-A, 7"
            , " A- D, 2", ", -1"
            , " , -1"
            , " #A- D, -1"})
    void parsePath(String pathInfo, int expectNodeSize) {
        Character[] result = CommonTools.parsePath(pathInfo);
        int nodeSize;
        if (null == result) {
            nodeSize = -1;
        } else {
            nodeSize = result.length;
        }
        assertEquals(expectNodeSize, nodeSize);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';'
            , value = {"AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7; 9 "
            , "AB334; 1"
            , "AB3, DE5, 32; -1"
            , "ab3; -1"
            , "ABC; -1"})
    void parseInput(String edgesInfo, int expectSize) {
        List<Vertex> result = CommonTools.parseInput(edgesInfo);
        int vertexSize;
        if (null == result) {
            vertexSize = -1;
        } else {
            vertexSize = result.size();
        }
        assertEquals(expectSize, vertexSize);
    }

    @ParameterizedTest
    @CsvSource(nullValues = {"null"}
            , value = {"AD5, D", ", null", "ab2, null"})
    void parseNodes(String edgesInfo, Character expectDestination) {
        Vertex vertex = CommonTools.parseNodes(edgesInfo);
        Character destination;
        if (null == vertex) {
            destination = null;
        } else {
            destination = vertex.destination;
        }
        assertEquals(expectDestination, destination);
    }
}