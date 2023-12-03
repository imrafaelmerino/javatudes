package advent_of_code.advent_of_code_2020.Day_20_Jurasic_Jigsaw;

import search.csp.*;
import types.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    record Tile(long id, List<String> top, List<String> bottom, List<String> right, List<String> left) {}

    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2020/Day_20_Jurasic_Jigsaw/input.txt";

        Function<Integer, List<Pos>> getVars = i -> {
            List<Pos> xs = new ArrayList<>();
            for (int j = 0; j < i; j++) for (int k = 0; k < i; k++) xs.add(new Pos(j, k));
            return xs;
        };

        Function<String, Integer> getId = s -> Integer.parseInt(s.split(" ")[1].split(":")[0]);

        Function<Grid<String>, Set<Grid<String>>> tx =
                grid -> Stream.of(grid,
                                  grid.rotate90(),
                                  grid.flipH(),
                                  grid.flipV(),
                                  grid.flipV().flipH(),
                                  grid.rotate90().flipH(),
                                  grid.rotate90().flipV(),
                                  grid.rotate90().flipV().flipH()
                                 )
                              .collect(Collectors.toSet());

        List<Tile> domainValues = new ArrayList<>();
        List<List<String>> tiles = FileParsers.toGroupsOfLines(input);
        tiles.forEach(group -> {
                          var id = getId.apply(group.get(0));
                          var grid = MutableGrid.fromRows(group.subList(1, group.size())
                                                                  .stream().map(it -> Arrays.stream(it.split(""))
                                                                                            .toList())
                                                                  .toList()
                                                            );

                          var txs = tx.apply(grid);


                          domainValues.addAll(txs.stream()
                                                 .map(tile -> new Tile(id,
                                                                       tile.getTopBorder().stream().map(Cell::value).toList(),
                                                                       tile.getBottomBorder().stream().map(Cell::value).toList(),
                                                                       tile.getRightBorder().stream().map(Cell::value).toList(),
                                                                       tile.getLeftBorder().stream().map(Cell::value).toList()
                                                      )
                                                     ).toList());
                      }
                     );


        int nTiles = tiles.size();
        int size = ((int) Math.sqrt(nTiles));


        List<Pos> variables = getVars.apply(size);


        Map<Pos, List<Tile>> domain = variables.stream()
                                               .collect(Collectors.toMap(k -> k,
                                                                         k -> domainValues
                                                                         )
                                                       );

        List<Constraint<Pos, Tile>> constraints = new ArrayList<>();

        constraints.addAll(Constraint.allSuchThat(variables,
                                                  (tile1, tile2) -> tile1.id != tile2.id
                                                 )
                          );

        for (Pos variable : variables) {
            if (variable.x() > 0)
                constraints.add(new BinaryConstraint<>(variable,
                                                       variable.left(),
                                                       (tile1, tile2) -> tile1.left.equals(tile2.right)
                ));

            if (variable.x() < size - 1)
                constraints.add(new BinaryConstraint<>(variable,
                                                       variable.right(),
                                                       (tile1, tile2) -> tile1.right.equals(tile2.left)
                ));
            if (variable.y() > 0)
                constraints.add(new BinaryConstraint<>(variable,
                                                       variable.down(),
                                                       (tile1, tile2) -> tile1.bottom.equals(tile2.top)
                ));


            if (variable.y() < size - 1)
                constraints.add(new BinaryConstraint<>(variable,
                                                       variable.up(),
                                                       (tile1, tile2) -> tile1.top.equals(tile2.bottom)
                ));
        }

        var solution = new FindFirst<>(variables, domain, constraints).findFirst();


        long result = multiplyCorners(solution, size);

        System.out.println(result);


        new FindAll<>(variables, domain, constraints)
                .stream()
                .filter(ass -> Fun.<Pos, Tile>isAssignmentComplete(variables).test(ass))
                .forEach(it -> System.out.println(multiplyCorners(it, size)));


    }

    public static long multiplyCorners(Map<Pos, Tile> assignments, int size) {

        return assignments.entrySet()
                          .stream()
                          .filter(e -> e.getKey().equals(new Pos(0, 0))
                                  || e.getKey().equals(new Pos(0, size - 1))
                                  || e.getKey().equals(new Pos(size - 1, 0))
                                  || e.getKey().equals(new Pos(size - 1, size - 1))
                       )
                .map(e -> e.getValue().id)
                .reduce(1L,(a, b) -> a * b);
    }


}
