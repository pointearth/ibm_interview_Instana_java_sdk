package com.instana;


import com.instana.exception.InputFormatException;
import com.instana.exception.NotFoundException;
import com.instana.exception.TraceNotFoundException;
import com.instana.graph.Vertex;

import java.rmi.NotBoundException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class InstanaApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new InputFormatException("Please type the data file name");
        }
        String[] paths = new String[]{"A-B-C", "A-D","A-D-C","A-E-B-C-D","A-E-D"};
        TraceService traceService = new TraceService();
        traceService.loadData(args[0]);
        for (int i = 0; i < paths.length; i++) {
            try {
                Integer latency = traceService.getAverageLatency(paths[i]);
                System.out.println(latency);
            } catch (TraceNotFoundException ex) {
                System.out.println("NO SUCH TRACE");
            } catch (NotFoundException notFoundException) {
                System.out.println(notFoundException);
            }
        }
        try {
            int traceNum = traceService.getTraceNumInHops('C', 'C', 3);
            System.out.println(traceNum);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getTraceNumEqualHops('A', 'C', 4);
            System.out.println(traceNum);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('A', 'C');
            System.out.println(traceNum);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('B', 'B');
            System.out.println(traceNum);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}