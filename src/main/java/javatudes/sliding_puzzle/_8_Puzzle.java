package javatudes.sliding_puzzle;

import search.BreathSearch;

public class _8_Puzzle {


    public static void main(String[] args) {


        var GOAL = new NPuzzle("1 2 3|4 5 6|7 8 X");

        var puzzle = new NPuzzle("1 3 2|5 4 6|7 8 X");

        puzzle.board.printRows();

        var sol =
                BreathSearch.fromActions(puzzle::getSuccessors)
                            .findFirst(puzzle.board,
                                       it -> it.equals(GOAL.board)
                                      );

        sol.stream()
           .forEach(action -> {
               System.out.println(action.name());
               action.state().printRows();
               System.out.println("\n");
           });
    }
}
