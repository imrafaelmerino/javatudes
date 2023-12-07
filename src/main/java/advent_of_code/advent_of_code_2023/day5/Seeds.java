package advent_of_code.advent_of_code_2023.day5;

import fun.tuple.Pair;
import types.FileParsers;
import types.ListFun;
import types.Range;
import types.StrFun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Seeds {

    public static void main(String[] args) {

        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day5/input.txt";

        List<List<String>> groupsOfLines = FileParsers.toGroupsOfLines(path);
        var seeds = StrFun.toListOfLong(groupsOfLines.get(0)
                                                     .get(0)
                                                     .split(":")[1]
                                       );

        var stages = IntStream.rangeClosed(1, 7)
                              .mapToObj(n -> getBlockTxs(groupsOfLines, n))
                              .toList();


        var sol_1 = seeds.stream()
                         .map(seed -> txAllStages(seed, stages))
                         .min(Long::compareTo);

        System.out.println(sol_1.get());


        List<Range> sources = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++)
            if (i % 2 != 0) sources.add(new Range(seeds.get(i - 1),
                                                  seeds.get(i - 1) + seeds.get(i) - 1));


        List<Range> xs = txAllStages(sources, stages);
        System.out.println(xs.stream()
                             .map(Range::min)
                             .sorted(Long::compareTo)
                             .findFirst()
                             .get());


    }

    private static long txAllStages(long input, List<Map<Range, Range>> txss) {
        if (txss.isEmpty()) return input;
        return txAllStages(txStage(input,
                                   ListFun.head(txss)),
                           ListFun.tail(txss));
    }

    private static Map<Range, Range> getBlockTxs(List<List<String>> groupsOfLines, int index) {
        return ListFun.tail(groupsOfLines.get(index))
                      .stream()
                      .map(StrFun::toListOfLong)
                      .collect(Collectors.toMap(ns -> new Range(ns.get(1),
                                                                ns.get(1) + ns.get(2) - 1),
                                                ns -> new Range(ns.get(0),
                                                                ns.get(0) + ns.get(2) - 1)
                                               )
                              );

    }

    private static long txStage(long input, Map<Range, Range> ranges) {
        return ranges.entrySet().stream()
                     .filter(e -> e.getKey().contain(input))
                     .map(e -> input + e.getValue().min() - e.getKey().min())
                     .findFirst()
                     .orElse(input);
    }

    private static List<Range> txAllStages(List<Range> inputs, List<Map<Range, Range>> stages) {
        if (stages.isEmpty()) return inputs;
        var stage = ListFun.head(stages);
        return txAllStages(txStage(inputs,
                                   new ArrayList<>(),
                                   stage
                                  ),
                           ListFun.tail(stages)
                          );
    }

    private static List<Range> txStage(List<Range> inputs,
                                       List<Range> outputs,
                                       Map<Range, Range> stage
                                      ) {
        if (inputs.isEmpty()) return outputs;
        var input = inputs.remove(0);
        var output = stage.entrySet()
                          .stream()
                          .map(e -> {
                                   var intersection = input.intersection(e.getKey());
                                   return Pair.of(e.getValue().min() - e.getKey().min(),
                                                  intersection
                                                 );
                               }
                              )
                          .filter(pair -> pair.second() != null)
                          .findFirst()
                          .orElse(Pair.of(null, input));


        return output.first() == null ?
                txStage(inputs,
                        ListFun.append(outputs, input),
                        stage) :
                txStage(ListFun.appendAll(inputs,
                                          input.difference(output.second())),
                        ListFun.append(outputs, output.second()
                                                      .translate(output.first())),
                        stage);

    }


}
