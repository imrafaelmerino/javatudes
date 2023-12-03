package advent_of_code.advent_of_code_2022.day2;

import types.FileParsers;

import java.util.List;
import java.util.Optional;

import static advent_of_code.advent_of_code_2022.day2.Day_2.Shape.*;

public class Day_2 {

    public static final int DRAW_SCORE = 3;
    public static final int LOST_SCORE = 0;
    public static final int WIN_SCORE = 6;

    enum Shape {
        ROCK(1), PAPER(2), SCISSORS(3);
        final int score;

        Shape(int score) {
            this.score = score;
        }
    }

    public record Game(Shape theirs, Shape mine) {
    }


    public static void main(String[] args)  {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day2/input_2.txt";

        List<String> lines = FileParsers.toListOfLines(input);

        List<Game> games = lines.stream()
                                .map(line -> line.split("\s"))
                                .map(tokens -> {
                                         Shape theirs = decryptColumn1(tokens[0]);
                                         Shape mine = decryptColumn2Part2(theirs,
                                                                          tokens[1]
                                                                         );
                                         return new Game(theirs,
                                                         mine
                                         );
                                     }
                                    )
                                .toList();

        Optional<Integer> sum = games.stream()
                                     .map(Day_2::getMyScore)
                                     .reduce(Integer::sum);

        System.out.println(sum.get());


    }

    public static int getMyScore(Game game) {
        return switch (game.theirs) {

            case ROCK -> switch (game.mine) {
                case ROCK -> ROCK.score + DRAW_SCORE;
                case PAPER -> PAPER.score + WIN_SCORE;
                case SCISSORS -> SCISSORS.score + LOST_SCORE;
            };

            case PAPER -> switch (game.mine) {
                case ROCK -> ROCK.score + LOST_SCORE;
                case PAPER -> PAPER.score + DRAW_SCORE;
                case SCISSORS -> SCISSORS.score + WIN_SCORE;
            };

            case SCISSORS -> switch (game.mine) {
                case ROCK -> ROCK.score + WIN_SCORE;
                case PAPER -> PAPER.score + LOST_SCORE;
                case SCISSORS -> SCISSORS.score + DRAW_SCORE;
            };
        };


    }

    public static Shape decryptColumn1(String encrypted) {
        if (encrypted.equalsIgnoreCase("a")) return ROCK;
        if (encrypted.equalsIgnoreCase("b")) return Shape.PAPER;
        if (encrypted.equalsIgnoreCase("c")) return Shape.SCISSORS;
        throw new IllegalArgumentException(encrypted);
    }

    public static Shape decryptColumn2Part1(String encrypted) {
        if (encrypted.equalsIgnoreCase("x")) return ROCK;
        if (encrypted.equalsIgnoreCase("y")) return Shape.PAPER;
        if (encrypted.equalsIgnoreCase("z")) return Shape.SCISSORS;
        throw new IllegalArgumentException(encrypted);
    }

    //X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
    public static Shape decryptColumn2Part2(Shape theirHand, String encrypted) {
        switch (theirHand) {
            case ROCK:
                if (encrypted.equalsIgnoreCase("x")) return SCISSORS;
                if (encrypted.equalsIgnoreCase("y")) return ROCK;
                if (encrypted.equalsIgnoreCase("z")) return PAPER;
            case PAPER:
                if (encrypted.equalsIgnoreCase("x")) return ROCK;
                if (encrypted.equalsIgnoreCase("y")) return PAPER;
                if (encrypted.equalsIgnoreCase("z")) return SCISSORS;
            case SCISSORS:
                if (encrypted.equalsIgnoreCase("x")) return PAPER;
                if (encrypted.equalsIgnoreCase("y")) return SCISSORS;
                if (encrypted.equalsIgnoreCase("z")) return ROCK;
            default:
                throw new IllegalArgumentException(encrypted);
        }
    }

}
