package advent_of_code._2023.day18;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;
import types.Polygon;
import types.Pos;

import java.util.ArrayList;
import java.util.List;

public class LavaductLagoon implements _2023_Puzzle {
    @Override
    public Object solveFirst() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());

        List<Pos> vertices = new ArrayList<>();
        var perimeter = 0;
        var last = new Pos(0, 0);
        for (var line : lines) {
            var tokens = line.split(" ");
            var dir = tokens[0];
            var n = Integer.parseInt(tokens[1]);
            perimeter += n;
            Pos next = null;
            switch (dir) {
                case "U" -> next = last.minus(new Pos(0, n));
                case "D" -> next = last.plus(new Pos(0, n));
                case "R" -> next = last.plus(new Pos(n, 0));
                case "L" -> next = last.minus(new Pos(n, 0));
                default -> {
                    assert false;
                }
            }
            last = next;
            vertices.add(next);
        }

        //shoelace + Pick's theorem
        return Polygon.internalPoints(Polygon.area(vertices), perimeter) + perimeter;
    }

    @Override
    public Object solveSecond() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());

        List<Pos> vertices = new ArrayList<>();
        var perimeter = 0L;
        var last = new Pos(0, 0);
        for (var line : lines) {
            var tokens = line.split(" ");
            var color = tokens[2];
            color = color.substring(2, color.length() - 1); //remove parenthesis
            String dir = color.substring(color.length() - 1);
            var n = Integer.parseInt(color.substring(0, color.length() - 1), 16); //from hex to decimal
            perimeter += n;
            Pos next = null;
            switch (dir) {
                case "3" -> next = last.minus(new Pos(0, n));
                case "1" -> next = last.plus(new Pos(0, n));
                case "0" -> next = last.plus(new Pos(n, 0));
                case "2" -> next = last.minus(new Pos(n, 0));
                default -> {
                    assert false;
                }
            }
            last = next;
            vertices.add(next);
        }

        //shoelace + Pick's theorem
        return Polygon.internalPoints(Polygon.area(vertices), perimeter) + perimeter;
    }

    @Override
    public String name() {
        return "Lavaduct Lagoon";
    }

    @Override
    public int day() {
        return 18;
    }

    @Override
    public String outputUnitsPart1() {
        return "cubic meters of lava";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }
}
