package com.instana.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon
 * Description: presenting an Adjacency List,
 * which is referenced by one of Digraph.node.
 * using map instead of list, it would be easier to ensure contains a special child node.
 */
public class AdjacencyList {
    public Character name;
    /**
     * children means all destination nodes from the node which named as this.name
     * Character: presents the destination node
     * Integer: presents the distance between destination node and this.name node
     */
    public Map<Character, Integer> children;

    public AdjacencyList(Character name) {
        this.name = name;
        children = new HashMap<>();
    }
}
