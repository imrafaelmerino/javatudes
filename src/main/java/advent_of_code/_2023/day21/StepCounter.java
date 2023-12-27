package advent_of_code._2023.day21;

import advent_of_code._2023._2023_Puzzle;
import types.PersistentGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.HashSet;

public class StepCounter implements _2023_Puzzle {

    private static int getGardens(Pos startPos, PersistentGrid<String> grid, int N) {
        var seen = new HashSet<State>();
        var frontier = new ArrayList<State>();
        frontier.add(new State(startPos, 0));
        int xmax = grid.xmax();
        int ymax = grid.ymax();
        while (!frontier.isEmpty()) {
            var state = frontier.remove(0);
            //System.out.println(state);

            seen.add(state);

            var neighbors = grid.getVHNeighbors(state.pos)
                                .stream()
                                .filter(it -> !grid.getVal(new Pos(it.x() % xmax,
                                                                   it.y() % ymax)
                                                          )
                                                   .equals("#"))
                                .map(it -> new State(it, state.steps + 1))
                                .filter(it -> !seen.contains(it) && it.steps <= N)
                                .toList();
            frontier.addAll(0, neighbors);
        }

        var seenPos = seen.stream()
                          .filter(it -> it.steps == N)
                          .map(it -> it.pos)
                          .toList();


        return seenPos.size();
    }

    @Override
    public Object solveFirst() throws Exception {
        var N = 64;
        var grid = PersistentGrid.fromFile(getInputPath());
        var startPos = grid.find((pos, val) -> val.equals("S"))
                           .getFirst()
                           .pos();


        return getGardens(startPos, grid, N);
    }

    @Override
    public Object solveSecond() throws Exception {
        return null;
    }

    @Override
    public String name() {
        return "Step Counter";
    }

    @Override
    public int day() {
        return 21;
    }

    @Override
    public String outputUnitsPart1() {
        return "garden plots";
    }

    @Override
    public String outputUnitsPart2() {
        return null;
    }

    record State(Pos pos, int steps) {
    }
}
