package advent_of_code.advent_of_code_2022.day13;

import fun.tuple.Pair;
import types.FileParsers;
import types.ListFun;
import types.StringParsers;

import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("raw")
public class Day13 {

    /**
     * I've created the function StringParsers.toNestedLists to be prepared for similar problems.
     * It's a shame not to be able to have an eval function like in python
     *
     * @param args
     */
    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day13/input.txt";
        //var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day13/input_test_2.txt";

        //[pack1, pack2]   [pack3, pack4] ....
        var packets = FileParsers.toGroupsOfLines(input)
                                 .stream()
                                 .map(rows -> List.of(StringParsers.toList(rows.get(0)),
                                                      StringParsers.toList(rows.get(1))
                                                     )
                                     )
                                 .toList();


        var result = IntStream.range(0, packets.size())
                              .mapToObj(i -> Pair.of(i + 1,
                                                     compare(packets.get(i).get(0),
                                                             packets.get(i).get(1)
                                                            )
                                                    )
                                       )
                              .filter(p -> p.second() == -1)
                              .map(Pair::first)
                              .reduce(0, Integer::sum);

        System.out.println(result);

        String D1 = "[[2]]";
        String D2 = "[[6]]";

        var allpackets = ListFun.append(FileParsers.toListOfLines(input, String::isEmpty),
                                        D1,
                                        D2
                                       );


        var sorted = allpackets.stream()
                               .sorted((o1, o2) -> compare(StringParsers.toList(o1),
                                                           StringParsers.toList(o2)
                                                          )
                                      )
                               .toList();


        System.out.println((sorted.indexOf(D1) + 1) * (sorted.indexOf(D2) + 1));


    }

    @SuppressWarnings("unchecked")
    static int compare(List left,
                       List right
                      ) {

        if (right.isEmpty() && left.isEmpty()) return 0;
        if (left.isEmpty()) return -1;
        if (right.isEmpty()) return 1;

        var lh = left.get(0);
        var rh = right.get(0);

        if (lh instanceof List lhl && rh instanceof List rhl) {
            var r = compare(lhl, rhl);
            return r == 0 ? compare(ListFun.tail(left),
                                    ListFun.tail(right)
                                   ) :
                    r;

        }
        if (lh instanceof String lhi && rh instanceof String rhi) {
            return lhi.equals(rhi) ?
                    compare(ListFun.tail(left),
                            ListFun.tail(right)
                           ) :
                    Integer.parseInt(lhi) < Integer.parseInt(rhi) ? -1 : 1;

        }
        if (lh instanceof List && rh instanceof String rhi) {
            right.set(0, ListFun.mutableOf(rhi));
            return compare(left, right);
        }

        if (rh instanceof List && lh instanceof String lhi) {
            left.set(0, ListFun.mutableOf(lhi));
            return compare(left, right);
        }


        throw new RuntimeException();

    }


}
