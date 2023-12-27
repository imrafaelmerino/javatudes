package advent_of_code._2023.day23;

import advent_of_code._2023._2023_Puzzle;
import fun.tuple.Pair;
import types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class ALongWalk implements _2023_Puzzle {


    private static HashMap<Pos, List<Pair<Pos, Integer>>> getCompactedGraph(Grid<String> grid, Pos start, Pos end) {
        var xs = grid.find((pos, val) -> !val.equals("#") && grid.getVHNeighbors(pos)
                                                                 .stream()
                                                                 .filter(it -> !grid.getVal(it).equals("#"))
                                                                 .count() > 2)
                     .stream()
                     .map(Cell::pos)
                     .collect(Collectors.toList());
        ListFun.prepend(xs, start);
        ListFun.append(xs, end);

        var graph = new HashMap<Pos, List<Pair<Pos, Integer>>>();

        for (Pos x : xs) {
            var frontier = new ArrayList<List<Pos>>();
            frontier.add(ListFun.append(new ArrayList<>(), x));
            var seen = new HashSet<>();
            while (!frontier.isEmpty()) {
                var path = frontier.remove(0);
                Pos last = path.getLast();
                if (!last.equals(x) && xs.contains(last)) {
                    graph.compute(path.getFirst(), (pos, list) -> {
                        var p = Pair.of(path.getLast(),
                                        path.size() - 1);
                        return list == null ?
                                ListFun.append(new ArrayList<>(), p) :
                                list.contains(p) ? list : ListFun.append(list, p);
                    });
                    graph.compute(path.getLast(), (pos, list) -> {
                        var p = Pair.of(path.getFirst(),
                                        path.size() - 1);
                        return list == null ?
                                ListFun.append(new ArrayList<>(), p) :
                                list.contains(p) ? list : ListFun.append(list, p);
                    });

                } else {
                    grid.getVHNeighbors(last)
                        .stream()
                        .filter(it -> !seen.contains(it) && !grid.getVal(it).equals("#"))
                        .forEach(pos -> frontier.add(0, ListFun.append(new ArrayList<>(path), pos)));
                }

                seen.add(last);
            }

        }

        return graph;
    }

    @Override
    public Object solveFirst() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath());
        var start = grid.getBottomBorder().stream().filter(it -> it.value().equals(".")).findFirst().get().pos();
        var end = grid.getTopBorder().stream().filter(it -> it.value().equals(".")).findFirst().get().pos();

        var frontier = new ArrayList<List<Pos>>();
        frontier.add(ListFun.append(new ArrayList<>(),
                                    start,
                                    start.up()
                                   )
                    );

        var solutions = new ArrayList<Integer>();

        while (!frontier.isEmpty()) {
            var currentPath = frontier.remove(0);
            var lastPos = currentPath.getLast();
            if (!grid.containsPos(lastPos)) continue;
            if (lastPos.equals(end)) {
                solutions.add(currentPath.size() - 1); //start doent count
                continue;
            }
            if (currentPath.size() > 2 && currentPath.get(currentPath.size() - 3).equals(lastPos))
                continue; //can't go back
            var lastSymbol = grid.getVal(lastPos);
            if (lastSymbol.equals("#")) continue;
            switch (lastSymbol) {
                case ">" -> frontier.add(ListFun.append(new ArrayList<>(currentPath),
                                                        lastPos.right()));
                case "<" -> frontier.add(ListFun.append(new ArrayList<>(currentPath),
                                                        lastPos.left()));
                case "v" -> frontier.add(ListFun.append(new ArrayList<>(currentPath),
                                                        lastPos.up()));
                case "^" -> frontier.add(ListFun.append(new ArrayList<>(currentPath),
                                                        lastPos.down()));
                case "." -> lastPos.neighborsVH()
                                   .forEach(pos -> frontier.add(ListFun.append(new ArrayList<>(currentPath),
                                                                               pos)));
            }

        }

        solutions.sort(Long::compare);

        return solutions.getLast();

    }

    @Override
    public Object solveSecond() throws Exception {
        Grid<String> grid = MutableGrid.fromFile(getInputPath());


        var start = grid.getBottomBorder()
                        .stream()
                        .filter(it -> it.value().equals("."))
                        .findFirst()
                        .get()
                        .pos();

        var end = grid.getTopBorder()
                      .stream()
                      .filter(it -> it.value().equals("."))
                      .findFirst()
                      .get()
                      .pos();

        var graph = getCompactedGraph(grid, start, end);

        var frontier = new ArrayList<List<Pos>>();
        frontier.add(ListFun.append(new ArrayList<>(),
                                    start
                                   )
                    );

        var solutions = new ArrayList<Integer>();

        while (!frontier.isEmpty()) {
            var currentPath = frontier.remove(0);

            var lastPos = currentPath.getLast();
            if (lastPos.equals(end)) {
                solutions.add(getCost(graph, currentPath)); //start doent count
            } else {
                List<Pair<Pos, Integer>> neighbors = graph.get(lastPos)
                                                          .stream()
                                                          .filter(it -> !currentPath.contains(it.first()))
                                                          .toList();
                neighbors
                        .forEach(pair -> frontier.add(0, ListFun.append(new ArrayList<>(currentPath),
                                                                        pair.first()))
                                );
            }
        }

        solutions.sort(Integer::compareTo);
        return solutions.getLast();

    }

    private Integer getCost(HashMap<Pos, List<Pair<Pos, Integer>>> graph, List<Pos> currentPath) {
        int cost = 0;
        for (int i = 0; i < currentPath.size() - 1; i++) {
            var start = currentPath.get(i);
            var end = currentPath.get(i + 1);
            cost += (graph.get(start).stream().filter(it -> it.first().equals(end)).findFirst().get().second());

        }

        return cost;
    }

    @Override
    public String name() {
        return "A Long Walk";
    }

    @Override
    public int day() {
        return 23;
    }

    @Override
    public String outputUnitsPart1() {
        return "steps";
    }

    @Override
    public String outputUnitsPart2() {
        return "steps";
    }

}
