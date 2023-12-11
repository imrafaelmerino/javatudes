package advent_of_code._2023.day11;

import advent_of_code._2023._2023_Puzzle;
import types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CosmicExpansion implements _2023_Puzzle {
    public static final String EMPTY = ".";
    public static final String GALAXY = "#";

    static String input1 = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;

    @Override
    public Object solveFirst() throws Exception {
        return solve(1);
    }

    private long solve(int factor) {
        Grid<String> grid = PersistentGrid.fromFile(getInputPath());

        var rows = grid.getRows();
        var columns = grid.getColumns();
        var rowIndexes = new ArrayList<Integer>();
        for (int i = 0; i < rows.size(); i++)
            if (rows.get(i).stream().allMatch(c -> c.value().equals(EMPTY))) rowIndexes.add(i);

        var columIndexes = new ArrayList<Integer>();
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).stream().allMatch(c -> c.value().equals(EMPTY))) columIndexes.add(i);


        var expanded = expandRows(expandColumns(grid,
                                                columIndexes,
                                                factor),
                                  rowIndexes, factor);


        var galaxies = expanded.find((pos, sym) -> sym.equals(GALAXY)).stream()
                               .map(Cell::pos)
                               .collect(Collectors.toList());


        return distances(0, galaxies);
    }

    long distances(long distances, List<Pos> galaxies) {
        if (galaxies.size() == 1) return distances;
        Pos remove = galaxies.getFirst();
        return distances(distances + distances(remove,
                                               ListFun.tail(galaxies)),
                         ListFun.tail(galaxies));
    }

    long distances(Pos galaxy, List<Pos> rest) {
        var distance = 0L;
        for (Pos pos : rest) {
            var manhattanDistance = galaxy.manhattanDistance(pos);
            distance = distance + manhattanDistance;
        }

        return distance;
    }


    Grid<String> expandColumns(Grid<String> grid, List<Integer> indexes, int factor) {
        if (indexes.isEmpty()) return grid;
        var index = indexes.remove(0);

        var expanded = grid.mapPos((pos, s) -> {
            if (pos.x() >= index) return new Pos(pos.x() + factor, pos.y());
            return pos;
        });
        return expandColumns(expanded,
                             indexes.stream().map(it -> it + factor).collect(Collectors.toList()),
                             factor);
    }

    Grid<String> expandRows(Grid<String> grid, List<Integer> indexes, int factor) {
        if (indexes.isEmpty()) return grid;
        var index = indexes.remove(0);

        var expanded = grid.mapPos((pos, s) -> {
            if (pos.y() >= index) return new Pos(pos.x(), pos.y() + factor);
            return pos;
        });
        return expandRows(expanded,
                          indexes.stream().map(it -> it + factor).collect(Collectors.toList()),
                          factor);
    }


    @Override
    public Object solveSecond() throws Exception {
        return solve(1000_000 - 1);
    }

    @Override
    public String name() {
        return "Cosmic Expansion";
    }

    @Override
    public int day() {
        return 11;
    }

    @Override
    public String outputUnitsPart1() {
        return "sum of the shortest distances";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }
}
