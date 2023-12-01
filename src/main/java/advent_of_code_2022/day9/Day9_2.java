package advent_of_code_2022.day9;

import types.FileParsers;
import types.Pos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Day9_2 {

    record Knots(Pos head, List<Pos> tail) {}

    public static void main(String[] args) throws IOException {

        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day9/input_test_2.txt";
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day9/input.txt";

        var moves = FileParsers.toListOfLines(input);

        var start = new Pos(0, 0);
        var current = new Knots(start, IntStream.range(0, 9).mapToObj(it -> start).collect(Collectors.toList()));
        List<Knots> steps = new ArrayList<>();
        for (String line : moves) {
            String[] move = line.split("\s");
            String direction = move[0];
            int times = Integer.parseInt(move[1]);
            steps.addAll(moveHead(direction, current, new ArrayList<>(), times));
            current = steps.get(steps.size() - 1);
        }
        System.out.println(steps.stream()
                                .map(it -> it.tail.get(it.tail.size() - 1))
                                .collect(Collectors.toSet())
                                .size()
                          );

    }

    static List<Knots> moveHead(String direction, Knots knots, List<Knots> result, int n) {
        if (n == 0) return result;
        Pos head = switch (direction) {
            case "R" -> knots.head.right();
            case "L" -> knots.head.left();
            case "U" -> knots.head.down();
            default -> knots.head.up();
        };
        List<Pos> tail = moveTails(head, knots.tail, new ArrayList<>());
        Knots next = new Knots(head, tail);
        result.add(next);
        return moveHead(direction, next, result, n - 1);
    }

    private static List<Pos> moveTails(Pos head, List<Pos> tails, List<Pos> result) {
        if (tails.isEmpty()) return result;
        Pos tail = moveTail(head, tails.get(0)); //update firs tail
        result.add(tail);
        //firs tails is the head of the second tail and so on
        return moveTails(tail, tails.subList(1, tails.size()), result);
    }

    static Pos moveTail(Pos head, Pos tail) {

        if (head.equals(tail)) return tail;

        if (tail.getNeighbors().contains(head)) return tail;

        if (head.x() == tail.x()) return (head.y() > tail.y()) ? tail.up() : tail.down();

        if (head.y() == tail.y()) return (head.x() > tail.x()) ? tail.right() : tail.left();

        if (head.y() > tail.y()) return (head.x() > tail.x()) ? tail.up().right() : tail.up().left();

        return (head.x() > tail.x()) ? tail.down().right() : tail.down().left();

    }
}
