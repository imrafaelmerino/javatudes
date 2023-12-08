package advent_of_code._2023.day6;

import types.FileParsers;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static types.ListFun.tail;
import static types.ListFun.toListOfInt;

public class WaitForIt {

    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day6/input.txt";
        var lines = FileParsers.toListOfLines(input);
        var time = toListOfInt(tail(Arrays.stream(lines.get(0)
                                                       .split("\\s+"))
                                          .toList()));
        var distance = toListOfInt(tail(Arrays.stream(lines.get(1)
                                                           .split("\\s+"))
                                              .toList()));

        var sol = IntStream.range(0, time.size())
                           .mapToLong(n -> allWins(time.get(n),
                                                   distance.get(n))
                                     )
                           .reduce(1L,
                                   (a, b) -> a * b
                                  );

        var sol2 = allWins(Long.parseLong(time.stream()
                                              .map(Object::toString)
                                              .collect(Collectors.joining())),
                           Long.parseLong(distance.stream()
                                                  .map(Object::toString)
                                                  .collect(Collectors.joining()))
                          );

        System.out.println(sol);

        System.out.println(sol2);

    }

    private static long allWins(long raceTime, long recordDistance) {
        return LongStream.rangeClosed(0, raceTime)
                         .map(timeHolding -> timeHolding * (raceTime - timeHolding))
                         .filter(distance -> distance > recordDistance)
                         .count();
    }
}
