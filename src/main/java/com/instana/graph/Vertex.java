package com.instana.graph;

/**
 * @author Simon
 * Description: From a source node, presents the distance and path from source node to destination node, acrossing previous node B as last step.
 * i.e: Based on A, vertex BC9 menas start from node A, to node C, accrossing node B as the last step, that means: path is A....->B->C, total instance is 9 ( 5+4)
 * the class is used in dijkstra algorithm, in priority Queue, or temporary data.
 */
public class Vertex {
    public Character previous;
    public Character destination;
    public int distance;
    public boolean visited = false;

    public Vertex(Character previous, Character destination, int distance) {
        this.previous = previous;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return String.format("vertex:%c%c%d", previous, destination, distance);
    }
}
