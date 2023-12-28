package advent_of_code._2023.day24;

import advent_of_code._2023._2023_Puzzle;
import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;
import types.FileParsers;
import types.Line;

import java.util.ArrayList;
import java.util.List;

public class NeverTellMeTheOdds implements _2023_Puzzle {


    private static List<HailStone> parse(List<String> lines) {
        List<HailStone> hailstones = new ArrayList<>();
        for (String line : lines) {
            var tokens = line.split("@");
            var xyz = tokens[0].trim().split(",");
            var vxyz = tokens[1].trim().split(",");
            hailstones.add(new HailStone(Long.parseLong(xyz[0].trim()),
                                         Long.parseLong(xyz[1].trim()),
                                         Long.parseLong(xyz[2].trim()),
                                         Long.parseLong(vxyz[0].trim()),
                                         Long.parseLong(vxyz[1].trim()),
                                         Long.parseLong(vxyz[2].trim())));

        }
        return hailstones;
    }

    private static boolean inTestArea(Line.Coor coor, long I2, long I1) {
        return coor.x() <= I2
               && coor.y() <= I2
               && coor.x() >= I1
               && coor.y() >= I1;
    }

    private static boolean isFuture(double timeA, double timeB) {
        return timeA >= 0 && timeB >= 0;
    }

    @Override
    public Object solveFirst() throws Exception {
        long I1 =  200000000000000L;
        long I2 = 400000000000000L;
        var lines = FileParsers.toListOfLines(getInputPath());
        var hailstones = parse(lines);
        var count = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                var a = hailstones.get(i);
                var b = hailstones.get(j);

                var lineA = new Line(new Line.Coor(a.x, a.y),
                                     new Line.Coor(a.x + a.vx, a.y + a.vy)
                );
                var lineB = new Line(new Line.Coor(b.x, b.y),
                                     new Line.Coor(b.x + b.vx, b.y + b.vy)
                );

                var intersection = lineA.intersection(lineB);

                if (intersection != null) {
                    var timeA = (intersection.x() - a.x) / a.vx;
                    var timeB = (intersection.x() - b.x) / b.vx;
                    if (inTestArea(intersection, I2, I1) && isFuture(timeA, timeB)) count += 1;
                }
            }
        }
        return count;
    }


    @Override
    public Object solveSecond() throws Exception {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public int day() {
        return 24;
    }

    @Override
    public String outputUnitsPart1() {
        return "intersections";
    }

    @Override
    public String outputUnitsPart2() {
        return null;
    }


    record HailStone(long x, long y, long z, long vx, long vy, long vz) {
    }
}
