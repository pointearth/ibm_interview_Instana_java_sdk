package com.instana.graph;

/**
 * @author Simon
 * Description: a light version of Node, without children collection.
 * the class is used in dijkstra algorithm, in priority Queue, or temporary data.
 */
public class Edge {
    public Character previous;
    public Character destination;
    public int weight;

    public Edge() {
        weight = Integer.MAX_VALUE;
    }

    public Edge(Character previous, Character destination, int weight) {
        this.previous = previous;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%c%c%d", previous, destination, weight);
    }
}
