package advent_of_code._2023.day8;

import advent_of_code.Puzzle;
import advent_of_code._2023._2023_Puzzle;
import fun.tuple.Pair;
import types.FileParsers;
import types.MathFun;

import java.util.*;
import java.util.regex.Pattern;

public final class HauntedWasteland implements _2023_Puzzle {


    private static List<String> getInstructions(String line) {
        return Arrays.stream(line.split("")).toList();
    }

    private static List<Node> getNodes(List<String> lines) {
        var regex = Pattern.compile("^(?<name>[A-Z]{3}) += +\\((?<left>[A-Z]{3}), (?<right>[A-Z]{3})\\)$");
        var nodes = new ArrayList<Node>();
        for (var line : lines) {
            var matcher = regex.matcher(line);
            if (matcher.matches()) {
                var node = new Node(matcher.group("name"),
                                    matcher.group("left"),
                                    matcher.group("right"));
                nodes.add(node);
            }
        }
        return nodes;
    }

    private static Map<String, Pair<Node, Node>> getNetwork(List<Node> nodes) {
        var network = new HashMap<String, Pair<Node, Node>>();
        for (var node : nodes) {
            network.put(node.name,
                        Pair.of(nodes.stream().filter(n -> n.name.equals(node.left)).findFirst().get(),
                                nodes.stream().filter(n -> n.name.equals(node.right)).findFirst().get()
                               )
                       );
        }
        return network;
    }

    @Override
    public Object solveFirst() throws Exception {
        var groups = FileParsers.toGroupsOfLines(getInputPath());
        var instructions = getInstructions(groups.get(0).get(0));
        var nodes = getNodes(groups.get(1));
        var network = getNetwork(nodes);


        var start = nodes.stream()
                         .filter(n -> n.name.equals("AAA"))
                         .findFirst()
                         .get();
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
        return steps;

    }

    @Override
    public Object solveSecond() throws Exception {
        var groups = FileParsers.toGroupsOfLines(getInputPath());
        var instructions = getInstructions(groups.get(0).get(0));
        var nodes = getNodes(groups.get(1));
        var network = getNetwork(nodes);

        var starts = nodes.stream()
                          .filter(n -> n.name.endsWith("A"))
                          .toList();

        Map<Node, Long> cycles = new HashMap<>();
        var steps = 0L;
        while (cycles.size() != starts.size()) {
            for (var in : instructions) {
                steps += 1;
                var ends = new ArrayList<Node>();
                for (var start : starts) {
                    var xs = network.get(start.name);
                    var end = in.equals("L") ? xs.first() : xs.second();
                    ends.add(end);
                    if (end.name.endsWith("Z"))
                        cycles.put(end, steps);
                }
                starts = ends;
            }
        }

        return MathFun.lcm(cycles.values());
    }

    @Override
    public String name() {
        return "Haunted Wasteland";
    }

    @Override
    public int day() {
        return 8;
    }


    @Override
    public String outputUnitsPart1() {
        return "steps";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }

    record Node(String name, String left, String right) {
    }
}
