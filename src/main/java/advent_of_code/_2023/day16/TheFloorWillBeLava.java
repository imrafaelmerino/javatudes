package advent_of_code._2023.day16;

import advent_of_code._2023._2023_Puzzle;
import types.ListFun;
import types.MutableGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.List;


public class TheFloorWillBeLava implements _2023_Puzzle {
    @Override
    public Object solveFirst() throws Exception {
        return getResult(MutableGrid.fromFile(getInputPath()), new State(new Pos(0, 0), Dir.E));
    }

    private long getResult(MutableGrid<String> grid, State start) {
        var visited = new ArrayList<State>();
        var frontier = ListFun.prepend(new ArrayList<>(),
                                       start);
        while (!frontier.isEmpty()) {
            var next = frontier.remove(0);
            if (!visited.contains(next)) {
                var successors = getSuccessors(next, grid);
                for (var successor : successors) if (grid.containsPos(successor.pos)) frontier.add(successor);
                visited.add(next);
            }
        }


        return visited.stream()
                      .map(it -> it.pos)
                      .distinct()
                      .count();
    }

    private List<State> getSuccessors(State state,
                                      MutableGrid<String> grid
                                     ) {
        var states = new ArrayList<State>();
        var pos = state.pos;
        var symbol = grid.getVal(pos);
        var dir = state.dir;
        switch (symbol) {
            case "." -> {
                switch (dir) {
                    case S -> states.add(new State(pos.up(), Dir.S));
                    case N -> states.add(new State(pos.down(), Dir.N));
                    case E -> states.add(new State(pos.right(), Dir.E));
                    case W -> states.add(new State(pos.left(), Dir.W));
                }
            }
            case "/" -> {
                switch (dir) {
                    case S -> states.add(new State(pos.left(), Dir.W));
                    case N -> states.add(new State(pos.right(), Dir.E));
                    case E -> states.add(new State(pos.down(), Dir.N));
                    case W -> states.add(new State(pos.up(), Dir.S));
                }
            }
            case "\\" -> {
                switch (dir) {
                    case S -> states.add(new State(pos.right(), Dir.E));
                    case N -> states.add(new State(pos.left(), Dir.W));
                    case E -> states.add(new State(pos.up(), Dir.S));
                    case W -> states.add(new State(pos.down(), Dir.N));
                }
            }
            case "-" -> {
                switch (dir) {
                    case S, N -> {
                        states.add(new State(pos.right(), Dir.E));
                        states.add(new State(pos.left(), Dir.W));
                    }
                    case E -> states.add(new State(pos.right(), Dir.E));
                    case W -> states.add(new State(pos.left(), Dir.W));
                }
            }
            case "|" -> {
                switch (dir) {
                    case E, W -> {
                        states.add(new State(pos.up(), Dir.S));
                        states.add(new State(pos.down(), Dir.N));
                    }
                    case N -> states.add(new State(pos.down(), Dir.N));
                    case S -> states.add(new State(pos.up(), Dir.S));
                }
            }
        }


        return states;
    }

    @Override
    public Object solveSecond() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath());
        var bottom = grid.getBottomBorder().stream().map(cell -> new State(cell.pos(), Dir.S)).toList();
        var top = grid.getTopBorder().stream().map(cell -> new State(cell.pos(), Dir.N)).toList();
        var left = grid.getLeftBorder().stream().map(cell -> new State(cell.pos(), Dir.E)).toList();
        var right = grid.getRightBorder().stream().map(cell -> new State(cell.pos(), Dir.W)).toList();
        var starts = ListFun.appendAll(new ArrayList<>(),bottom,top,left,right);

        var max = 0L;
        for(var start:starts){
            var result = getResult(grid,start);
            if(result > max) max = result;
        }
        return max;

    }

    @Override
    public String name() {
        return "The Floor Will Be Lava";
    }

    @Override
    public int day() {
        return 16;
    }

    @Override
    public String outputUnitsPart1() {
        return "tiles become energized";
    }

    @Override
    public String outputUnitsPart2() {
        return "is maximum number of tiles that become energized";
    }

    enum Dir {N, S, E, W}

    record State(Pos pos, Dir dir) {
    }
}
