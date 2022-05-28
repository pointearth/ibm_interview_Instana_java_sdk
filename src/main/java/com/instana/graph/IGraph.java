package com.instana.graph;

import com.instana.exception.GraphException;
import com.instana.exception.NotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface of Graph
 * @author Simon
 */
public interface IGraph {

    /**
     * Load data to create the graph
     * @param edgeList -  Edges to be loaded into the graph
     * @throws NullPointerException
     */
    void loadData(List<Edge> edgeList) throws NullPointerException;

    /**
     * Clean all the data in the Graph
     */
    void cleanData();

    /**
     * Get amount of the source nodes
     * @return the amount
     * Description: if a node is not exist as source node, it is not in the amount
     * i.e: in graph "A-B-C", return is 2, because 'C' is only a destination node, not source node.
     */
    int getAmountOfSourceNodes();

    /**
     * Get amount of the edges in the graph
     * @return the amount of edges
     */
    int getAmountOfEdges();

    /**
     * Add an edge into the graph
     * @param edge - the edge to be added
     */
    void addEdge(Edge edge);

    /**
     * Remove an edge from the graph
     * @param edge - the edge to be removed
     */
    void removeEdge(Edge edge);

    /**
     * Get instance between source node and destination node
     * @param source - source node name
     * @param destination - destination node name
     * @return - distance between source and destination
     *      Optional.empty() - there is no path between source and destination
     * @throws NotFoundException - source doesn't exit in the graph
     */
    Optional<Integer> getDirectInstance(Character source, Character destination) throws NotFoundException;

    /**
     * Get number of paths between source node and destination node, within special edges
     * @param source - source node name
     * @param destination - destination node name
     * @param maxEdgeNum - limitation number of the edges to be used in a path
     * @return - number of paths between source node and destination node
     * @throws NotFoundException - source node doesn't exist in the graph
     * @throws GraphException - internal error in the Graph
     */
    int getPathNumInEdgeNum(Character source, Character destination, int maxEdgeNum) throws NotFoundException, GraphException;

    /**
     * Get number of paths between source node and destination node, each of paths composed in exactly number of edges
     * @param source - source node name
     * @param destination - destination node name
     * @param exactlyEdges - number of the edges to be used in a path
     * @return - number of paths between source node and destination node
     * @throws NotFoundException - source node doesn't exist in the graph
     * @throws GraphException - internal error in the Graph
     */
    int getPathNumEqualEdgeNum(Character source, Character destination, int exactlyEdges) throws NotFoundException, GraphException;

    /**
     * Get the shortest path between source and destination
     * @param source - source node name
     * @param destination - destination node name
     * @return the shortest path
     * 1. Optional.empty() : there is no path between source node and destination node
     * 2. not empty: Map.Entry<Integer, List<Character>> - description the shortest path, including:
     *      @Integer - the instance of the path
     *      @List<Character> - serious of nodes in the Path. i.e: ['A', 'B' 'C'] means the shortest path is composed of nodes 'A', 'B' 'C'.
     * @throws NotFoundException - source node doesn't exist in the graph
     * @throws GraphException - internal error in the Graph
     */
    Optional<Map.Entry<Integer, List<Character>>> getShortestPath(Character source, Character destination) throws NotFoundException, GraphException;
}
