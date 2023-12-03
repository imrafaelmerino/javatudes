package advent_of_code.advent_of_code_2022.day12;

import search.*;
import types.*;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Day12 {


    public static void main(String[] args) {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day12/input.txt";
        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day12/input_test_2.txt";

        Grid<String> grid = MutableGrid.fromFile(new File(input).getPath());

        var S = grid.findOne((pos, val) -> val.equals("S")).pos();
        var E = grid.findOne((pos, val) -> val.equals("E")).pos();

        grid.put(S, "a");
        grid.put(E, "z");

        Function<Pos, List<Pos>> succ1 = source ->
                grid.getVHNeighbors(source).stream()
                    .filter(neighbor -> {
                        char sourceHeight = grid.getVal(source).charAt(0);
                        char targetHeight = grid.getVal(neighbor).charAt(0);
                        return targetHeight - sourceHeight <= 1;
                    })
                    .toList();



        System.out.println(BreathSearch.fromStates(succ1)
                                       .findFirst(S, p -> p.equals(E)).size() - 1
                          );

        //part 2 going backwards and updating succesors because we are descending now
        // faster than doing breadth searc from all a and picking the lowest

        Function<Pos, List<Pos>> succ2 = source ->
                grid.getVHNeighbors(source).stream()
                    .filter(neighbor -> {
                        char sourceHeight = grid.getVal(source).charAt(0);
                        char targetHeight = grid.getVal(neighbor).charAt(0);
                        return sourceHeight - targetHeight <= 1;
                    })
                    .toList();

        System.out.println(BreathSearch.fromStates(succ2)
                                       .findFirst(E, p -> grid.getVal(p).equals("a")).size() - 1
                          );


    }
}
