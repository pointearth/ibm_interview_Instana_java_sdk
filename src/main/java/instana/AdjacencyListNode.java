package instana;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon
 * Description: presenting an Adjacency List,
 * which is referenced by one of Digraph.node.
 * using map instead of list, it would be easier to ensure contains a special child node.
 */
public class AdjacencyListNode {
    public Character name;
    public Map<Character, Integer> children;
    public int weight = 0;

    public AdjacencyListNode(Character name, int weight) {
        this.name = name;
        this.weight = weight;
        children = new HashMap<>();
    }
}
