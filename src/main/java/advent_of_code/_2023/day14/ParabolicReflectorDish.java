package advent_of_code._2023.day14;

import advent_of_code._2023._2023_Puzzle;
import types.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * TODO, use matrix instead of grid and rotate. Optimize and refactor!
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
        var grid = MutableGrid.fromFile(getTestInputPath());
        return getLoad(tiltNorth(grid));
    }

    private static Grid<String> tiltNorth(Grid<String> grid) {
        var columns = grid.getColumns();
        for (int x = 0; x < columns.size(); x++) {
            for (int y = 1; y < columns.get(x).size(); y++) {
                var column = grid.getColumns().get(x);
                var symbol = column.get(y).value();
                if (symbol.equals("O")) {
                    boolean seen = false;
                    Integer best = null;
                    for (int i = 0; i < y; i++) {
                        Cell<String> c = column.get(i);
                        if (SYMBOLS.contains(c.value())) {
                            Integer integer = c.pos().y() + 1;
                            if (!seen || integer.compareTo(best) > 0) {
                                seen = true;
                                best = integer;
                            }
                        }
                    }
                    int to = seen ? best : 0;
                    if (to != y) {
                        grid = grid.put(new Pos(x, to), symbol);
                        grid = grid.put(new Pos(x, y), ".");
                    }
                }
            }
        }
        return grid;
    }

    private static Grid<String> tiltEast(Grid<String> grid) {
        for (int y = 0; y < grid.getRows().size(); y++) {
            for (int x = grid.xmax()-1; x >= 0; x--) {
                var rows = grid.getRows();
                var row = rows.get(y);
                var symbol = row.get(x).value();
                if (symbol.equals("O")) {
                    boolean seen = false;
                    Integer best = null;
                    int bound = grid.xmax();
                    for (int i = x + 1; i <= bound; i++) {
                        Cell<String> c = row.get(i);
                        if (SYMBOLS.contains(c.value())) {
                            Integer integer = c.pos().x() - 1;
                            if (!seen || integer.compareTo(best) < 0) {
                                seen = true;
                                best = integer;
                            }
                        }
                    }
                    int to = seen ? best : grid.xmax();
                    if (to != x) {
                        grid = grid.put(new Pos(to, y), symbol);
                        grid = grid.put(new Pos(x, y), ".");
                    }
                }
            }
        }
        return grid;
    }

    private static Grid<String> tiltWest(Grid<String> grid) {
        for (int y = 0; y < grid.getRows().size(); y++) {
            for (int x = 1; x < grid.getColumns().size(); x++) {
                var rows = grid.getRows();
                var row = rows.get(y);
                var symbol = row.get(x).value();
                if (symbol.equals("O")) {
                    boolean seen = false;
                    Integer best = null;
                    for (int i = 0; i < x; i++) {
                        Cell<String> c = row.get(i);
                        if (SYMBOLS.contains(c.value())) {
                            Integer integer = c.pos().x() + 1;
                            if (!seen || integer.compareTo(best) > 0) {
                                seen = true;
                                best = integer;
                            }
                        }
                    }
                    int to = seen ? best : 0;
                    if (to != x) {
                        grid = grid.put(new Pos(to, y), symbol);
                        grid = grid.put(new Pos(x, y), ".");
                    }
                }
            }
        }
        return grid;
    }


    private static Grid<String> tiltSouth(Grid<String> grid) {
        var columns = grid.getColumns();
        var ymax = grid.ymax();
        for (int x = 0; x < columns.size(); x++) {
            for (int y = ymax-1; y >= 0; y--) {
                var column = grid.getColumns().get(x);
                var symbol = column.get(y).value();
                if (symbol.equals("O")) {
                    boolean seen = false;
                    Integer best = null;
                    for (int i = y + 1; i <= ymax; i++) {
                        Cell<String> c = column.get(i);
                        if (SYMBOLS.contains(c.value())) {
                            Integer integer = c.pos().y() - 1;
                            if (!seen || integer.compareTo(best) < 0) {
                                seen = true;
                                best = integer;
                            }
                        }
                    }
                    int to = seen ? best : ymax;
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

        Grid<String> grid = MutableGrid.fromFile(getInputPath());
        var i=1;
        var loads = new ArrayList<String>();
        while (true) {
            grid = tiltEast(tiltSouth(tiltWest(tiltNorth(grid))));
            var load = getLoad(grid);
            System.out.println("%s, %s".formatted(i,load));
            i=i+1;
            loads.add(load+"");
            if(i==1000)break;
        }

        var result = StrFun.findLongestRepetition(loads.stream().collect(Collectors.joining(",")));

        System.out.println(result);
        return result;


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
