package advent_of_code._2023.day20;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;
import types.MathFun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PulsePropagation implements _2023_Puzzle {


    @Override
    public Object solveFirst() throws Exception {


        var regex = "(?<source>.+) -> (?<target>.+)";

        var nodes = initNodes(regex);

        List<Pulse> pulses = new ArrayList<>();
        var low = 0L;
        var high = 0L;

        for (int i = 0; i < 1000; i++) {
            pulses.add(new Pulse(null, Broadcaster.INSTANCE, 0));


            while (!pulses.isEmpty()) {
                var pulse = pulses.remove(0);
                if (pulse.signal == 1) high += 1;
                else low += 1;
                if (pulse.source == null)
                    nodes.get(Broadcaster.INSTANCE)
                         .forEach(mod -> pulses.add(new Pulse(Broadcaster.INSTANCE,
                                                              mod,
                                                              pulse.signal)));
                else {
                    switch (pulse.target) {
                        case FlipFlop ff -> {
                            if (pulse.signal == 0) {
                                ff.status = !ff.status;
                                nodes.get(ff)
                                     .forEach(mod -> pulses.add(new Pulse(ff, mod, ff.status ? 1 : 0)));
                            }
                        }
                        case Conjunction con -> {
                            con.memory.put(pulse.source, pulse.signal == 1);
                            if (con.memory.values()
                                          .stream()
                                          .allMatch(it -> it))
                                nodes.get(con).forEach(mod -> pulses.add(new Pulse(con, mod, 0)));
                            else nodes.get(con)
                                      .forEach(mod -> pulses.add(new Pulse(con, mod, 1)));
                        }

                        case Unknown unknown -> {
                        }

                        default -> throw new IllegalStateException("Unexpected value: " + pulse.target);
                    }
                }

            }
        }


        return high * low;


    }

    private Map<Node, List<Node>> initNodes(String regex) {
        Map<Node, List<Node>> nodes = new HashMap<>();

        FileParsers.toListOfLineMatchers(getInputPath(), regex)
                   .forEach(m -> {
                       Node source = Node.parse(m.group("source").trim());
                       nodes.put(source, new ArrayList<>());
                   });

        FileParsers.toListOfLineMatchers(getInputPath(), regex)
                   .forEach(m -> {
                       Node source = Node.parse(m.group("source").trim());
                       for (String node : m.group("target").trim().split(",")) {
                           nodes.get(source)
                                .add(nodes.keySet().stream().filter(it -> it.name()
                                                                            .equals(node.trim()))
                                          .findFirst()
                                          .orElse(Unknown.INSTANCE));
                       }

                   });

        var connections = nodes.entrySet()
                               .stream().filter(e -> e.getKey() instanceof Conjunction)
                               .map(nodeListEntry -> ((Conjunction) nodeListEntry.getKey()))
                               .toList();

        for (Conjunction connection : connections) {
            for (Map.Entry<Node, List<Node>> e : nodes.entrySet()) {
                if (e.getValue().contains(connection)) connection.memory.put(e.getKey(), false);
            }

        }

        return nodes;
    }

    @Override
    public Object solveSecond() throws Exception {
        var regex = "(?<source>.+) -> (?<target>.+)";

        var nodes = initNodes(regex);

        List<Pulse> pulses = new ArrayList<>();
        List<Long> periods = new ArrayList<>();

        for (long i = 0; i < 30000; i++) {
            pulses.add(new Pulse(null, Broadcaster.INSTANCE, 0));

            while (!pulses.isEmpty()) {
                var pulse = pulses.remove(0);
                if (pulse.equals(new Pulse(new Conjunction("xc"), new Conjunction("tj"), 1)))
                    periods.add(i);
                if (pulse.equals(new Pulse(new Conjunction("sk"), new Conjunction("tj"), 1)))
                    periods.add(i);
                if (pulse.equals(new Pulse(new Conjunction("vt"), new Conjunction("tj"), 1)))
                    periods.add(i);
                if (pulse.equals(new Pulse(new Conjunction("kk"), new Conjunction("tj"), 1)))
                    periods.add(i);


                if (pulse.source == null)
                    nodes.get(Broadcaster.INSTANCE).forEach(mod -> pulses.add(new Pulse(Broadcaster.INSTANCE,
                                                                                        mod,
                                                                                        pulse.signal)));
                else {
                    switch (pulse.target) {
                        case FlipFlop ff -> {
                            if (pulse.signal == 0) {
                                ff.status = !ff.status;
                                nodes.get(ff).forEach(mod -> pulses.add(new Pulse(ff, mod, ff.status ? 1 : 0)));
                            }
                        }
                        case Conjunction con -> {
                            con.memory.put(pulse.source, pulse.signal == 1);
                            if (con.memory.values().stream().allMatch(it -> it))
                                nodes.get(con).forEach(mod -> pulses.add(new Pulse(con, mod, 0)));
                            else nodes.get(con).forEach(mod -> pulses.add(new Pulse(con, mod, 1)));
                        }

                        case Unknown unknown -> {
                        }


                        default -> throw new IllegalStateException("Unexpected value: " + pulse.target);
                    }
                }

            }
        }

        System.out.println(periods);

        return MathFun.lcm(periods.get(4) - periods.get(0),
                           periods.get(5) - periods.get(1),
                           periods.get(6) - periods.get(2),
                           periods.get(7) - periods.get(3));

    }

    @Override
    public String name() {
        return "Pulse Propagation";
    }

    @Override
    public int day() {
        return 20;
    }

    @Override
    public String outputUnitsPart1() {
        return "low pulses x high pulses";
    }

    @Override
    public String outputUnitsPart2() {
        return "fewest number of button presses";
    }

    record Pulse(Node source, Node target, int signal) {
    }

}
