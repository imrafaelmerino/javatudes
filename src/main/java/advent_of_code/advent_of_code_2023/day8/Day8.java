package advent_of_code.advent_of_code_2023.day8;

import fun.tuple.Pair;
import types.FileParsers;
import types.MathFun;

import java.util.*;
import java.util.regex.Pattern;

public class Day8 {


    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day8/input.txt";

        var groups = FileParsers.toGroupsOfLines(input);

        List<String> instructions = Arrays.stream(groups.get(0).get(0).split("")).toList();

        var regex = Pattern.compile("^(?<name>[A-Z]{3}) += +\\((?<left>[A-Z]{3}), (?<right>[A-Z]{3})\\)$");
        var network = new HashMap<String, Pair<Node, Node>>();
        var nodes = new ArrayList<Node>();
        for (var line : groups.get(1)) {
            var matcher = regex.matcher(line);
            if (matcher.matches()) {
                var node = new Node(matcher.group("name"),
                                    matcher.group("left"),
                                    matcher.group("right"));
                nodes.add(node);
            }
        }
        for (var node : nodes) {
            network.put(node.name,
                        Pair.of(nodes.stream().filter(n -> n.name.equals(node.left)).findFirst().get(),
                                nodes.stream().filter(n -> n.name.equals(node.right)).findFirst().get()
                               )
                       );
        }

        System.out.println(part1(network,
                                 nodes,
                                 instructions)
                          );
        System.out.println(part2(network,
                                 nodes,
                                 instructions)
                          );


    }

    private static String part1(Map<String, Pair<Node, Node>> network,
                                List<Node> nodes,
                                List<String> instructions
                               ) {
        var start = nodes.stream().filter(n -> n.name.equals("AAA")).findFirst().get();
        var steps = 0;
        var end = start;
        while (!end.name.equals("ZZZ")) {
            for (var in : instructions) {
                var xs = network.get(start.name);
                end = in.equals("L") ? xs.first() : xs.second();
                steps += 1;
                start = end;
            }
        }
        return steps + "";
    }

    /**
     * input is cooked, no way to brute force this.
     */
    private static String part2(Map<String, Pair<Node, Node>> network,
                                List<Node> nodes,
                                List<String> instructions
                               ) {

        var starts = nodes.stream()
                          .filter(n -> n.name.endsWith("A"))
                          .toList();

        Map<Node, Long> cycles = new HashMap<>();
        var steps = 0L;
        while (cycles.size() != starts.size()) {
            for (var in : instructions) {
                steps += 1;
                List<Node> ends = new ArrayList<>();
                for (Node start : starts) {
                    var xs = network.get(start.name);
                    var end = in.equals("L") ? xs.first() : xs.second();
                    ends.add(end);
                    if (end.name.endsWith("Z"))
                        cycles.put(end, steps);
                }
                starts = ends;
            }
        }

        return MathFun.lcm(cycles.values()).toString();

    }

    record Node(String name, String left, String right) {
    }
}
