package advent_of_code._2023.day17;

import advent_of_code._2023._2023_Puzzle;
import search.BranchBoundSearch;
import types.Grid;
import types.MutableGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClumsyCrucible implements _2023_Puzzle {


    @Override
    public Object solveFirst() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath())
                              .mapValues((pos, val) -> Integer.parseInt(val));


        BiFunction<State, State, Integer> stepCost = (a, b) -> grid.getVal(b.pos);
        var bb = BranchBoundSearch.fromStates(getSuccessor(grid),
                                              stepCost
                                             );

        var goalPoint = new Pos(grid.xmax(),
                                grid.ymax());
        Predicate<State> goal = s -> s.pos.equals(goalPoint);

        var startPoint = new Pos(0, 0);

        var pathCost = bb.findFirst(new State(startPoint, Dir.E, -1),
                                    goal
                                   );

//        var states = pathCost.path().states().stream().map(it -> it.pos).toList();
//        grid.color(ControlChar.RED, (pos, val) -> states.contains(pos)).printRows();

        return pathCost.cost();
    }


    private Function<State, List<State>> getSuccessor(Grid<Integer> grid) {
        return state -> {

            var result = new ArrayList<State>();

            Pos pos = state.pos;
            Dir dir = state.dir;

            switch (dir) {
                case E -> {
                    result.add(new State(pos.right(), Dir.E, state.times + 1));
                    result.add(new State(pos.up(), Dir.S, 1));
                    result.add(new State(pos.down(), Dir.N, 1));
                }
                case W -> {
                    result.add(new State(pos.left(), Dir.W, state.times + 1));
                    result.add(new State(pos.up(), Dir.S, 1));
                    result.add(new State(pos.down(), Dir.N, 1));
                }
                case N -> {
                    result.add(new State(pos.down(), Dir.N, state.times + 1));
                    result.add(new State(pos.left(), Dir.W, 1));
                    result.add(new State(pos.right(), Dir.E, 1));
                }
                case S -> {
                    result.add(new State(pos.up(), Dir.S, state.times + 1));
                    result.add(new State(pos.left(), Dir.W, 1));
                    result.add(new State(pos.right(), Dir.E, 1));
                }
            }

            return result.stream().filter(s -> grid.containsPos(s.pos) && s.times <= 3).toList();


        };


    }

    private Function<State, List<State>> getSuccessor2(Grid<Integer> grid) {
        return state -> {

            var result = new ArrayList<State>();

            Pos pos = state.pos;
            Dir dir = state.dir;

            switch (dir) {
                case E -> {
                    result.add(new State(pos.right(), Dir.E, state.times + 1));
                    if (state.times >= 4) {
                        result.add(new State(pos.up(), Dir.S, 1));
                        result.add(new State(pos.down(), Dir.N, 1));
                    }
                }
                case W -> {
                    result.add(new State(pos.left(), Dir.W, state.times + 1));
                    if (state.times >= 4) {
                        result.add(new State(pos.up(), Dir.S, 1));
                        result.add(new State(pos.down(), Dir.N, 1));
                    }
                }
                case N -> {
                    result.add(new State(pos.down(), Dir.N, state.times + 1));
                    if (state.times >= 4) {
                        result.add(new State(pos.left(), Dir.W, 1));
                        result.add(new State(pos.right(), Dir.E, 1));
                    }
                }
                case S -> {
                    result.add(new State(pos.up(), Dir.S, state.times + 1));
                    if (state.times >= 4) {
                        result.add(new State(pos.left(), Dir.W, 1));
                        result.add(new State(pos.right(), Dir.E, 1));
                    }
                }
            }

            return result.stream()
                         .filter(s -> grid.containsPos(s.pos) && s.times <= 10)
                         .toList();


        };


    }


    @Override
    public Object solveSecond() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath())
                              .mapValues((pos, val) -> Integer.parseInt(val));


        BiFunction<State, State, Integer> stepCost = (a, b) -> grid.getVal(b.pos);
        var bb = BranchBoundSearch.fromStates(getSuccessor2(grid),
                                              stepCost
                                             );
        var goalPoint = new Pos(grid.xmax(),
                                grid.ymax());
        Predicate<State> goal = s -> s.pos.equals(goalPoint) && s.times >= 4;

        var start = new Pos(0, 0);
        var pathCost = bb.findFirst(new State(start, Dir.S, -1),
                                    goal
                                   );

//        var states = pathCost.path().states().stream().map(it -> it.pos).toList();
//        grid.color(ControlChar.RED, (pos, val) -> states.contains(pos)).printRows();

        return pathCost.cost();
    }

    @Override
    public String name() {
        return "Clumsy Crucible";
    }

    @Override
    public int day() {
        return 17;
    }

    @Override
    public String outputUnitsPart1() {
        return "is the least heat";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }

    enum Dir {N, S, E, W}

    /**
     * @param pos   current position
     * @param dir   last move direction
     * @param times number moves in the same direction (reset to 0 when changing directions)
     */
    record State(Pos pos, Dir dir, int times) {


    }
}
