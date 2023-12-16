package advent_of_code._2023.day13;

import advent_of_code._2023._2023_Puzzle;
import types.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * very slow optimize
 */
public class PointOfIncidence implements _2023_Puzzle {

    static Function<Integer, DoubleStream> mirrors =
            max -> DoubleStream.iterate(0.5,
                                        a -> a <= max,
                                        a -> a + 1);

    private int summarize(LineOfReflection it) {
        int n = (int) it.pos + 1;
        return it.line == LINE.V ? n : 100 * n;
    }

    @Override
    public Integer solveFirst() throws Exception {
        //var patterns = FileParsers.toGroupsOfLines(getInputPath());
        var patterns = FileParsers.toGroupsOfLines(getInputPath()).
                                  stream()
                                  .map(MutableGrid::fromLines)
                                  .toList();

        return patterns.stream()
                       .flatMap(grid -> find(grid).stream())
                       .map(this::summarize)
                       .reduce(0, Integer::sum);

    }

    List<LineOfReflection> find(Grid<String> grid) {
        var vertical = findVertical(grid);
        var lor = findVertical(grid.transpose());
        var horizontal = lor.stream()
                            .map(it -> new LineOfReflection(it.pos, LINE.H))
                            .collect(Collectors.toList());
        vertical.addAll(horizontal);
        return vertical;
    }

    List<LineOfReflection> findVertical(Grid<String> grid) {
        var candidates = mirrors.apply(grid.xmax());

        return candidates.mapToObj(candidate -> {
                             var lefts = grid.xs().stream().filter(x -> x < candidate).toList();
                             var rights = grid.xs().stream().filter(x -> x > candidate).toList();
                             var zip = ListFun.zip(lefts.reversed(), rights);
                             var allEquals = zip.stream()
                                                .allMatch(pair -> {
                                                    var columns = grid.getColumns();
                                                    var leftCells = columns.get(pair.first());
                                                    var rightCells = columns.get(pair.second());
                                                    return leftCells.stream()
                                                                    .map(Cell::value)
                                                                    .toList()
                                                                    .equals(rightCells.stream()
                                                                                      .map(Cell::value)
                                                                                      .toList());
                                                });
                             if (allEquals) return new LineOfReflection(candidate, LINE.V);
                             return null;
                         })
                         .filter(Objects::nonNull)
                         .collect(Collectors.toList());


    }

    @Override
    public Object solveSecond() throws Exception {
        var patterns = FileParsers.toGroupsOfLines(getInputPath())
                                  .stream()
                                  .map(PersistentGrid::fromLines)
                                  .toList();

        return patterns.stream()
                       .map(grid -> {
                           var lor = find(grid).get(0);
                           for (var cell : grid) {
                               var ys = grid.put(cell.pos(),
                                                 cell.value()
                                                     .equals("#") ? "." : "#");
                               var lor1 = find(ys);
                               if (lor1 != null) {
                                   var a = lor1.stream().filter(it -> !it.equals(lor)).findFirst();
                                   if (a.isPresent()) return summarize(a.get());
                               }
                           }
                           return 0;

                       })
                       .reduce(0, Integer::sum);
    }

    @Override
    public String name() {
        return "Point of Incidence";
    }

    @Override
    public int day() {
        return 13;
    }

    @Override
    public String outputUnitsPart1() {
        return "sum of columns above plus 100 times rows above the line of reflection";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }

    enum LINE {H, V} //horizontal or vertical

    record LineOfReflection(double pos, LINE line) {
    }
}
