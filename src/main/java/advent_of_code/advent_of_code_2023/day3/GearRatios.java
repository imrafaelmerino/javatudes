package advent_of_code.advent_of_code_2023.day3;

import types.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GearRatios {

    public static void main(String[] args) {
        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day3/input.txt";
        var grid = MutableGrid.fromFile(path);


        System.out.println(solve_part1(grid));

        System.out.println(solve_part2(grid));

    }

    private static long solve_part2(Grid<String> grid) {
        return grid.find((pos, val) -> val.equals("*"))
                   .stream()
                   .map(cell -> getAdjacentEngineParts(grid, cell.pos()))
                   .filter(parts -> parts.size() == 2)
                   .map(parts -> parts.get(0) * parts.get(1))
                   .reduce(Integer::sum)
                   .get();
    }

    private static List<Integer> getAdjacentEngineParts(Grid<String> grid, Pos pos) {
        List<Integer> gears = new ArrayList<>();

        //pos is a gear
        gears.addAll(getHorizontalEngineParts(grid, pos));

        //pos up doesn't have to be a gear
        gears.addAll(getVerticalEnginePart(pos.up(), grid));

        //pos down doesn't have to be a gear
        gears.addAll(getVerticalEnginePart(pos.down(), grid));

        return gears;
    }

    private static List<Integer> getVerticalEnginePart(Pos pos, Grid<String> grid) {
        var gears = new ArrayList<Integer>();
        if (grid.containsPos(pos)) {
            String val = grid.getVal(pos);
            if (isDigit(val)) gears.add(joinDigits(findLeftDigits(pos, grid),
                                                   val,
                                                   findRightDigits(pos, grid))
                                       );
            else gears.addAll(getHorizontalEngineParts(grid, pos));

        }
        return gears;
    }

    private static List<Integer> getHorizontalEngineParts(Grid<String> grid, Pos pos) {
        var gears = new ArrayList<Integer>();
        var right = pos.right();
        if (grid.containsPos(right)) {
            var rightDigits = findRightDigits(pos, grid);
            if (!rightDigits.isEmpty()) gears.add(toInt(rightDigits));
        }
        Pos left = pos.left();
        if (grid.containsPos(left)) {
            var leftDigits = findLeftDigits(pos, grid);
            if (!leftDigits.isEmpty()) gears.add(toInt(leftDigits));
        }
        return gears;
    }

    private static int toInt(List<Cell<String>> rightDigits) {
        return Integer.parseInt(joinDigits(rightDigits));
    }

    private static int solve_part1(Grid<String> grid) {
        var candidates = grid.find((pos, val) -> isDigit(val)
                                                 && grid.getNeighbors(pos)
                                                        .stream()
                                                        .anyMatch(p -> isSymbol(grid.getVal(p)))
                                  );

        //explored contains candidates that has already been taken account processing another symbol.
        //For example, in the following example 1 and 3 are candidates, but we want to count 123 just once and not twice
        //  *123*
        var explored = new HashSet<Pos>();
        var acc = 0;
        for (var candidate : candidates) {
            if (!explored.contains(candidate.pos())) {
                var left = findLeftDigits(candidate.pos(), grid);
                var right = findRightDigits(candidate.pos(), grid);
                explored.addAll(left.stream().map(Cell::pos).toList());
                explored.addAll(right.stream().map(Cell::pos).toList());
                var number = joinDigits(left,
                                        candidate.value(),
                                        right);
                acc += number;
            }
        }
        return acc;
    }

    private static int joinDigits(List<Cell<String>> leftCellDigits,
                                  String digit,
                                  List<Cell<String>> rightCellDigits
                                 ) {
        return Integer.parseInt("%s%s%s".formatted(joinDigits(leftCellDigits),
                                                   digit,
                                                   joinDigits(rightCellDigits)
                                                  )
                               );
    }

    private static String joinDigits(List<Cell<String>> cells) {
        return cells.stream().map(Cell::value).collect(Collectors.joining());
    }

    public static boolean isDigit(String str) {
        return Character.isDigit(str.charAt(0));
    }

    public static boolean isSymbol(String str) {
        var val = str.charAt(0);
        return !Character.isDigit(val) && val != '.';
    }

    public static List<Cell<String>> findRightDigits(Pos pos,
                                                     Grid<String> grid
                                                    ) {

        return findRightDigits(pos, grid, new ArrayList<>());
    }

    public static List<Cell<String>> findRightDigits(Pos pos,
                                                     Grid<String> grid,
                                                     List<Cell<String>> result
                                                    ) {
        var next = pos.right();
        if (!grid.containsPos(next)) return result;
        var val = grid.getVal(next);
        if (isDigit(val)) return findRightDigits(next,
                                                 grid,
                                                 ListFun.append(result,
                                                                new Cell<>(next, val)));
        return result;

    }

    public static List<Cell<String>> findLeftDigits(Pos pos,
                                                    Grid<String> grid,
                                                    List<Cell<String>> result
                                                   ) {
        var next = pos.left();
        if (!grid.containsPos(next)) return result;
        var val = grid.getVal(next);
        if (isDigit(val)) return findLeftDigits(next,
                                                grid,
                                                ListFun.prepend(result,
                                                                new Cell<>(next, val)));
        return result;

    }

    public static List<Cell<String>> findLeftDigits(Pos pos, Grid<String> grid) {
        return findLeftDigits(pos, grid, new ArrayList<>());

    }
}
