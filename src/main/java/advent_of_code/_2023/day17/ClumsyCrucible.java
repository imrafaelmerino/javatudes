package advent_of_code._2023.day17;

import advent_of_code._2023._2023_Puzzle;
import search.BranchBoundSearch;
import types.ControlChar;
import types.Grid;
import types.MutableGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

enum Dir {N, S, E, W}

public class ClumsyCrucible implements _2023_Puzzle {


    // (0,0), (1,0), (2,0), (2,1), (3,1), (4,1), (5,1), (5,0), (6,0), (7,0) (8,0), (8,1), (8,2), (9,2), (10,2)
    String a = """
            2>>34^>>>1323
            32v>>>35v5623
            32552456v>>54
            3446585845v52
            4546657867v>6
            14385987984v4
            44578769877v6
            36378779796v>
            465496798688v
            456467998645v
            12246868655<v
            25465488877v5
            43226746555v>
            """;

    @Override
    public Object solveFirst() throws Exception {
        var input = """
                2413432311323
                3215453535623
                3255245654254
                3446585845452
                4546657867536
                1438598798454
                4457876987766
                3637877979653
                4654967986887
                4564679986453
                1224686865563
                2546548887735
                4322674655533
                """;
        var grid = MutableGrid.fromFile(getInputPath())
                              .mapValues((pos, val) -> Integer.parseInt(val));

        var goal = new Pos(grid.xmax(),
                           grid.ymax());
        BiFunction<State, State, Integer> stepCost = (a, b) -> grid.getVal(b._4.pos);
        var bb = BranchBoundSearch.fromStates(getSuccessor(grid),
                                              stepCost
                                             );

        var start = new Pos(0, 0);
        var pathCost = bb.findFirst(new State(null,
                                              null,
                                              null,
                                              new Lava(start, Dir.E)),
                                    s -> s._4.pos.equals(goal)
                                   );

        var states = pathCost.path().states().stream().map(it -> it._4.pos).toList();
        //grid.color(ControlChar.RED, (pos, val) -> states.contains(pos)).printRows();

        return pathCost.cost();
    }

    private Function<State, List<State>> getSuccessor(Grid<Integer> grid) {
        return state -> {
            var result = new ArrayList<State>();
            var _1 = state._1;
            var _2 = state._2;
            var _3 = state._3;
            var _4 = state._4;


            if (isSameX(_2, _3, _4)) {
                result.add(new State(_2, _3, _4, new Lava(_4.pos.right(), Dir.E)));
                result.add(new State(_2, _3, _4, new Lava(_4.pos.left(), Dir.W)));
                if (_1 != null && _2.dir != _4.dir) {
                    if (_4.dir == Dir.S) result.add(new State(_2, _3, _4, new Lava(_4.pos.up(), Dir.S)));
                    else result.add(new State(_2, _3, _4, new Lava(_4.pos.down(), Dir.N)));
                }
            } else if (isSameY(_2, _3, _4)) {
                result.add(new State(_2, _3, _4, new Lava(_4.pos.up(), Dir.S)));
                result.add(new State(_2, _3, _4, new Lava(_4.pos.down(), Dir.N)));
                if (_1 != null && _2.dir != _4.dir) {
                    if (_4.dir == Dir.E) result.add(new State(_2, _3, _4, new Lava(_4.pos.right(), Dir.E)));
                    else result.add(new State(_2, _3, _4, new Lava(_4.pos.left(), Dir.W)));
                }
            } else {
                result.add(new State(_2, _3, _4, new Lava(_4.pos.right(), Dir.E)));
                result.add(new State(_2, _3, _4, new Lava(_4.pos.left(), Dir.W)));
                result.add(new State(_2, _3, _4, new Lava(_4.pos.up(), Dir.S)));
                result.add(new State(_2, _3, _4, new Lava(_4.pos.down(), Dir.N)));
            }


            return result.stream()
                         .filter(it ->
                                         grid.containsPos(it._4.pos)
                                       && (_2 == null || !_4.pos.equals(_2.pos)))
                         .toList();
        };


    }

    private boolean isSameX(Lava _1, Lava _2, Lava _3) {
        return _1 != null && _2 != null && _3 != null
               && _1.pos.x() == _2.pos.x()
               && _2.pos.x() == _3.pos.x();
    }

    private boolean isSameY(Lava _1, Lava _2, Lava _3) {
        return _1 != null && _2 != null && _3 != null
               && _1.pos.y() == _2.pos.y()
               && _2.pos.y() == _3.pos.y();
    }

    @Override
    public Object solveSecond() throws Exception {
        return null;
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
        return null;
    }

    /**
     * we need to distinguish  v>>> from >>>> for that we'll use the
     */

    record Lava(Pos pos, Dir dir) {
    }

    record State(Lava _1, Lava _2, Lava _3, Lava _4) {


    }
}
