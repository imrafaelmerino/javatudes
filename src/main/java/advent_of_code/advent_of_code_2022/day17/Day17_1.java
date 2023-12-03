package advent_of_code.advent_of_code_2022.day17;

import fun.tuple.Pair;
import types.FileParsers;
import types.Grid;
import types.MutableGrid;
import types.Pos;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17_1 {
    static int WIDE = 7;
    static int LEFT_GAP = 2;
    static int BOTTOM_GAP = 3;
    static String EMPTY = ".";
    static String REST_ROCK = "#";
    static String MOVE_ROCK = "@";

    static String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day17/input.txt";
    //static String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day17/input_test_2.txt";

    static String JET_PATTERN = FileParsers.toListOfLines(input)
                                           .get(0)
                                           .trim();


    static Grid<String> chamber = MutableGrid.fromString("""
                                                                 .........
                                                                 .........
                                                                 .........
                                                                 .........
                                                                 ---------""");

    static List<Grid<String>> ROCKS = List.of(MutableGrid.fromString("@@@@"),
                                              MutableGrid.fromString("""
                                                                             .@.
                                                                             @@@
                                                                             .@.""").remove((pos,val) -> val.equals(EMPTY)),
                                              MutableGrid.fromString("""
                                                                             ..@
                                                                             ..@
                                                                             @@@""").remove((pos,val) -> val.equals(EMPTY)),
                                              MutableGrid.fromString("""
                                                                             @
                                                                             @
                                                                             @
                                                                             @"""),
                                              MutableGrid.fromString("""
                                                                             @@
                                                                             @@""")

                                             );

    static Iterator<Pair<Integer, Grid<String>>> ROCK_ITERATOR =
            IntStream.iterate(0, i -> i + 1)
                     .mapToObj(i -> Pair.of(i + 1, ROCKS.get(i % ROCKS.size())))
                     .iterator();


    static Iterator<String> PATTERN_ITERATOR =
            Stream.iterate(JET_PATTERN, i -> JET_PATTERN)
                  .flatMap(it -> Arrays.stream(it.split("")))
                  .iterator();


    public static void main(String[] args) {

        while (ROCK_ITERATOR.hasNext()) {

            Pair<Integer, Grid<String>> next = ROCK_ITERATOR.next();

            Grid<String> rock = next.second();

            if (next.first() == 2023) {
                System.out.println(Math.abs(chamber.ymax() - chamber.ymin()));
                System.exit(1);
            }

            move(chamber, rock, PATTERN_ITERATOR);

        }


    }

    static Grid<String> placeRock(Grid<String> chamber,
                                  Grid<String> rock
                                 ) {
        var STARTING_Y_POINT = chamber.containsValue(REST_ROCK) ?
                chamber.getCellsSet()
                       .stream()
                       .filter(v -> v.value().equals(REST_ROCK))
                       .map(c->c.pos().y())
                       .min(Integer::compareTo)
                       .get() - BOTTOM_GAP - 1 - rock.ymax() + rock.ymin() :
                chamber.ymax() - BOTTOM_GAP - 1;

        var STARTING_X_POINT = chamber.xmin() + LEFT_GAP + 1;


        return rock.translate(STARTING_X_POINT,
                              STARTING_Y_POINT
                             );


    }


    static Grid<String> move(Grid<String> chamber,
                             Grid<String> rock,
                             Iterator<String> JET_PATTERN
                            ) {
        Grid<String> moved = placeRock(chamber,
                                       rock
                                      );
        while (JET_PATTERN.hasNext()) {
            String next = JET_PATTERN.next();
            if (next.equals(">")) moved = moveRight(chamber, moved);
            else if (next.equals("<")) moved = moveLeft(chamber, moved);
            else throw new RuntimeException();
            var restedRock = moveDown(chamber, moved);
            if (restedRock.first())
                return chamber.merge(restedRock.second()
                                               .mapValues((p,v)->REST_ROCK,
                                                                    (p,v) -> v.equals(MOVE_ROCK)
                                                             )
                                    );
            moved = restedRock.second();

        }


        throw new RuntimeException("pattern exhausted");
    }

    static Grid<String> moveRight(Grid<String> chamber,
                                  Grid<String> rock
                                 ) {
        var translated = rock.translate(1, 0);
        var empty = translated.getRows()
                              .stream()
                              .map(it -> it.get(it.size() - 1))
                              .allMatch(e -> {
                                            if (e.pos().x() == WIDE + 1) return false;
                                            String s = chamber.getVal(e.pos());
                                            return !REST_ROCK.equals(s);
                                        }
                                       );


        return empty ? translated : rock;

    }

    static Grid<String> moveLeft(Grid<String> chamber,
                                 Grid<String> rock
                                ) {
        var translated = rock.translate(-1, 0);
        var emtpy = translated.getRows()
                              .stream()
                              .map(it -> it.get(0))
                              .allMatch(e -> {
                                            if (e.pos().x() == 0) return false;
                                            String s = chamber.getVal(e.pos());
                                            return !REST_ROCK.equals(s);
                                        }
                                       );

        return emtpy ? translated : rock;

    }

    static Pair<Boolean, Grid<String>> moveDown(Grid<String> chamber,
                                                Grid<String> rock
                                               ) {
        var translated = rock.translate(0, 1);
        var empty = translated.getColumns()
                              .stream()
                              .map(it -> it.get(it.size() - 1))
                              .allMatch(e -> {
                                            String s = chamber.getVal(e.pos());
                                            return s == null || s.equals(EMPTY);
                                        }
                                       );

        return Pair.of(!empty,
                       empty ? translated : rock
                      );

    }


}


