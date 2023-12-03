package javatudes;

import search.Action;
import search.BreathSearch;
import search.SearchPath;
import types.*;

import java.util.List;
import java.util.function.*;

public class Bloxorz {


    public static final String ORANGE_TILE = "*";
    public static final String START = "#";
    public static final String GOAL = "!";
    public static final String SPACE = " ";

    /**
     *
     * @param _1 lower x or y point
     * @param _2 upper x or y point
     */
    record State(Pos _1, Pos _2) {}

    static BiPredicate<Grid<String>, State> isInBoard =
            (board, state) ->
                    board.getVal(state._1) != null
                            && !board.getVal(state._1).equals(SPACE)
                            && board.getVal(state._2) != null
                            && !board.getVal(state._2).equals(SPACE);

    static BiPredicate<Grid<String>, State> isVerticalOnOrangeTile = (board, state) ->
            state._1.equals(state._2) && board.getVal(state._1).equals(ORANGE_TILE);

    static BiPredicate<Grid<String>, State> isGameOver = isInBoard.negate().or(isVerticalOnOrangeTile);

    static Function<Grid<String>, Function<State, List<Action<State>>>> succ = board -> s -> {
        if (isGameOver.test(board, s)) return List.of();
        if (s._1.equals(s._2))
            return List.of(
                    new Action<>("RIGHT", new State(s._1.right(), s._1.right().right())),
                    new Action<>("LEFT", new State(s._1.left().left(), s._1.left())),
                    new Action<>("UP", new State(s._1.up(), s._1.up().up())),
                    new Action<>("DOWN", new State(s._1.down().down(), s._1.down()))
                          );
        if (s._1.x() == s._2.x())
            return List.of(
                    new Action<>("RIGHT", new State(s._1.right(), s._2.right())),
                    new Action<>("LEFT", new State(s._1.left(), s._2.left())),
                    new Action<>("UP", new State(s._2.up(), s._2.up())),
                    new Action<>("DOWN", new State(s._1.down(), s._1.down()))
                          );

        if (s._1.y() == s._2.y())
            return List.of(
                    new Action<>("RIGHT", new State(s._2.right(), s._2.right())),
                    new Action<>("LEFT", new State(s._1.left(), s._1.left())),
                    new Action<>("UP", new State(s._1.up(), s._2.up())),
                    new Action<>("DOWN", new State(s._1.down(), s._2.down()))
                          );

        assert false;

        return null;

    };

    static BiConsumer<Grid<String>, SearchPath<State>> print = (board, sp) -> {

        sp.stream()
          .forEach(action ->
                   {
                       System.out.println(action.name());
                       System.out.println(action.state());
                       var updated =
                               board
                                    .color(
                                           ControlChar.BLUE,
                                           action.state()._1,
                                           action.state()._2
                                          );
                       updated.printRows();
                       System.out.println("\n");
                   }
                  );
    };

    static Function<Grid<String>, SearchPath<State>> solve = board -> {

        var startPos = board.filter((pos,val) -> val.equals(START)).getPositions();
        var startStare = new State(startPos.get(0),
                                   startPos.size() == 1 ? startPos.get(0) : startPos.get(1)
        );
        var goalPos = board.filter((pos,val) -> val.equals(GOAL)).getPositions().get(0);
        var goalState = new State(goalPos, goalPos);
        Predicate<State> isGoal = s -> s.equals(goalState);

        return BreathSearch.fromActions(succ.apply(board))
                           .findFirst(startStare, isGoal);

    };


    public static void main(String[] args) {

        var puzzles = List.of("""
                ......    ...!..
                ..#...    ......
                ................""",
                """
                        ...
                        .#....
                        .........
                         .........
                             ..!..
                              ...

                        """,
                """
                              ....  ...
                        ....  ....  .!.
                        ..#.  ....  ...
                        ....  ....  ...
                        ...............
                        ....  ....""",
                """
                              .......
                        ....  ...  ..
                        .........  ....
                        .#..       ..!.
                        ....       ....
                                    ...""",
                """
                           *******
                           *******
                        ....     ...
                        ...       ..
                        ...       ..
                        .#.   ....*****
                        ...   ....*****
                              .!.  **.*
                              ...  ****"""
                );

        puzzles.forEach(str ->{
                                     var board = PersistentGrid.fromString(str);
                                     print.accept(board, solve.apply(board));
                                     System.out.println("------------------------------------------------------");
                                 }
                       );





    }
}
