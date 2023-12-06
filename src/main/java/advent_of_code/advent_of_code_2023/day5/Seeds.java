package advent_of_code.advent_of_code_2023.day5;

import fun.tuple.Pair;
import types.FileParsers;
import types.ListFun;
import types.Range;
import types.StrFun;

import java.util.*;
import java.util.stream.Collectors;

public class Seeds {

    public static void main(String[] args) {

        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day5/input_test.txt";

        List<List<String>> groupsOfLines = FileParsers.toGroupsOfLines(path);
        var seeds = StrFun.toListOfLong(groupsOfLines.get(0)
                                                     .get(0)
                                                     .split(":")[1]);
        var seedToSoil = getMapOfRanges(groupsOfLines, 1);
        var soilToFertilizer = getMapOfRanges(groupsOfLines, 2);
        var fertilizerToWater = getMapOfRanges(groupsOfLines, 3);
        var waterToLight = getMapOfRanges(groupsOfLines, 4);
        var lightToTemperature = getMapOfRanges(groupsOfLines, 5);
        var temperatureToHumidity = getMapOfRanges(groupsOfLines, 6);
        var humidityToLocation = getMapOfRanges(groupsOfLines, 7);
        var txss = List.of(seedToSoil,
                           soilToFertilizer,
                           fertilizerToWater,
                           waterToLight,
                           lightToTemperature,
                           temperatureToHumidity,
                           humidityToLocation);


        var sol_1 = seeds.stream()
                         .map(seed -> goThroughRanges(seed, txss))
                         .min(Long::compareTo);

        System.out.println(sol_1.get());


        List<Range> sources = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++)
            if (i % 2 != 0) sources.add(new Range(seeds.get(i - 1),
                                                  seeds.get(i - 1) + seeds.get(i) - 1));

        List<Range> xs = tx(sources, txss);
        System.out.println(xs);
        System.out.println(xs.stream()
                             .map(Range::min)
                             .sorted(Long::compareTo)
                             .findFirst().get());


    }

    private static long goThroughRanges(long source, List<Map<Range, Range>> ranges) {
        if (ranges.isEmpty()) return source;
        Map<Range, Range> head = ListFun.head(ranges);
        return goThroughRanges(getDestination(head,
                                              source),
                               ListFun.tail(ranges));
    }

    private static Map<Range, Range> getMapOfRanges(List<List<String>> groupsOfLines, int index) {
        return ListFun.tail(groupsOfLines.get(index))
                      .stream()
                      .map(StrFun::toListOfLong)
                      .collect(Collectors.toMap(ns -> new Range(ns.get(1),
                                                                ns.get(1) + ns.get(2)),
                                                ns -> new Range(ns.get(0),
                                                                ns.get(0) + ns.get(2)),
                                                (existing, replacement) -> existing,
                                                LinkedHashMap::new
                                               )
                              );

    }

    //intersection

    private static long getDestination(Map<Range, Range> ranges, long source) {
        return ranges.entrySet().stream()
                     .filter(e -> e.getKey().contain(source))
                     .map(e -> {
                         var minSource = e.getKey().min();
                         var offset = source - minSource;
                         var minDest = e.getValue().min();
                         return minDest + offset;
                     })
                     .findFirst()
                     .orElse(source);
    }

    private static List<Range> tx(List<Range> inputs, List<Map<Range, Range>> txss) {
        if (txss.isEmpty()) return inputs;
        var txs = ListFun.head(txss);
        return tx(tx(inputs,
                     new ArrayList<>(),
                     txs
                    ),
                  ListFun.tail(txss)
                 );
    }

    private static List<Range> tx(List<Range> inputs,
                                  List<Range> outputs,
                                  Map<Range, Range> txs
                                 ) {
        System.out.println(inputs);
        if (inputs.isEmpty()) return outputs;
        var input = inputs.remove(0);
        var output = txs.entrySet()
                        .stream()
                        .map(e -> Pair.of(e.getValue().min() - e.getKey().min(),
                                          input.intersection(e.getKey())
                                         )
                            )
                        .filter(pair -> pair.second() != null)
                        .findFirst()
                        .orElse(Pair.of(-1L, input));


        return output.first() == -1L ?
                tx(inputs,
                   ListFun.append(outputs, input),
                   txs) :
                tx(ListFun.appendAll(inputs,
                                     input.difference(output.second())),
                   ListFun.append(outputs, output.second().translate(output.first())),
                   txs);

    }





}
