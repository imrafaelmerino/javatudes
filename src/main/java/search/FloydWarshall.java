package search;

import fun.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloydWarshall {

    public static int get(Map<?, Integer> map, Object key) {
        return map.containsKey(key) ?
                map.get(key) :
                Integer.valueOf(Integer.MAX_VALUE);
    }

    public static Map<Pair<String, String>, Integer> search(Map<String, ? extends Iterable<String>> graph) {

        Map<Pair<String, String>, Integer> D = new HashMap<>();
        List<String> keys = graph.keySet()
                                 .stream()
                                 .toList();
        for (String i : keys) {
            D.put(Pair.of(i, i), 0);
            for (var j : graph.get(i)) D.put(Pair.of(i, j), 1);
        }

        for (var k : keys) {
            // Pick all vertices as source one by one
            for (var i : keys) {
                // Pick all vertices as destination for the
                // above picked source
                for (var j : keys) {
                    // If vertex k is on the shortest path
                    // from i to j, then update the value of
                    // dist[i][j]
                    if (!i.equals(j)) {
                        int ij = get(D, Pair.of(i, j));
                        int ik = get(D, Pair.of(i, k));
                        int kj = get(D, Pair.of(k, j));
                        int s = (ik == Integer.MAX_VALUE || kj == Integer.MAX_VALUE)
                                ? Integer.MAX_VALUE :
                                ik + kj;
                        if (s < ij) D.put(Pair.of(i, j),
                                          s
                                         );
                    }
                }
            }
        }
        return D;
    }


    public static void main(String[] args) {
        var nodes = new HashMap<String, List<String>>();
        nodes.put("A", List.of("B", "C"));
        nodes.put("B", List.of("A", "D"));
        nodes.put("C", List.of("A"));

        var a = search(nodes);

        System.out.println(a);

        /**
         *
         *
         *       A
         *   B        C
         * D
         */
    }
}
