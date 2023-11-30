package advent_of_code_2022.day23;

import fun.tuple.Pair;
import types.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 {

    private Grid<String> grid;

    public static List<String> DIR = List.of("N", "S", "W", "E");

    public static Function<Integer, Supplier<Iterator<String>>> ITER =
            first -> () -> Stream.iterate(first % DIR.size(),
                                          i -> (i + 1) % DIR.size()
                                         )
                                 .map(n -> {
                                     String s = DIR.get(n);
                                     //System.out.println(s);
                                     return s;
                                 })
                                 .iterator();

    public Object solve1() {

       for (int i = 0; i < 10; i++) {
            grid = next(grid,ITER.apply(i));
            if(grid == null) break;
        }
        return (grid.ymax()-grid.ymin()+1)*(grid.xmax()-grid.xmin()+1)-grid.size();
    }

    public Object solve2() {
        //Part 2
        for (int i = 0; i < 100000; i++) {
            grid = next(grid, ITER.apply(i));

            if (grid == null) {
                System.out.println(i + 1);
                System.exit(1);
            }
        }

        return null;
    }
    public Grid<String> next(Grid<String> gs, Supplier<Iterator<String>> iters) {
        //candidates, someone around
        var xs = gs.filter((pos, val) -> !pos
                                   .getNeighbors()
                                   .stream()
                                   .allMatch(it -> gs.getVal(it) == null)
                          );

        if (xs.isEmpty()) return null;

        //ones that propose a move
        List<Pair<Pos, Pos>> ys =
                xs.getCells()
                  .stream()
                  .map(it -> propose(it.pos(), gs, iters))
                  .filter(Objects::nonNull)
                  .toList();

        //  pos proposed for just one elf
        List<Pair<Pos, Pos>> zs =
                ys.stream()
                  .collect(Collectors.groupingBy(Pair::second))
                  .values()
                  .stream()
                  .filter(pos -> pos.size() == 1)
                  .map(triples -> triples.get(0))
                  .toList();

        // old positions to be removed
        var rs = zs.stream()
                   .map(Pair::first)
                   .toList();

        return gs.merge(PersistentGrid.fromCells(zs.stream()
                                                   .map(t -> new Cell<>(t.second(),
                                                                        "#"
                                                   ))
                                                   .toList()
                                                )
                       )
                 .remove((pos, val) -> rs.contains(pos));

    }


    private Pair<Pos, Pos> propose(Pos pos,
                                   Grid<String> gs,
                                   Supplier<Iterator<String>> iters
                                  ) {
        var iter = iters.get();
        for (int i = 0; i < DIR.size(); i++) {
            String dir = iter.next();
            if (dir.equals("N") && isNorthFree(gs, pos))
                return Pair.of(pos, pos.down());
            if (dir.equals("S") && isSouthFree(gs, pos))
                return Pair.of(pos, pos.up());
            if (dir.equals("E") && isEastFree(gs, pos))
                return Pair.of(pos, pos.right());
            if (dir.equals("W") && isWestFree(gs, pos))
                return Pair.of(pos, pos.left());
        }
        return null;


    }

    private static boolean isWestFree(Grid<String> grid, Pos pos) {
        return grid.getVal(pos.left()) == null
                && grid.getVal(pos.left().down()) == null
                && grid.getVal(pos.left().up()) == null;
    }

    private static boolean isEastFree(Grid<String> grid, Pos pos) {
        return grid.getVal(pos.right()) == null
                && grid.getVal(pos.right().down()) == null
                && grid.getVal(pos.right().up()) == null;
    }

    private static boolean isSouthFree(Grid<String> grid, Pos pos) {
        return grid.getVal(pos.up()) == null
                && grid.getVal(pos.up().right()) == null
                && grid.getVal(pos.up().left()) == null;
    }

    private static boolean isNorthFree(Grid<String> grid, Pos pos) {
        return grid.getVal(pos.down()) == null
                && grid.getVal(pos.down().right()) == null
                && grid.getVal(pos.down().left()) == null;
    }

    public Day23(String input) {

        grid = PersistentGrid.fromFile(input)
                             .remove((pos, val) -> val.equals(".") || val.equals(" "));

    }


    public static void main(String[] args) {
        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day23/input.txt";

        Day23 day23 = new Day23(input);
        System.out.println(day23.solve1());
        System.out.println(day23.solve2());
    }

    static void print(Grid<String> grid) {

        grid.copy().printRows(".", "");
        System.out.println("\n");
    }
}
