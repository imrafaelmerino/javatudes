package advent_of_code;

import types.decorators.Timing;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public interface Calendar {


    static void mainProgram(Calendar calendar, String[] args) throws Exception {
        String str = String.join(" ", requireNonNull(args))
                           .trim();

        var allPattern = Pattern.compile("^all$");
        var dayPartPattern = Pattern.compile("^(?<day>\\d?\\d) +(?<part>[1-2])$");
        var dayPattern = Pattern.compile("^\\d\\d?$");

        var allMatcher = allPattern.matcher(str);
        var dayPartMatcher = dayPartPattern.matcher(str);
        var dayMatcher = dayPattern.matcher(str);

        if (allMatcher.matches())
            calendar.printAllSolutions();
        else if (dayMatcher.matches())
            calendar.printDaySolutions(Integer.parseInt(str));
        else if (dayPartMatcher.matches()) {
            var day = Integer.parseInt(dayPartMatcher.group("day"));
            var part = Integer.parseInt(dayPartMatcher.group("part"));
            calendar.printDayPartSolution(day, part);
        } else throw new IllegalArgumentException("%s is not valid. Use all | [1-24] | [1-24] [1-2]".formatted(str));
    }

    List<Puzzle> getPuzzles();

    default Puzzle findPuzzle(int day) {
        return getPuzzles().stream().filter(it -> it.day() == day)
                           .findFirst()
                           .orElseThrow(() -> new RuntimeException("day %d not found".formatted(day)));
    }

    default Puzzle findPuzzle(String name) {
        return getPuzzles().stream().filter(it -> it.name().equals(name))
                           .findFirst()
                           .orElseThrow(() -> new RuntimeException("name %s not found".formatted(name)));
    }


    default void printDaySolutions(int day) {
        var title = new StringBuilder("Advent of Code %s @ %s\n".formatted(year(), link()));

        var puzzle = findPuzzle(day);
        var timing1 = new Timing<>(() -> {
            try {
                return puzzle.solveFirst();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 1);
        var timing2 = new Timing<>(() -> {
            try {
                return puzzle.solveSecond();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 1);
        String s = """
                - Day %s: %s
                  . Part 1: %s in %s
                  . Part 2: %s in %s
                """;
        System.out.println(title.append(s.formatted(puzzle.day(),
                                                    puzzle.name(),
                                                    timing1.get(), Timing.formatTime(timing1.stats.accTime),
                                                    timing2.get(), Timing.formatTime(timing2.stats.accTime)
                                                   )));
    }

    default void printDayPartSolution(int day, int part) {
        var title = new StringBuilder("Advent of Code %s @ %s\n".formatted(year(), link()));

        var puzzle = findPuzzle(day);
        var timing = new Timing<>(() -> {
            try {
                return part == 1 ? puzzle.solveFirst() : puzzle.solveSecond();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 1);

        System.out.println(title.append("""
                                                 - Day %s: %s
                                                   . Part %s: %s in %s
                                                """
                                                .formatted(day,
                                                           puzzle.name(),
                                                           part,
                                                           timing.get(),
                                                           Timing.formatTime(timing.stats.accTime))));


    }

    default void printAllSolutions() {
        var title = new StringBuilder("Advent of Code %s @ %s\n".formatted(year(), link()));
        for (var puzzle : getPuzzles()) {
            var timing1 = new Timing<>(() -> {
                try {
                    return puzzle.solveFirst();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, 1);
            var timing2 = new Timing<>(() -> {
                try {
                    return puzzle.solveSecond();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, 1);
            String s = """
                    - Day %s: %s
                      . Part 1: %s in %s
                      . Part 2: %s in %s
                    """;
            title = title.append(s.formatted(puzzle.day(),
                                             puzzle.name(),
                                             timing1.get(), Timing.formatTime(timing1.stats.accTime),
                                             timing2.get(), Timing.formatTime(timing2.stats.accTime)
                                            ));

        }
        System.out.println(title);
    }

    String year();

    default String link() {
        return "https://adventofcode.com/%s".formatted(year());
    }

}
