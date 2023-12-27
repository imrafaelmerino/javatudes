package advent_of_code;

import types.decorators.Timing;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public interface Calendar {


    static void mainProgram(Calendar calendar, String[] args) {
        String str = String.join(" ", requireNonNull(args))
                           .trim();

        var allPattern = Pattern.compile("^all(?<times> \\d+)?$");
        var dayPartPattern = Pattern.compile("^(?<day>\\d?\\d) +(?<part>[1-2])(?<times> \\d+)$");
        var dayPattern = Pattern.compile("^(?<day>\\d?\\d)(?<times> \\d+)$");

        var allMatcher = allPattern.matcher(str);
        var dayPartMatcher = dayPartPattern.matcher(str);
        var dayMatcher = dayPattern.matcher(str);


        if (allMatcher.matches()) {
            var times = allMatcher.group("times") != null ? Integer.parseInt(allMatcher.group("times").trim()) : 1;
            calendar.printAllSolutions(times);
        } else if (dayMatcher.matches()) {
            var times = dayMatcher.group("times") != null ? Integer.parseInt(dayMatcher.group("times").trim()) : 1;
            var day = Integer.parseInt(dayMatcher.group("day"));
            calendar.printDaySolutions(day, times);
        } else if (dayPartMatcher.matches()) {
            var times = dayPartMatcher.group("times") != null ? Integer.parseInt(dayPartMatcher.group("times").trim()) : 1;
            var day = Integer.parseInt(dayPartMatcher.group("day"));
            var part = Integer.parseInt(dayPartMatcher.group("part"));
            calendar.printDayPartSolution(day, part, times);
        } else throw new IllegalArgumentException("`%s` is not valid.".formatted(str));
    }

    List<Puzzle> getPuzzles();

    default Puzzle findPuzzle(int day) {
        return getPuzzles().stream()
                           .filter(it -> it.day() == day)
                           .findFirst()
                           .orElseThrow(() -> new RuntimeException("day %d not found".formatted(day)));
    }


    default void printDaySolutions(int day, int times) {
        var title = new StringBuilder("Advent of Code %s @ %s\n\n".formatted(year(), link()));

        var puzzle = findPuzzle(day);
        var timing1 = new Timing<>(() -> {
            try {
                return puzzle.solveFirst();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, times);
        var timing2 = new Timing<>(() -> {
            try {
                return puzzle.solveSecond();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, times);
        String s = """
                - Day %s: %s
                  . Part 1: 
                       Solution: %s %s
                       Stats: %s
                  . Part 2: 
                       Solution: %s %s
                       Stats: %s
                """;
        System.out.println(title.append(s.formatted(puzzle.day(),
                                                    puzzle.name(),
                                                    timing1.get(),
                                                    puzzle.outputUnitsPart1(),
                                                    times == 1 ?
                                                            timing1.stats.getAccTime() :
                                                            timing1.getTimeStats(),
                                                    timing2.get(),
                                                    puzzle.outputUnitsPart2(),
                                                    times == 1 ?
                                                            timing2.stats.getAccTime() :
                                                            timing2.getTimeStats()
                                                   )));
    }


    default void printDayPartSolution(int day, int part, int times) {
        var title = new StringBuilder("Advent of Code %s @ %s\n\n".formatted(year(), link()));

        var puzzle = findPuzzle(day);
        var timing = new Timing<>(() -> {
            try {
                return part == 1 ? puzzle.solveFirst() : puzzle.solveSecond();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, times);

        System.out.println(title.append("""
                                                 - Day %s: %s
                                                   . Part %s: 
                                                        Solution: %s %s
                                                        Stats: %s
                                                """
                                                .formatted(day,
                                                           puzzle.name(),
                                                           part,
                                                           timing.get(),
                                                           part == 1 ?
                                                                   puzzle.outputUnitsPart1() :
                                                                   puzzle.outputUnitsPart2(),
                                                           times == 1 ?
                                                                   timing.stats.getAccTime() :
                                                                   timing.getTimeStats())));


    }


    default void printAllSolutions(int times) {
        var title = new StringBuilder("Advent of Code %s @ %s\n\n".formatted(year(), link()));
        for (var puzzle : getPuzzles()) {
            var timing1 = new Timing<>(() -> {
                try {
                    return puzzle.solveFirst();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, times);
            var timing2 = new Timing<>(() -> {
                try {
                    return puzzle.solveSecond();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, times);
            title.append("""
                                 - Day %s: %s
                                   . Part 1:
                                        Solution: %s %s 
                                        Stats: %s
                                   . Part 2: 
                                        Solution: %s %s 
                                        Stats: %s
                                        
                                 """.formatted(puzzle.day(),
                                               puzzle.name(),
                                               timing1.get(),
                                               puzzle.outputUnitsPart1(),
                                               times == 1 ?
                                                       timing1.stats.getAccTime() :
                                                       timing1.getTimeStats(),
                                               timing2.get(),
                                               puzzle.outputUnitsPart2(),
                                               times == 1 ?
                                                       timing2.stats.getAccTime() :
                                                       timing2.getTimeStats()
                                              ));

        }
        System.out.println(title);
    }

    String year();

    default String link() {
        return "https://adventofcode.com/%s".formatted(year());
    }

}
