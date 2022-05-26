package com.instana;


import com.instana.graph.Vertex;

import java.util.Comparator;
import java.util.PriorityQueue;

public class InstanaApplication {
    public static void main(String[] args)  {
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.instance));
        Vertex v = new Vertex('A','B',5);
        queue.add(v);
        v.instance = 2;
        System.out.println(queue.contains(v));
//        TraceService traceService = new TraceService();
//        try {
//            traceService.loadData("data.txt");
//            traceService.getAverageLatency("A-B-C");
//            traceService.getAverageLatency("A-D");
//            traceService.getAverageLatency("A-D-C");
//            traceService.getAverageLatency("A-E-B-C-D");
//            traceService.getAverageLatency("A-E-D");
//
//            traceService.getLenShortestTrace('A', 'C');
//            traceService.getLenShortestTrace('B', 'B');
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }

    }
}


//A-B-C
//        A-D
//        A-D-C
//        A-E-B-C-D
//        A-E-D
//        C-C
//        A-C
//        A-C
//        B-B