package com.instana.graph;

import com.instana.exception.GraphException;
import com.instana.exception.InputFormatException;
import com.instana.exception.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGraph {

    void loadData(List<Edge> edgeList) throws NullPointerException;
    void initData();
    int getNumOfVertices();
    int getNumOfEdges();

    void addEdge(Edge edge);
    void removeEdge(Edge edge);
    Optional<Integer> getInstance(Character source, Character destination) throws NotFoundException;
    int getTraceNumberInMaxHops(Character starNodeName, Character endNodeName, int maxHops);
    int getTraceNumberExactlyHops(Character starNodeName, Character endNodeName, int exactlyHops);
    Optional<Map.Entry<Integer,List<Character>>> getShortestPath(Character source, Character destination ) throws NotFoundException, GraphException;
}
