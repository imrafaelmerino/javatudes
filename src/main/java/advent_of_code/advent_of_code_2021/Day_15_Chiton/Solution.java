package advent_of_code.advent_of_code_2021.Day_15_Chiton;

import search.AStarSearch;
import search.SearchPathCost;
import types.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Solution {


    public static void main(String[] args) {

        part1();
        part2();

    }

    private static void part1() {
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2021/Day_15_Chiton/input.txt";
        Grid<Integer> grid = PersistentGrid.fromFile(input)
                                           .mapValues((e, v) -> Integer.parseInt(v));


        Function<Pos, List<Pos>> successorGen = grid::getVHNeighbors;

        Pos target = new Pos(grid.xmax(),
                             grid.ymax()
        );

        BiFunction<Pos, Pos, Integer> stepFun = (pos, b) -> grid.getVal(b);
        var bb = AStarSearch.fromStates(successorGen,
                                        stepFun,
                                        b -> b.manhattanDistance(target));


        SearchPathCost<Pos> pathCost = bb.findFirst(new Pos(0, 0),
                                                    s -> s.equals(target)
                                                   );
        System.out.println(pathCost.cost());


        grid.color(
                    ControlChar.RED,
                    (pos, value) -> pathCost.path().states().contains(pos)
                  )
            .printRows();
    }

    private static void part2() {
        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2021/Day_15_Chiton/input.txt";
        Grid<Integer> tile = MutableGrid.fromFile(input)
                                        .mapValues((pos, v) -> Integer.parseInt(v));


        List<Pos> centers = Pos.rows(new IntRange(0, 4),
                                     new IntRange(0, 4)
                                    );

        tile.printRows();

        System.out.println("\n");

        Grid<Integer> grid = tile.tiles(centers,
                                        (center, e) -> {
                                            int newRisk = e.value() + center.x() + center.y();
                                            return newRisk <= 9 ? newRisk : newRisk % 9;
                                        }
                                       );

        grid.printRows();

        Function<Pos, List<Pos>> successorGen = grid::getVHNeighbors;
        Pos target = new Pos(grid.xmax(),
                             grid.ymax()

        );


        SearchPathCost<Pos> pathCost = AStarSearch.fromStates(successorGen,
                                                              (pos, b) -> grid.getVal(b),
                                                              b -> b.manhattanDistance(target)
                                                             )
                                                  .findFirst(new Pos(0, 0),
                                                             s -> s.equals(target)
                                                            );
        System.out.println(pathCost.cost());

//        grid.color(
//                    ControlChar.RED,
//                    (pos, val) -> pathCost.path()
//                                          .states()
//                                          .contains(pos)
//                  )
//            .printRows();

    }


}
