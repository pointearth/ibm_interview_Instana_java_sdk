package com.instana.graph;

/**
 * @author Simon
 * Description: a light version of Node, without children collection.
 * the class is used in dijkstra algorithm, in priority Queue, or temporary data.
 */
public class Vertex {
    public Character source;
    public Character destination;
    public int weight;

    public Vertex() {
        weight = Integer.MAX_VALUE;
    }

    public Vertex(Character previous, Character destination, int weight) {
        this.source = previous;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("vertex:%c%c%d", source, destination, weight);
    }
}
