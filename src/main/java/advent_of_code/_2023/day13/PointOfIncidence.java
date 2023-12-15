package advent_of_code._2023.day13;

import advent_of_code._2023._2023_Puzzle;
import fun.tuple.Pair;
import types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PointOfIncidence implements _2023_Puzzle {
    @Override
    public Integer solveFirst() throws Exception {
        //var patterns = FileParsers.toGroupsOfLines(getInputPath());
        var patterns = FileParsers.toGroupsOfLines(getTestInputPath());
        return patterns.stream()
                       .map(MutableGrid::fromLines)
                       .map(it -> this.getLines(it).get())
                       .reduce(0, Integer::sum);
    }

    private Optional<Integer> getLines(Grid<String> grid) {
        var xs = getAllReflections(grid)
                .stream()
                .filter(pairs -> {
                            return pairs.stream()
                                        .allMatch(it -> {
                                                      var columns = grid.getColumns();
                                                      var colum1 = columns.get(it.first())
                                                                          .stream()
                                                                          .map(Cell::value)
                                                                          .toList();
                                                      var colums2 = columns.get(it.second())
                                                                           .stream()
                                                                           .map(Cell::value)
                                                                           .toList();
                                                      return colum1
                                                              .equals(colums2);
                                                  }
                                                 );
                        }
                       ).findFirst();

        if (xs.isPresent()) {
            return Optional.of(xs.get().stream()
                                 .map(Pair::first)
                                 .max(Integer::compareTo)
                                 .orElse(0)
                               + 1
                              );
        } else {
            var grid1 = grid.transpose();
            xs = getAllReflections(grid1)
                    .stream()
                    .filter(pairs ->
                                    pairs.stream()
                                         .allMatch(it -> {
                                             var columns = grid1.getColumns();
                                             var colum1 = columns.get(it.first())
                                                                 .stream()
                                                                 .map(Cell::value)
                                                                 .toList();
                                             var colums2 = columns.get(it.second())
                                                                  .stream()
                                                                  .map(Cell::value)
                                                                  .toList();
                                             return colum1
                                                     .equals(colums2);
                                         })
                           )
                    .findFirst();
            return xs.map(pairs -> 100 * (pairs.stream()
                                               .map(Pair::first)
                                               .max(Integer::compareTo)
                                               .orElse(0) + 1)
                         );
        }


    }

    public List<List<Pair<Integer, Integer>>> getAllReflections(Grid<String> grid) {
        int middle = grid.xmax() / 2;
        var middlePair = Pair.of(middle, middle + 1);
        ArrayList<List<Pair<Integer, Integer>>> allReflections = new ArrayList<>();
        var otherPairs =
                IntStream.rangeClosed(1, grid.xmax() - middle)
                         .mapToObj(i -> Pair.of(middlePair.first() - i,
                                                middlePair.second() + i)
                                  )
                         .filter(p -> p.second() <= grid.xmax())
                         .collect(Collectors.toList());
        var pairs = ListFun.prepend(otherPairs, middlePair);

        allReflections.add(pairs);


        for (int i = 1; i < grid.xmax() / 2; i++) {
            int finalI = i;
            allReflections.add(pairs.stream()
                                    .map(pair -> Pair.of(pair.first() + finalI,
                                                         pair.second() + finalI)
                                        )
                                    .filter(p -> p.second() <= grid.xmax())
                                    .toList());
        }
        for (int i = 1; i <= grid.xmax() / 2; i++) {
            int finalI = i;
            allReflections.add(pairs.stream()
                                    .map(pair -> Pair.of(pair.first() - finalI,
                                                         pair.second() - finalI)
                                        )
                                    .filter(p -> p.first() >= 0)
                                    .toList());
        }
        return allReflections;
    }

    @Override
    public Object solveSecond() throws Exception {
        var patterns = FileParsers.toGroupsOfLines(getTestInputPath());
        return patterns.stream()
                       .map(PersistentGrid::fromLines)
                       .map(grid -> {
                           var lines = getLines(grid).get();
                           return grid.getCellsSet()
                                      .stream()
                                      .map(cell -> {
                                          var xs = grid.put(cell.pos(),
                                                            cell.value()
                                                                .equals("#") ? "." : "#");
                                          return getLines(xs);

                                      })
                                      .filter(it -> it.isPresent() && !it.get().equals(lines))
                                      .findFirst()
                                      .get().get();
                       })
                       .reduce(0,
                               Integer::sum);
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
}
