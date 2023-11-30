package advent_of_code_2022.day15;

import types.FileParsers;
import types.Pos;
import types.Range;

import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Day15_part_2 {
    record Sensor(Pos loc, Pos beacon, int d) {}

    public static void main(String[] args) {

        //language=RegExp
        var regex = "^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$";
        //var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day15/input_test.txt";
        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day15/input.txt";


        var sensors = FileParsers.toListOfLineMatchers(input, regex)
                                 .stream()
                                 .map(matcher -> {
                                          Pos loc = new Pos(parseInt(matcher.group(1)),
                                                            parseInt(matcher.group(2))
                                          );
                                          Pos beacon = new Pos(parseInt(matcher.group(3)),
                                                               parseInt(matcher.group(4))
                                          );
                                          return new Sensor(loc,
                                                            beacon,
                                                            loc.manhattanDistance(beacon)
                                          );
                                      }
                                     )
                                 .peek(System.out::println)
                                 .toList();

        var Y = 4000000;
        var X = 4000000;

        IntStream.range(0, Y + 1)
                 .forEach(y -> {
                              var intervals = sensors.stream()
                                                     .filter(sensor -> Math.abs(sensor.loc.y() - y) <= sensor.d)
                                                     .map(sensor -> getXInterval(sensor, y))
                                                     .collect(Collectors.toList());

                              var sorted = Range.union(intervals);

                              var x = 0;
                              for (var interval : sorted) {
                                  if (interval.min() > x) {
                                      System.out.println("(" + x + ", " + y + ")");
                                      System.out.println(BigInteger.valueOf(Y)
                                                                   .multiply(BigInteger.valueOf(x))
                                                                   .add(BigInteger.valueOf(y))
                                                        );
                                      System.exit(0);
                                  }
                                  x = interval.max() + 1;
                                  if (x > X) break; //next y
                              }
                          }
                         );


    }

    static Range getXInterval(Sensor sensor,
                              int y
                             ) {
        var wide = sensor.d - Math.abs(sensor.loc.y() - y);

        return new Range(sensor.loc.x() - wide,
                         sensor.loc.x() + wide
        );
    }


}
