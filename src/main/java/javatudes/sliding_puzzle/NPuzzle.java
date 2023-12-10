package javatudes.sliding_puzzle;

import fun.tuple.Pair;
import search.Action;
import types.Grid;
import types.ListFun;
import types.PersistentGrid;
import types.Pos;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NPuzzle {

    private static final BiFunction<Long, List<Long>, Long> countNumberInversions =
            (n, seq) -> seq.stream().filter(i -> i < n).count();
    private static final Function<String, Grid<String>> parseBoard = str -> {
        List<List<String>> rows = Arrays.stream(str.split("\\|"))
                                        .toList()
                                        .stream()
                                        .map(row -> Arrays.stream(row.split(" "))
                                                          .toList())
                                        .collect(Collectors.toList());

        return PersistentGrid.fromRows(rows);

    };
    public final Grid<String> board;

    public NPuzzle(String str) {
        this.board = parseBoard.apply(str);
        if (!isSolvable()) {
            throw new IllegalArgumentException("puzzle is not solvable!");
        }
    }

    /**
     * In general, for a given grid of width N, we can find out check if a N*N – 1 puzzle is solvable or not by
     * following below simple rules : If N is odd, then puzzle instance is solvable if number of inversions is even in
     * the input state. If N is even, puzzle instance is solvable if the blank is on an even row counting from the
     * bottom (second-last, fourth-last, etc.) and number of inversions is odd. the blank is on an odd row counting from
     * the bottom (last, third-last, fifth-last, etc.) and number of inversions is even. For all other cases, the puzzle
     * instance is not solvable.
     */
    private boolean isSolvable() {
        var xs = board.getRows((pos, val) -> Long.parseLong(val),
                               (pos, val) -> !val.equals("X")
                              )
                      .stream()
                      .flatMap(Collection::stream)
                      .collect(Collectors.toList());
        var n_inversions = countAllInversions.apply(xs);
        int nRows = board.getRows().size();
        var N = nRows * board.getColumns().size();
        if (N % 2 == 0) {
            var spaceRow = getEmptySquare(board).y();
            return (nRows - spaceRow) % 2 == 0 ? n_inversions % 2 == 1 : n_inversions % 2 == 0;
        } else return n_inversions % 2 == 0;
    }

    private Pos getEmptySquare(Grid<String> p_board) {
        return p_board.findOne((pos, val) -> val.equals("X")).pos();
    }

    public List<Action<Grid<String>>> getSuccessors(Grid<String> p_board) {
        var emptyPos = getEmptySquare(p_board);

        return Stream.of(Pair.of("DOWN", emptyPos.up()),
                         Pair.of("UP", emptyPos.down()),
                         Pair.of("RIGHT", emptyPos.right()),
                         Pair.of("LEFT", emptyPos.left())
                        )
                     .filter(pair -> p_board.getVal(pair.second()) != null)
                     .map(pair -> new Action<>(pair.first(),
                                               p_board.copy()
                                                      .put(pair.second(),
                                                           "X"
                                                          )
                                                      .put(emptyPos,
                                                           p_board.getVal(pair.second())
                                                          )
                          )
                         )
                     .toList();

    }

    private static final Function<List<Long>, Long> countAllInversions =
            seq -> {
                if (seq.isEmpty()) return 0L;
                var head = seq.getFirst();
                var tail = ListFun.tail(seq);
                return countNumberInversions.apply(head, tail) + NPuzzle.countAllInversions.apply(tail);
            };


}
