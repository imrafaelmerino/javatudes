package etudes;


import search.csp.Constraint;
import search.csp.FindFirst;
import types.*;
import java.util.*;

import java.util.stream.Collectors;

public class Sudoku {

    final Grid<String> grid;
    final Map<Pos, List<Pos>> neighbors = new HashMap<>();
    private final List<Pos> variables;
    private final List<Constraint<Pos, String>> constraints = new ArrayList<>();
    private final Map<Pos, List<String>> domain;

    static List<Pos> getRec(int xoffset, int yoffset) {
        List<Pos> rec = new ArrayList<>();
        for (var i = 0; i < 3; i++) for (var j = 0; j < 3; j++) rec.add(new Pos(i + xoffset, j + yoffset));
        return rec;
    }

    static List<List<Pos>> getAllRec() {
        List<List<Pos>> xs = new ArrayList<>();
        for (var i = 0; i < 3; i++) for (var j = 0; j < 3; j++) xs.add(getRec(3 * i, 3 * j));
        return xs;
    }

    public static void main(String[] args) {
        var sudokus = List.of("""
                                      ...26.7.1
                                      68..7..9.
                                      19...45..
                                      82.1...4.
                                      ..46.29..
                                      .5...3.28
                                      ..93...74
                                      .4..5..36
                                      7.3.18...""",
                              """
                                      ..4...3..
                                      ..98.....
                                      38..1..2.
                                      .....6.1.
                                      ..3......
                                      75.4..2..
                                      ....4...5
                                      .9.......
                                      82.5..7..""",
                              """
                                      .2..81...
                                      .5....72.
                                      .7.5...9.
                                      .........
                                      ..63..4..
                                      9....4..8
                                      ....4....
                                      24..95...
                                      ...8.3.5.""",
                              """
                                      1..489..6
                                      73.....4.
                                      .....1295
                                      ..712.6..
                                      5..7.3..8
                                      ..6.957..
                                      9146.....
                                      .2.....37
                                      8..512..4"""
                             );
        sudokus.forEach(str -> {
            var sudoku = new Sudoku(PersistentGrid.fromString(str));
            var ass = sudoku.solve();
            new MutableGrid<>(ass).printRows();
            System.out.println();
        });

    }

    public Sudoku(Grid<String> grid) {
        //todo check dimensions of the grid
        this.grid = grid;

        for (List<Pos> rec : getAllRec())
            for (Pos pos : rec) {

                var sameColumn = grid.getColumns()
                                     .get(pos.x())
                                     .stream()
                                     .map(Cell::pos)
                                     .collect(Collectors.toList());

                constraints.addAll(Constraint.allDifferent(sameColumn));

                var sameRow = grid.getRows()
                                  .get(pos.y())
                                  .stream()
                                  .map(Cell::pos)
                                  .collect(Collectors.toList());

                constraints.addAll(Constraint.allDifferent(sameRow));

                constraints.addAll(Constraint.allDifferent(rec));

                var sameRec =
                        rec.stream()
                           .filter(it -> !sameRow.contains(it) && !sameColumn.contains(it))
                           .collect(Collectors.toList());


                neighbors.put(pos,
                              ListFun.remove(ListFun.appendAll(sameColumn, sameRow, sameRec),
                                             pos
                                            )
                             );

            }

        this.variables = grid.getPositions();

        var values = Arrays.stream("123456789".split("")).toList();

        this.domain = variables.stream().collect(Collectors.toMap(pos -> pos, pos -> values));

        grid.filter((pos,val) -> values.contains(val))
            .forEach(e -> domain.put(e.pos(),
                                     List.of(e.value())
                                    )
                    );

    }

    public Map<Pos, String> solve() {


        //todo definir heuristicas : numero de vecinos, domain con menos valores
        //pasar los assigments
        //return new FindFirst<>(variables, domain, constraints,initialAss).findFirst();
        return new FindFirst<>(variables,
                               domain,
                               constraints).findFirst();

    }


}
