package com.instana.graph;

/**
 * @author Simon
 * Description: a light version of Node, without children collection.
 * the class is used in dijkstra algorithm, in priority Queue, or temporary data.
 */
public class Edge {
    public Character from;
    public Character to;
    public int distance;

    public Edge() {
        distance = Integer.MAX_VALUE;
    }

    public Edge(Character from, Character to, int distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return String.format("edge: %c%c%d", from, to, distance);
    }
}
