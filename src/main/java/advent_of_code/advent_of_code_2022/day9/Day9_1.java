package advent_of_code.advent_of_code_2022.day9;

import types.FileParsers;
import types.Pos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


public class Day9_1 {

    record Knots(Pos head, Pos tail) {}

    public static void main(String[] args)  {

        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day9/input_test_2.txt";
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day9/input.txt";

        var moves = FileParsers.toListOfLines(input);

        var start = new Pos(0, 0);
        var current = new Knots(start, start);
        List<Knots> steps = new ArrayList<>();
        for (String line : moves) {
            String[] move = line.split("\s");
            String direction = move[0];
            int times = Integer.parseInt(move[1]);
            steps.addAll(moveHead(direction, current, new ArrayList<>(), times));
            current = steps.get(steps.size() - 1);
        }
        System.out.println(steps.stream()
                                .map(it -> it.tail)
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
        Pos tail = moveTail(head, knots.tail);
        Knots next = new Knots(head, tail);
        result.add(next);
        return moveHead(direction, next, result, n - 1);
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
