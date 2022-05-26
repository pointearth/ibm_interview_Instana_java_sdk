package com.instana.graph;

/**
 * @author Simon
 * Description: Based on source node, presents the distance and path from source node to destination node, acrossing previous node B as last step.
 * i.e: Based on A, vertex BC9 menas start from node A, to node C, accrossing node B as the last step, that means: path is A....->B->C, total instance is 9 ( 5+4)
 * the class is used in dijkstra algorithm, in priority Queue, or temporary data.
 */
public class Vertex {
    public Character previous;
    public Character destination;
    public int instance;
    public boolean visited = false;

    public Vertex() {
        instance = Integer.MAX_VALUE;
    }

    public Vertex(Character previous, Character destination, int instance) {
        this.previous = previous;
        this.destination = destination;
        this.instance = instance;
    }

    @Override
    public String toString() {
        return String.format("vertex:%c%c%d", previous, destination, instance);
    }
}
