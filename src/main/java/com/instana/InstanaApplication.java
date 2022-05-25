package com.instana;


public class InstanaApplication {
    public static void main(String[] args)  {
        TraceService traceService = new TraceService();
        try {
            traceService.loadData("data.txt");
            traceService.getAverageLatency("A-B-C");
            traceService.getAverageLatency("A-D");
            traceService.getAverageLatency("A-D-C");
            traceService.getAverageLatency("A-E-B-C-D");
            traceService.getAverageLatency("A-E-D");

            traceService.getLenShortestTrace('A', 'C');
            traceService.getLenShortestTrace('B', 'B');
        } catch (Exception ex) {
            System.out.println(ex);
        }

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