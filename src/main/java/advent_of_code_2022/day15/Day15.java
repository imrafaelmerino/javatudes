package advent_of_code_2022.day15;

import types.FileParsers;
import types.Pos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class Day15 {

    public static void main(String[] args) {

        //language=RegExp
        var regex = "^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)$";
        var pattern = Pattern.compile(regex);

        //var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day15/input_test.txt";
        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day15/input.txt";

        record Sensor(Pos loc, Pos beacon, int d) {}


        var xs = FileParsers.toListOfLines(input)
                            .stream()
                            .map(line -> {
                                Matcher matcher = pattern.matcher(line);
                                matcher.matches();
                                return matcher;
                            })
                            .map(
                                    m -> {
                                        Pos sensor = new Pos(parseInt(m.group(1)), parseInt(m.group(2)));
                                        Pos beacon = new Pos(parseInt(m.group(3)), parseInt(m.group(4)));
                                        int d = sensor.manhattanDistance(beacon);
                                        return new Sensor(sensor,
                                                          beacon,
                                                          d
                                        );
                                    }
                                )
                            .toList();


        int y = 2000000;

        var x = xs.stream()
                  .filter(sensor ->
                                  sensor.d - Math.abs(sensor.loc.y() - y) >= 0
                         )
                  .flatMap(sensor -> {
                      var xcoverageAtY = sensor.d - Math.abs(sensor.loc.y() - y);
                      var xmin = sensor.loc.x() - xcoverageAtY;
                      var xmax = sensor.loc.x() + xcoverageAtY;
                      return IntStream.range(xmin, xmax + 1).boxed();
                  })
                  .collect(Collectors.toSet());


        var beaconsAtY = xs.stream()
                           .filter(s -> s.beacon.y() == y)
                           .map(s -> s.beacon.x())
                           .collect(Collectors.toSet());

        System.out.println(x.size() - beaconsAtY.size());


    }


}
