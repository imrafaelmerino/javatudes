package advent_of_code._2023.day6;

import advent_of_code.Puzzle;
import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static types.ListFun.tail;
import static types.ListFun.toListOfInt;

public final class WaitForIt implements _2023_Puzzle {


    private static long allWins(long raceTime, long recordDistance) {
        return LongStream.rangeClosed(0, raceTime)
                         .map(timeHolding -> timeHolding * (raceTime - timeHolding))
                         .filter(distance -> distance > recordDistance)
                         .count();
    }

    @Override
    public Object solveFirst()  {
        var lines = FileParsers.toListOfLines(getInputPath());
        var time = toListOfInt(tail(Arrays.stream(lines.get(0)
                                                       .split("\\s+"))
                                          .toList()));
        var distance = toListOfInt(tail(Arrays.stream(lines.get(1)
                                                           .split("\\s+"))
                                              .toList()));

        return IntStream.range(0, time.size())
                        .mapToLong(n -> allWins(time.get(n),
                                                distance.get(n))
                                  )
                        .reduce(1L,
                                (a, b) -> a * b
                               );


    }

    @Override
    public Object solveSecond() {
        var lines = FileParsers.toListOfLines(getInputPath());
        var time = toListOfInt(tail(Arrays.stream(lines.get(0)
                                                       .split("\\s+"))
                                          .toList()));
        var distance = toListOfInt(tail(Arrays.stream(lines.get(1)
                                                           .split("\\s+"))
                                              .toList()));


        return allWins(Long.parseLong(time.stream()
                                          .map(Object::toString)
                                          .collect(Collectors.joining())),
                       Long.parseLong(distance.stream()
                                              .map(Object::toString)
                                              .collect(Collectors.joining()))
                      );

    }

    @Override
    public String name() {
        return "Wait For it";
    }

    @Override
    public int day() {
        return 6;
    }


    @Override
    public String outputUnitsPart1() {
        return "product of the number of ways you can beat the record";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }
}
