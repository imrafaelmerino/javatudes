package advent_of_code_2022.day5;

import types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Solution {


    public static void main(String[] args) {

        part1();
        part2();

    }

    record Instruction(int size, int from, int to) {}

    private static void part1() {
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day5/input.txt";
        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day5/input_test_2.txt";
        List<MutableGrid<String>> grids = MutableGrid.fromGroupsOfLines(FileParsers.toGroupsOfLines(input));


        List<Instruction> instructions = grids.get(1)
                                              .reduceRows(row -> row.stream().map(Cell::value).collect(Collectors.joining()))
                                              .stream()
                                              .map(instruction -> instruction.split("\s"))
                                              .map(words -> new Instruction(parseInt(words[1]),
                                                                            parseInt(words[3]) - 1, //index starts at zero
                                                                            parseInt(words[5]) - 1  //index starts at zero
                                                   )
                                                  )
                                              .collect(Collectors.toList());


        List<List<String>> stacks =
                grids.get(0)
                     .getColumns((pos, val) -> val,
                                 (pos, val) -> Constants.UPPER_CASE_LETTERS.contains(val) //remove all characters but uppercase letters
                                )
                     .stream()
                     .filter(column -> !column.isEmpty())  //empty columns between stacks
                     .toList();

        System.out.println(stacks);
        System.out.println(instructions);

        for (var ins : instructions) {
            for (int i = 0; i < ins.size; i++) {
                String cargo = stacks.get(ins.from)
                                     .remove(0);
                stacks.get(ins.to)
                      .add(0,
                           cargo
                          );
            }
        }

        System.out.println(stacks.stream()
                                 .map(stack -> stack.remove(0))
                                 .collect(Collectors.joining()));
    }


    private static void part2() {
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day5/input.txt";
        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day5/input_test_2.txt";
        List<MutableGrid<String>> grids = MutableGrid.fromGroupsOfLines(FileParsers.toGroupsOfLines(input));


        List<Instruction> instructions = grids.get(1)
                                              .joinRows()
                                              .stream()
                                              .map(instruction -> instruction.split("\s"))
                                              .map(words -> new Instruction(parseInt(words[1]),
                                                                            parseInt(words[3]) - 1, //index starts at zero
                                                                            parseInt(words[5]) - 1  //index starts at zero
                                                   )
                                                  )
                                              .collect(Collectors.toList());


        List<List<String>> stacks =
                grids.get(0)
                     .getColumns((pos,val)->val,
                                 (pos,val) -> Constants.UPPER_CASE_LETTERS.contains(val)
                                )
                     .stream()
                     .filter(column -> !column.isEmpty())  //empty columns from input between stacks
                     .collect(Collectors.toList());

        System.out.println(stacks);
        System.out.println(instructions);

        for (var ins : instructions) {
            List<String> cargo = new ArrayList<>();
            for (int i = 0; i < ins.size; i++) {
                cargo.add(stacks.get(ins.from)
                                .remove(0)
                         );
            }

            stacks.get(ins.to)
                  .addAll(0,
                          cargo
                         );

        }

        System.out.println(stacks.stream()
                                 .map(stack -> stack.remove(0))
                                 .collect(Collectors.joining()));
    }


}
