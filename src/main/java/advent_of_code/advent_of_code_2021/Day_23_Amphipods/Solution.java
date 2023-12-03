package advent_of_code.advent_of_code_2021.Day_23_Amphipods;

import search.BranchBoundSearch;
import search.SearchPathCost;
import types.*;
import types.decorators.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Solution {

    public record Amphipod(String type,
                           int energy,
                           List<Pos> home,
                           int xHome
    ) {}

    // sorted by amphipod letter (A -> 1, B -> 10 and so on)
    static List<Integer> energy = List.of(1, 10, 100, 1000);

    Grid<String> burrow;
    // amphipods can't stop at these positions
    List<Pos> outsideRooms;
    int yHallway;
    Range xHallway;
    Map<String, Amphipod> map;

    Function<Grid<String>, List<Pos>> getAmphipods =
            grid -> grid.getCells()
                        .stream()
                        .filter(e -> Character.isAlphabetic(e.value().charAt(0)))
                        .map(Cell::pos)
                        .toList();





    public Solution(Grid<String> burrow) {

        this.burrow = burrow;

        List<Pos> hallway = burrow.getCellsSet()
                                  .stream()
                                  .filter(e -> e.value().equals("."))
                                  .map(it -> it.pos())
                                  .sorted(Comparator.comparingInt(p -> p.x()))
                                  .collect(Collectors.toList());

        yHallway = hallway.get(0).y();
        xHallway = new Range(hallway.get(0).x(),
                             ListFun.last(hallway).x()
        );

        var amphipods = burrow.getCellsSet()
                              .stream()
                              .filter(e -> Character.isAlphabetic(e.value().charAt(0)))
                              .map(it -> it.value())
                              .distinct()
                              .sorted(Comparator.naturalOrder())
                              .toList();

        var rooms = burrow.getCellsSet()
                          .stream()
                          .filter(e -> amphipods.contains(e.value()))
                          .map(e -> e.pos())
                          .sorted(Comparator.<Pos>comparingInt(p -> p.x()).thenComparingInt(p -> p.y()))
                          .collect(Collectors.groupingBy(e -> e.x()))
                          .entrySet()
                          .stream()
                          .sorted(Comparator.comparingInt(e -> e.getKey()))
                          .map(e -> e.getValue())
                          .toList();

        outsideRooms = rooms.stream().map(it -> new Pos(it.get(0).x(), yHallway)).collect(Collectors.toList());

        map = IntStream.range(0, amphipods.size())
                       .boxed()
                       .collect(Collectors.toMap(amphipods::get,
                                                 i -> new Amphipod(amphipods.get(i),
                                                                   energy.get(i),
                                                                   rooms.get(i),
                                                                   rooms.get(i).get(0).x()
                                                 )
                                                )
                               );
    }


    public SearchPathCost<Grid<String>> solve() {


        return BranchBoundSearch.fromStates(getSucc,
                                            (a, b) -> costFun.apply(a, b)
                                           ).findFirst(burrow,
                                                 s->isGoal.apply(s)
                                                      );
    }

    BiFunction<Pos, Pos, Set<Pos>> lineBetween = (a, b) ->
    {

        if (a.equals(b)) return Set.of(a);

        int xmin = Math.min(a.x(), b.x());
        int xmax = Math.max(a.x(), b.x());

        if (a.y() == yHallway && b.y() == yHallway)
            return new Range(xmin, xmax)
                    .stream()
                    .mapToObj(i -> new Pos(i, yHallway))
                    .collect(Collectors.toSet());

        if (a.x() == b.x())
            return new Range(Math.min(a.y(), b.y()), Math.max(a.y(), b.y()))
                    .stream()
                    .mapToObj(i -> new Pos(a.x(), i))
                    .collect(Collectors.toSet());


        return Stream.of(new Range(xmin, xmax).stream().mapToObj(i -> new Pos(i, yHallway)),
                         new Range(yHallway, a.y()).stream().mapToObj(i -> new Pos(a.x(), i)),
                         new Range(yHallway, b.y()).stream().mapToObj(i -> new Pos(b.x(), i))
                        )
                     .flatMap(n -> n)
                     .collect(Collectors.toSet());

    };


    BiFunction<Grid<String>, Grid<String>, Integer> costFun =
            (a, b) -> {
                List<Pos> amphipodsA = getAmphipods.apply(a).stream().toList();

                List<Pos> amphipodsB = getAmphipods.apply(b).stream().toList();

                var finalPos = amphipodsB.stream()
                                         .filter(o -> !amphipodsA.contains(o))
                                         .findFirst()
                                         .get();
                var sourcePos = amphipodsA.stream()
                                          .filter(o -> !amphipodsB.contains(o))
                                          .findFirst()
                                          .get();

                // -1 to leave out source
                return (lineBetween.apply(sourcePos, finalPos).size() - 1) * map.get(a.getVal(sourcePos)).energy;

            };


    BiFunction<Grid<String>, Pos,Boolean> isPlacedAtHome =
            (grid, pos) -> {
                var amphipod = map.get(grid.getVal(pos));
                var homeBottom = ListFun.last(amphipod.home);
                return amphipod.home.contains(pos)
                        && lineBetween.apply(pos, homeBottom)
                                      .stream()
                                      .allMatch(p -> amphipod.type.equals(grid.getVal(p)));
            };



    Function<Grid<String>, List<Pos>> getOutOfPlace =
            grid -> getAmphipods.apply(grid)
                                .stream()
                                .filter(it -> !isPlacedAtHome.apply(grid, it))
                                .collect(Collectors.toList());



    BiFunction<Grid<String>, Pos, Optional<Pos>> moveStraightHome =
            (grid, pos) -> {
                var amphipod = map.get(grid.getVal(pos));
                boolean otherTypeAtHome =
                        amphipod.home.stream()
                                     .anyMatch(p ->
                                                       !grid.getVal(p).equals(".")
                                                               && !amphipod.type.equals(grid.getVal(p))
                                              );

                return otherTypeAtHome ?
                        Optional.empty() :
                        Stream.iterate(amphipod.home.size() - 1,
                                       i -> i >= 0,
                                       i -> i - 1
                                      )
                              .map(amphipod.home::get)
                              .filter(homePos ->
                                              SetFun.remove(lineBetween.apply(homePos, pos), pos)
                                                    .stream()
                                                    .allMatch(it -> ".".equals(grid.getVal(it))))
                              .findFirst();
            };



    BiFunction<Grid<String>, Pos, List<Pos>> getMovesToHallway =
            (grid, pos) ->
                    xHallway.stream()
                            .mapToObj(x -> new Pos(x, yHallway))
                            .filter(p -> !outsideRooms.contains(p))
                            .filter(p ->
                                            SetFun.remove(lineBetween.apply(p, pos),
                                                          pos
                                                         )
                                                  .stream()
                                                  .allMatch(it -> ".".equals(grid.getVal(it)))
                                   )
                            .toList();




    Function<Grid<String>, List<Grid<String>>> getSucc  = grid ->
        {
            List<Pos> outPlace = getOutOfPlace.apply(grid);
            for (Pos pos : outPlace) {
                var optPos = moveStraightHome.apply(grid, pos);
                if (optPos.isPresent()) return List.of(grid.copy().put(optPos.get(),
                                                                       grid.getVal(pos))
                                                           .put(pos, ".")
                                                      );
            }
            return outPlace.stream()
                           .filter(p -> p.y() != yHallway)
                           .flatMap(pos ->
                                            getMovesToHallway.apply(grid, pos)
                                                             .stream()
                                                             .map(newPos -> grid.copy()
                                                                                .put(newPos, grid.getVal(pos))
                                                                                .put(pos, ".")
                                                                 )
                                   )
                           .toList();
        };




    ToIntFunction<Grid<String>> h1 = grid ->
            (int) getAmphipods.apply(grid)
                              .stream()
                              .filter(pos -> !isPlacedAtHome.apply(grid, pos))
                              .count();

    Function<Grid<String>, Integer> h2 = grid ->
            (int) getAmphipods.apply(grid)
                              .stream()
                              .filter(pos -> !isPlacedAtHome.apply(grid, pos))
                              .map(i -> map.get(grid.getVal(i)).energy)
                              .reduce(0, Integer::sum);

    Function<Grid<String>,Boolean> isGoal = grid -> getOutOfPlace.apply(grid).isEmpty();



    public static void main(String[] args) {
        //add to set is slow
        var burrow = MutableGrid.fromFile("/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2021/Day_23_Amphipods/input.txt");
        Solution solution = new Solution(burrow);

        var r = new Timing(() -> {
            var s = solution.solve();
            System.out.println(s.cost());
            return s;
        }, 1);
        r.get();




    }
}
