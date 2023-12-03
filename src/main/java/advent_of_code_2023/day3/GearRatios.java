package advent_of_code_2023.day3;

import types.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GearRatios {

    public static void main(String[] args) {
        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2023/day3/input.txt";
        var grid = MutableGrid.fromFile(path);


        System.out.println(solve_part1(grid));

        System.out.println(solve_part2(grid));

    }

    private static long solve_part2(Grid<String> grid) {
        return grid.find((pos, val) -> val.equals("*"))
                   .stream()
                   .map(cell -> getAdjacentParts(grid, cell.pos()))
                   .filter(parts -> parts.size() == 2)
                   .map(parts -> parts.get(0) * parts.get(1))
                   .reduce(Integer::sum)
                   .get();
    }

    private static List<Integer> getAdjacentParts(Grid<String> grid, Pos pos) {
        List<Integer> gears = new ArrayList<>();


        Pos right = pos.right();
        if (grid.containsPos(right)) {
            var rightDigits = findRightDigits(pos, grid);
            if (!rightDigits.isEmpty())
                gears.add(toInt(rightDigits));
        }
        Pos left = pos.left();
        if (grid.containsPos(left)) {
            var leftDigits = findLeftDigits(pos, grid);
            if (!leftDigits.isEmpty())
                gears.add(toInt(leftDigits));
        }

        Pos up = pos.up();
        if (grid.containsPos(up)) {
            var leftDigits = findLeftDigits(up, grid);
            var rightDigits = findRightDigits(up, grid);
            String val = grid.getVal(up);
            if (isDigit(val)) gears.add(joinDigits(leftDigits, val, rightDigits));
            else {
                if (!leftDigits.isEmpty()) gears.add(toInt(leftDigits));
                if (!rightDigits.isEmpty()) gears.add(toInt(rightDigits));
            }
        }

        Pos down = pos.down();
        if (grid.containsPos(down)) {
            var leftDigits = findLeftDigits(down, grid);
            var rightDigits = findRightDigits(down, grid);
            String val = grid.getVal(down);
            if (isDigit(val)) gears.add(joinDigits(leftDigits, val, rightDigits));
            else {
                if (!leftDigits.isEmpty()) gears.add(toInt(leftDigits));
                if (!rightDigits.isEmpty()) gears.add(toInt(rightDigits));
            }
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

        var explored = new HashSet<Pos>();
        var acc = 0;
        for (var candidate : candidates) {
            if (!explored.contains(candidate.pos())) {
                var leftCellDigits = findLeftDigits(candidate.pos(), grid);
                var rightCellDigits = findRightDigits(candidate.pos(), grid);
                explored.addAll(leftCellDigits.stream().map(Cell::pos).collect(Collectors.toSet()));
                explored.addAll(rightCellDigits.stream().map(Cell::pos).collect(Collectors.toSet()));
                var number = joinDigits(leftCellDigits, candidate.value(), rightCellDigits);
                acc += number;
            }
        }
        return acc;
    }

    private static int joinDigits(List<Cell<String>> leftCellDigits,
                                  String digit,
                                  List<Cell<String>> rightCellDigits) {
        return Integer.parseInt("%s%s%s".formatted(joinDigits(leftCellDigits),
                                                   digit,
                                                   joinDigits(rightCellDigits)
                                                  )
                               );
    }

    private static String joinDigits(List<Cell<String>> leftCellDigits) {
        return leftCellDigits.stream().map(Cell::value).collect(Collectors.joining());
    }

    public static boolean isDigit(String str) {
        return Character.isDigit(str.charAt(0));
    }

    public static boolean isSymbol(String str) {
        char val = str.charAt(0);
        return !Character.isDigit(val) && val != '.';
    }

    public static List<Cell<String>> findRightDigits(Pos pos, Grid<String> grid) {

        return findRightDigits(pos, grid, new ArrayList<>());
    }

    public static List<Cell<String>> findRightDigits(Pos pos, Grid<String> grid, List<Cell<String>> result) {
        var next = pos.right();
        if (!grid.containsPos(next)) return result;
        var val = grid.getVal(next);
        if (isDigit(val)) return findRightDigits(next, grid, ListFun.append(result, new Cell<>(next, val)));
        return result;

    }

    public static List<Cell<String>> findLeftDigits(Pos pos, Grid<String> grid, List<Cell<String>> result) {
        var next = pos.left();
        if (!grid.containsPos(next)) return result;
        var val = grid.getVal(next);
        if (isDigit(val)) return findLeftDigits(next, grid,
                                                ListFun.prepend(result, new Cell<>(next, val)));
        return result;

    }

    public static List<Cell<String>> findLeftDigits(Pos pos, Grid<String> grid) {
        return findLeftDigits(pos, grid, new ArrayList<>());

    }
}
