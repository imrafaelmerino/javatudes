package advent_of_code.advent_of_code_2022.day10;

import fun.tuple.Pair;
import types.FileParsers;
import types.ListFun;
import types.StreamFun;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 {


    public static void main(String[] args)  {


        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day10/input.txt";

        List<String> instructions = FileParsers.toListOfLines(input);

        var stream = stream(instructions, 1, 0, Stream.empty());

        //20, 60, 100, 140, 180, 220
        var samples = Stream.iterate(20,
                                     it -> it <= 220,
                                     it -> it + 40
                                    )
                            .toList();

        System.out.println(stream
                                   .filter(p -> samples.contains(p.second()))
                                   .map(p -> p.first() * p.second())
                                   .reduce(0,Integer::sum)

                          );


        System.out.println(stream(instructions, 1, 0, Stream.empty())
                                   .map(p -> {
                                            int x = p.second() % 40;
                                            var sprite = List.of(p.first(),
                                                                 p.first() + 1,
                                                                 p.first() + 2
                                                                );
                                            return (sprite.contains(x) ? "#" : ".") + (x == 0 ? "\n" : "");
                                        }
                                       )
                                   .reduce("", String::concat)
                          );


    }

    public static Stream<Pair<Integer, Integer>> stream(List<String> instructions,
                                                        int register,
                                                        int cycles,
                                                        Stream<Pair<Integer, Integer>> acc
                                                       ) {

        if (instructions.isEmpty()) return acc;
        String instruction = instructions.get(0);
        if (instruction.equalsIgnoreCase("noop"))
            return stream(ListFun.tail(instructions),
                          register,
                          cycles + 1,
                          StreamFun.concat(acc,
                                           Pair.of(register, cycles + 1)
                                          )
                         );

        String[] tokens = instruction.split("\s");
        int number = Integer.parseInt(tokens[1]);

        return stream(ListFun.tail(instructions),
                      register + number,
                      cycles + 2,
                      StreamFun.concat(acc,
                                       Pair.of(register, cycles + 1),
                                       Pair.of(register, cycles + 2)
                                      )
                     );

    }


}
