package advent_of_code._2023.day10;

import advent_of_code._2023._2023_Puzzle;
import search.DepthSearch1;
import search.SearchPath;
import types.Cell;
import types.Grid;
import types.MutableGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PipeMaze implements _2023_Puzzle {


    private static final String SOUTH = "SOUTH";
    private static final String NORTH = "NORTH";
    private static final String EAST = "EAST";
    private static final String WEST = "WEST";
    static Map<String, List<String>> CONNECTIONS = Map.of(
            "|", List.of(SOUTH, NORTH),
            "L", List.of(NORTH, EAST),
            "-", List.of(EAST, WEST),
            "J", List.of(NORTH, WEST),
            "7", List.of(SOUTH, WEST),
            "F", List.of(SOUTH, EAST)
                                                         );
    static String input1 = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
            """;
    static String input2 = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """;


    @Override
    public Object solveFirst() throws Exception {
        //var grid = MutableGrid.fromFile(getInputPath());
        var grid = MutableGrid.fromString(input1);
        var start = grid.findOne((pos, sym) -> sym.equals("S"));
        var pipes = CONNECTIONS.keySet();
        for (var pipe : pipes) {
            var path = findLoop(grid.put(start.pos(), pipe), new Cell<>(start.pos(), pipe));
            if (!path.isEmpty()) {
                var loop_size = path.size();
                return (loop_size - 1) % 2 == 0
                        ? loop_size / 2
                        : loop_size / 2 + 1;
            }
        }
        throw new RuntimeException("Sorry, you didn't find the loop!");
    }

    private SearchPath<Cell<String>> findLoop(Grid<String> grid,
                                              Cell<String> start
                                             ) {
        return DepthSearch1.fromStates(getSuccessors(grid))
                           .findFirst(start, path -> path.size() > 3 && path.last().state().pos()
                                                                            .equals(path.first()
                                                                                        .state()
                                                                                        .pos()));


    }

    private Function<SearchPath<Cell<String>>, List<Cell<String>>> getSuccessors(Grid<String> grid) {

        return path -> {

            List<Cell<String>> successors = new ArrayList<>();

            var pos = path.last().state().pos();
            var symbol = path.last().state().value();
            var connections = CONNECTIONS.get(symbol);

            var up = pos.up();
            var upVal = grid.getVal(up);
            var upCell = new Cell<>(up, upVal);
            if (grid.containsPos(up) && !upVal.equals(".") && connections.contains(SOUTH) && CONNECTIONS.get(upVal).contains(NORTH)) {
                if (!path.containsState(upCell) || (path.size() > 2 && path.first().state().equals(upCell)))
                    successors.add(upCell);
            }

            var down = pos.down();
            var downVal = grid.getVal(down);
            var downCell = new Cell<>(down, downVal);
            if (grid.containsPos(down) && !downVal.equals(".") && connections.contains(NORTH) && CONNECTIONS.get(downVal).contains(SOUTH))
                if (!path.containsState(downCell) || (path.size() > 2 && path.first().state().equals(downCell)))
                    successors.add(downCell);

            var right = pos.right();
            var rightVal = grid.getVal(right);
            var righCell = new Cell<>(right, rightVal);

            if (grid.containsPos(right) && !rightVal.equals(".") && connections.contains(EAST) && CONNECTIONS.get(rightVal).contains(WEST))
                if (!path.containsState(righCell) || (path.size() > 2 && path.first().state().equals(righCell)))
                    successors.add(righCell);

            var left = pos.left();
            var leftVal = grid.getVal(left);
            var leftCell = new Cell<>(left, leftVal);

            if (grid.containsPos(left) && !leftVal.equals(".") && connections.contains(WEST) && CONNECTIONS.get(leftVal).contains(EAST))
                if (!path.containsState(leftCell) || (path.size() > 2 && path.first().state().equals(leftCell)))
                    successors.add(leftCell);
            return successors;
        };

    }


    @Override
    public Object solveSecond() throws Exception {
        //var grid = MutableGrid.fromFile(getInputPath());
//        Grid<String> grid = MutableGrid.fromString(input2);
//        var start = grid.findOne((pos, sym) -> sym.equals("S"));
//        var pipes = CONNECTIONS.keySet();
//        var vertex = new ArrayList<Pos>();
//        for (var pipe : pipes) {
//            var path = findLoop(grid.put(start.pos(), pipe), new Cell<>(start.pos(), pipe));
//            if (!path.isEmpty()) {
//                return null;
//            }
//        }

        throw new RuntimeException("Sorry, you didn't find the loop!");


    }

    @Override
    public String name() {
        return "Pipe Maze";
    }

    @Override
    public int day() {
        return 10;
    }

    @Override
    public String outputUnitsPart1() {
        return "steps";
    }

    @Override
    public String outputUnitsPart2() {
        return null;
    }

}
