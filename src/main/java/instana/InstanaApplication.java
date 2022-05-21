package instana;

public class InstanaApplication {
    public static void main(String[] args) {
        Digraph digraph = new Digraph(args[1]);
        digraph.getLatency("A-B-C");
    }
}