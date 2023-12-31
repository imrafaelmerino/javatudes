package advent_of_code.advent_of_code_2022.day4;

import types.FileParsers;
import types.LongRange;

import java.util.List;

public class Solution {

    public static void main(String[] args) {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day4/input.txt";

        List<String> lines = FileParsers.toListOfLines(input);

        var n = lines.stream()
                     .map(line -> line.split(","))
                     .map(sections -> {
                              var sections1 = sections[0].split("-");
                              var sections2 = sections[1].split("-");
                              return List.of(new LongRange(Integer.parseInt(sections1[0]),
                                                           Integer.parseInt(sections1[1])
                                             ),
                                             new LongRange(Integer.parseInt(sections2[0]),
                                                           Integer.parseInt(sections2[1])
                                             )
                                            );


                          }
                         )
/*                     .filter(ranges -> {
                         Range r1 = ranges.get(0);
                         Range r2 = ranges.get(1);
                         return r1.contain(r2) || r2.contain(r1);
                     })*/
                     .filter(ranges -> ranges.get(0)
                                             .intersection(ranges.get(1)) != null)
                     .peek(System.out::println)
                     .count();

        System.out.println(n);


    }

    private static boolean isFullyOverlap(LongRange a, LongRange b) {
        if (a.min() > b.min()) return a.max() <= b.max();
        if (a.min() == b.min()) return true;
        return b.max() <= a.max();
    }

    private static boolean isOverlapAtAll(LongRange a, LongRange b) {
        return !((a.min() < b.min() && a.max() < b.min()) ||
                 a.max() > b.max() && a.min() > b.max());
    }
}
