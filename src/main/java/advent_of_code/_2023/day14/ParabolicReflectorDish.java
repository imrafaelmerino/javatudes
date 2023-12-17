package advent_of_code._2023.day14;

import advent_of_code._2023._2023_Puzzle;
import types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;


/**
 * TODO, use matrix instead of grid and rotate. Optimize and refactor!
 * calculate solution programmatically instead printing and working out the offset and period...
 */

public class ParabolicReflectorDish implements _2023_Puzzle {

    private static final Set<String> SYMBOLS = Set.of("#", "O");

    private static Integer getLoad(Grid<String> grid) {
        var size = grid.getRows().size();
        return grid.find((pos, val) -> val.equals("O"))
                   .stream()
                   .map(c -> size - c.pos().y())
                   .reduce(0, Integer::sum);
    }

    @Override
    public Object solveFirst() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath());
        return getLoad(tiltNorth(grid));
    }

    private static Grid<String> tiltNorth(Grid<String> grid) {
        var columns = grid.getColumns();
        for (int x = 0; x < columns.size(); x++) {
            for (int y = 1; y < columns.get(x).size(); y++) {
                var column = grid.getColumns().get(x);
                var symbol = column.get(y).value();
                if (symbol.equals("O")) {
                    int to = IntStream.range(0, y)
                                      .mapToObj(column::get)
                                      .filter(c -> SYMBOLS.contains(c.value()))
                                      .map(c -> c.pos().y() + 1)
                                      .max(Integer::compareTo)
                                      .orElse(0);
                    if (to != y) {
                        grid = grid.put(new Pos(x, to), symbol);
                        grid = grid.put(new Pos(x, y), ".");
                    }
                }
            }
        }
        return grid;
    }







    @Override
    public Object solveSecond() throws Exception {

       return null;

    }

    @Override
    public String name() {
        return "Parabolic Reflector Dish";
    }

    @Override
    public int day() {
        return 14;
    }

    @Override
    public String outputUnitsPart1() {
        return "total load";
    }

    @Override
    public String outputUnitsPart2() {
        return "total load";
    }
}
