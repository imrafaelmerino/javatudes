package advent_of_code._2023.day5;

import advent_of_code._2023._2023_Puzzle;
import fun.tuple.Pair;
import types.FileParsers;
import types.ListFun;
import types.LongRange;
import types.StrFun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Seeds implements _2023_Puzzle {


    private static long txAllStages(long input, List<Map<LongRange, LongRange>> txss) {
        if (txss.isEmpty()) return input;
        return txAllStages(txStage(input,
                                   ListFun.head(txss)),
                           ListFun.tail(txss));
    }

    private static Map<LongRange, LongRange> getBlockTxs(List<List<String>> groupsOfLines, int index) {
        return ListFun.tail(groupsOfLines.get(index))
                      .stream()
                      .map(StrFun::parseSpacedLongs)
                      .collect(Collectors.toMap(ns -> new LongRange(ns.get(1),
                                                                    ns.get(1) + ns.get(2) - 1),
                                                ns -> new LongRange(ns.get(0),
                                                                    ns.get(0) + ns.get(2) - 1)
                                               )
                              );

    }

    private static long txStage(long input, Map<LongRange, LongRange> ranges) {
        return ranges.entrySet().stream()
                     .filter(e -> e.getKey().contain(input))
                     .map(e -> input + e.getValue().min() - e.getKey().min())
                     .findFirst()
                     .orElse(input);
    }

    private static List<LongRange> txAllStages(List<LongRange> inputs, List<Map<LongRange, LongRange>> stages) {
        if (stages.isEmpty()) return inputs;
        var stage = ListFun.head(stages);
        return txAllStages(txStage(inputs,
                                   new ArrayList<>(),
                                   stage
                                  ),
                           ListFun.tail(stages)
                          );
    }

    private static List<LongRange> txStage(List<LongRange> inputs,
                                           List<LongRange> outputs,
                                           Map<LongRange, LongRange> stage
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


    @Override
    public Object solveFirst() {

        List<List<String>> groupsOfLines = FileParsers.toGroupsOfLines(getInputPath());
        var seeds = StrFun.parseSpacedLongs(groupsOfLines.get(0)
                                                         .get(0)
                                                         .split(":")[1]
                                           );

        var stages = IntStream.rangeClosed(1, 7)
                              .mapToObj(n -> getBlockTxs(groupsOfLines, n))
                              .toList();


        return seeds.stream()
                    .map(seed -> txAllStages(seed, stages))
                    .min(Long::compareTo)
                    .orElse(0L);

    }

    @Override
    public Object solveSecond() {
        List<List<String>> groupsOfLines = FileParsers.toGroupsOfLines(getInputPath());
        var seeds = StrFun.parseSpacedLongs(groupsOfLines.get(0)
                                                         .get(0)
                                                         .split(":")[1]
                                           );

        var stages = IntStream.rangeClosed(1, 7)
                              .mapToObj(n -> getBlockTxs(groupsOfLines, n))
                              .toList();


        List<LongRange> sources = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++)
            if (i % 2 != 0) sources.add(new LongRange(seeds.get(i - 1),
                                                      seeds.get(i - 1) + seeds.get(i) - 1));


        List<LongRange> xs = txAllStages(sources, stages);
        return xs.stream()
                 .map(LongRange::min)
                 .min(Long::compareTo)
                 .orElse(0L);

    }

    @Override
    public String name() {
        return "If You Give A Seed A Fertilizer";
    }

    @Override
    public int day() {
        return 5;
    }

    @Override
    public String outputUnitsPart1() {
        return "lowest location number";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }
}
