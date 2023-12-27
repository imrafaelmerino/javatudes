package advent_of_code._2023.day22;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;

import java.util.*;
import java.util.stream.IntStream;

public class SandSlabs implements _2023_Puzzle {



    private void placeOnGround(List<Brick> bricks) {
        for (int i = 0; i < bricks.size(); i++) {
            var brick = bricks.get(i);
            var z = i == 0 ?
                    1 :
                    IntStream.range(0, i)//since bricks are sorted, we iterate through the ones that are below (lower ez)
                             .mapToObj(bricks::get)
                             .filter(brick::intersect)
                             .mapToInt(b -> b.ez + 1)//add one to be placed just on top!
                             .max()
                             .orElse(1); // go straight to the ground!

            var length = brick.ez - brick.sz;
            brick.sz = z;
            brick.ez = z + length;
        }
    }

    @Override
    public Object solveFirst() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());
        ArrayList<Brick> bricks = new ArrayList<>();
        Map<Integer,Brick> zStartMap = new HashMap<>();
        for (String line : lines) {
            var points = line.split("~");
            var a = points[0];
            var b = points[1];
            var ac = a.split(",");
            var bc = b.split(",");
            var ax = Integer.parseInt(ac[0]);
            var ay = Integer.parseInt(ac[1]);
            var az = Integer.parseInt(ac[2]);
            var bx = Integer.parseInt(bc[0]);
            var by = Integer.parseInt(bc[1]);
            var bz = Integer.parseInt(bc[2]);
            Brick e = new Brick(ax, ay, az, bx, by, bz);
            bricks.add(e);
            zStartMap.put(az, e);
        }
        bricks.sort(Comparator.comparingInt(r -> r.sz));
        placeOnGround(bricks);
        var count = 0;
        for (var brick : bricks) {
            var bricksOnTop = getAtTheTop(bricks, brick);
            if (bricksOnTop.isEmpty()) count += 1;
            else if (bricksOnTop.stream()
                                .allMatch(brickOnTop -> countOnTheBottom(bricks, brickOnTop) > 1)) count += 1;
        }
        return count;
    }

    public List<Brick> getAtTheTop(List<Brick> bricks, Brick brick) {
        return bricks.stream()
                     .filter(it -> it.sz - 1 == brick.ez && it.intersect(brick))
                     .toList();
    }


    public long countOnTheBottom(List<Brick> bricks, Brick brick) {
        return bricks.stream()
                     .filter(it -> it.ez == brick.sz - 1 && it.intersect(brick))
                     .count();
    }


    public long countFalls(List<Brick> bricks,
                           List<Brick> atTop
                          ) {
        var willFall = atTop.stream()
                            .filter(it -> countOnTheBottom(bricks, it) == 0)
                            .toList();

        bricks.removeAll(willFall);
        return willFall.size() +
               willFall.stream()
                       .map(it -> countFalls(bricks, getAtTheTop(bricks, it)))
                       .reduce(0L, Long::sum);
    }

    @Override
    public Object solveSecond() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());
        ArrayList<Brick> bricks = new ArrayList<>();
        Map<Integer,Brick> zStartMap = new HashMap<>();
        for (String line : lines) {
            var points = line.split("~");
            var a = points[0];
            var b = points[1];
            var ac = a.split(",");
            var bc = b.split(",");
            var ax = Integer.parseInt(ac[0]);
            var ay = Integer.parseInt(ac[1]);
            var az = Integer.parseInt(ac[2]);
            var bx = Integer.parseInt(bc[0]);
            var by = Integer.parseInt(bc[1]);
            var bz = Integer.parseInt(bc[2]);
            Brick e = new Brick(ax, ay, az, bx, by, bz);
            bricks.add(e);
            zStartMap.put(az, e);
        }
        bricks.sort(Comparator.comparingInt(r -> r.sz));
        placeOnGround(bricks);
        var count = 0L;
        for (var brick : bricks) {
            var copy = new ArrayList<>(bricks);
            copy.remove(brick);
            count += (countFalls(copy, getAtTheTop(copy, brick))); // doesn count the first one to fall
        }
        return count;
    }

    @Override
    public String name() {
        return "Sand Slabs";
    }

    @Override
    public int day() {
        return 22;
    }

    @Override
    public String outputUnitsPart1() {
        return "bricks";
    }

    @Override
    public String outputUnitsPart2() {
        return "sum of all bricks";
    }


}
