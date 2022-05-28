package com.instana;


import com.instana.exception.InputFormatException;
import com.instana.exception.NotFoundException;
import com.instana.exception.TraceNotFoundException;

/** Main function
 * @author Simon
 */
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
                Integer latency = traceService.getLatency(paths[i]);
                System.out.println(String.format("%d. %d",i+1,latency));
            } catch (TraceNotFoundException ex) {
                System.out.println(String.format("%d. %s",i+1,"NO SUCH TRACE"));
            } catch (NotFoundException notFoundException) {
                System.out.println(notFoundException);
            }
        }
        try {
            int traceNum = traceService.getTraceNumInHops('C', 'C', 3);
            System.out.println(String.format("6. %d",traceNum));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getTraceNumEqualHops('A', 'C', 4);
            System.out.println(String.format("7. %d",traceNum));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('A', 'C');
            System.out.println(String.format("8. %d",traceNum));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('B', 'B');
            System.out.println(String.format("9. %d",traceNum));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}