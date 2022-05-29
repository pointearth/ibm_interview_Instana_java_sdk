package com.instana;


import com.instana.exception.InputFormatException;
import com.instana.exception.NotFoundException;
import com.instana.exception.TraceNotFoundException;

/**
 * Main function
 *
 * @author Simon
 */
public class InstanaApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new InputFormatException("Please type the data file name");
        }
        int argLength = args.length;
        String[] paths = new String[]{"A-B-C", "A-D", "A-D-C", "A-E-B-C-D", "A-E-D"};
        TraceService traceService = new TraceService();
        traceService.loadData(args[argLength - 1]);
        for (int i = 0; i < paths.length; i++) {
            try {
                Integer latency = traceService.getLatency(paths[i]);
                System.out.printf("%d. %d\n", i + 1, latency);
            } catch (TraceNotFoundException ex) {
                System.out.printf("%d. %s\n", i + 1, "NO SUCH TRACE");
            } catch (NotFoundException notFoundException) {
                System.out.printf("%s\n", notFoundException);
            }
        }
        try {
            int traceNum = traceService.getTraceNumInHops('C', 'C', 3);
            System.out.printf("6. %d\n", traceNum);
        } catch (Exception ex) {
            System.out.printf("%s\n", ex);
        }
        try {
            int traceNum = traceService.getTraceNumEqualHops('A', 'C', 4);
            System.out.printf("7. %d\n", traceNum);
        } catch (Exception ex) {
            System.out.printf("%s\n", ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('A', 'C');
            System.out.printf("8. %d\n", traceNum);
        } catch (Exception ex) {
            System.out.printf("%s\n", ex);
        }
        try {
            int traceNum = traceService.getLenShortestTrace('B', 'B');
            System.out.printf("9. %d\n", traceNum);
        } catch (Exception ex) {
            System.out.printf("%s\n", ex);
        }
    }
}