package advent_of_code.advent_of_code_2022.day14;

import types.*;

import java.util.Arrays;
import java.util.List;

public class Day14_2 {


    public static final String SAND = "o";

    public static final String ROCK = "#";

    public static void main(String[] args) {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day14/input.txt";


        List<String> paths = FileParsers.toListOfLines(input);

        List<List<Pos>> edges = paths.stream()
                                     .map(line -> Arrays.stream(line.split("->")).toList())
                                     .map(e -> e.stream()
                                                .map(it -> it.split(","))
                                                .map(it -> new Pos(Integer.parseInt(it[0].trim()),
                                                                   Integer.parseInt(it[1].trim())
                                                ))
                                                .toList()
                                         )
                                     .toList();


        List<List<Pos>> rocks = edges.stream()
                                     .map(line -> ListFun.sliding(line, 2)
                                                         .flatMap(it ->
                                                                          Pos.line(it.get(0),
                                                                                             it.get(1)
                                                                                            )
                                                                             .stream()
                                                                 )
                                                         .toList()
                                         )
                                     .toList();

        var grid = MutableGrid.fromCells(rocks.stream().flatMap(it -> it.stream()
                                                                  .map(a -> new Cell<>(a, ROCK))
                                                         ).toList()
                                  );

        Pos SOURCE = new Pos(500, 0);


        var bottom = grid.ymax() + 2;


        int xmax = grid.xmax();
        int xmin = grid.xmin();
        for (int x = xmin - 1000; x < xmax + 1000; x++) {
            grid.put(new Pos(x, bottom), ROCK);
        }

        grid.put(SOURCE, "+");
        //grid.printRows(".", "");

        for (int i = 0; i < 1000000; i++) {
            System.out.println(i);
            System.out.println("\n\n");
            Grid<String> a = pour(grid, SOURCE);
            String s = a.getVal(SOURCE);
            if (s != null && s.equals(SAND)) {
                a.printRows(".", "");
                break;
            }
            a.put(SOURCE, "+");
            // a.printRows(".", "");

        }

    }


    static Grid<String> pour(Grid<String> grid, Pos current) {
        Pos down = current.up();
        String spaceDown = grid.getVal(down);
        if (spaceDown == null) {
            return pour(grid.remove(current).put(down, SAND), down);
        }
        var spaceDownLeft = down.left();
        var spaceDownRight = down.right();

        if (spaceDown.equals(ROCK)) {
            if (grid.getVal(spaceDownLeft) != null && grid.getVal(spaceDownRight) != null)
                return grid.put(current, SAND);

        }

        if (spaceDown.equals(SAND) || spaceDown.equals(ROCK)) {

            if (grid.getVal(spaceDownLeft) == null)
                return pour(grid.remove(current).put(spaceDownLeft, SAND), spaceDownLeft);


            if (grid.getVal(spaceDownRight) == null)
                return pour(grid.remove(current).put(spaceDownRight, SAND), spaceDownRight);

            return grid.put(current, SAND);
        }

        return grid;


    }


}
