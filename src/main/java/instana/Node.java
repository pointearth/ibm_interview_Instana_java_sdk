package instana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    public String name;
    public Map<String, Node> children;
    public int weight = 0;
    public Node(String name, int weight) {
        this.name = name;
        this.weight = weight;
        children = new HashMap<>();
    }
}
