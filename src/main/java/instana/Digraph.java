package instana;

import java.util.*;

public class Digraph {
    private static final String INPUT_SEPARATOR = ",";
    private static final String PATH_SEPARATOR = "-";
    private Map<String, Node> nodes;

    public Digraph(String edgesInfo) {

    }

    /***
     *
     * @param pathInfo: nodes in the trace, such as A-B-C
     * @return
     *  -2: pathInfo' format error
     *  -1: no such trace
     *  >=0: average Latencies
     */
    public int getLatency(String pathInfo) {
        return 0;
    }
}
