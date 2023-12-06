package advent_of_code.advent_of_code_2023.day5;

import types.FileParsers;
import types.ListFun;
import types.StrFun;

import java.util.*;
import java.util.stream.Collectors;

public class Seeds {

    public static void main(String[] args) {

        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day5/input.txt";

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
        var txs = List.of(seedToSoil,
                          soilToFertilizer,
                          fertilizerToWater,
                          waterToLight,
                          lightToTemperature,
                          temperatureToHumidity,
                          humidityToLocation);


        var sol_1 = seeds.stream()
                         .map(seed -> goThroughRanges(seed, txs))
                         .min(Long::compareTo);

        System.out.println(sol_1.get());


        List<Range> sources = new ArrayList<>();
        for (int i = 0; i < seeds.size(); i++)
            if (i % 2 != 0) sources.add(new Range(seeds.get(i - 1),
                                                  seeds.get(i - 1) + seeds.get(i) - 1));

        for (var tx : txs) {
            var next = new ArrayList<Range>();
            while (!sources.isEmpty()) {
                var source = sources.remove(sources.size()-1);
                boolean mapped = false;
                for (var from : tx.keySet()) {
                    var os = Math.max(from.min, source.min);
                    var oe = Math.min(from.max, source.max);
                    if (os < oe) {
                        mapped = true;
                        Range translated = new Range(os, oe).translate(from, tx.get(from));
                        next.add(translated);
                        if (os > source.min()) {
                            sources.add(new Range(source.min, os));
                        }
                        if (oe < source.max()) {
                            sources.add(new Range(oe, source.max));
                        }
                        break;
                    }
                }
                if (!mapped) {
                    next.add(source);
                }
            }
            sources = next;
        }

        List<Long> sorted = sources.stream()
                                   .map(Range::min)
                                   .sorted(Long::compareTo)
                                   .toList();

        System.out.println(sorted.get(0));

    }

    //intersection


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



    record Range(long min, long max) {


        boolean contain(long s) {
            return s >= min && s <= max;
        }

        public Range intersection(Range other) {
            var min = Math.max(this.min, other.min);
            var max = Math.min(this.max, other.max);
            // Check if there is a valid intersection
            // Return null or some special value to indicate no intersection
            return min <= max ? new Range(min, max) : null;
        }

        public List<Range> intersection(Set<Range> others) {
            return others.stream()
                         .map(this::intersection)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());
        }

        public List<Range> difference(List<Range> others) {
            List<Range> result = ListFun.append(new ArrayList<>(), this);

            for (var other : others)
                result = result.stream()
                               .flatMap(current -> current.difference(other).stream())
                               .toList();

            return result;
        }

        public long length() {
            return max - min + 1;
        }


        public List<Range> difference(Range other) {
            List<Range> result = new ArrayList<>();

            var min = Math.max(this.min, other.min);
            var max = Math.min(this.max, other.max);

            if (min <= max) {
                // There is an intersection, split into two ranges
                if (this.min < min) result.add(new Range(this.min, min - 1));
                if (this.max > max) result.add(new Range(max + 1, this.max));
            } else result.add(this);            // No intersection, the entire range is retained


            return result;
        }

        public boolean contained(Range other) {
            return this.min >= other.min && this.max <= other.max;
        }


        public Range translate(Range from, Range to) {
            if (from.length() != to.length())
                throw new IllegalArgumentException("source and target different length");
            if (!this.contained(from)) throw new IllegalArgumentException("this not contained in source");
            Range result = new Range(to.min + this.min - from.min,
                                     to.max - (from.max - this.max
                                     )
            );
            return result;
        }

        @Override
        public String toString() {
            return "(%s, %s)".formatted(min, max);
        }
    }


}
