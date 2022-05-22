package instana;

import java.util.Comparator;
import java.util.PriorityQueue;

public class InstanaApplication {
    public static void main(String[] args) {

        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.weight));
        pq.add(new Vertex('A', 'B', 5));
        pq.add(new Vertex('B', 'C', 4));
        pq.add(new Vertex('C', 'D', 9));
        Vertex temp = new Vertex('C', 'D', 2);
        pq.offer(temp);
        pq.offer(new Vertex('C', 'D', 1));

        while (!pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }
}