package advent_of_code._2023.day1;

import advent_of_code.Puzzle;
import types.ListFun;
import types.StrFun;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static types.Constants.WORD_REPRESENTATIONS_FOR_DIGITS;

public final class Trebuchet implements Puzzle {


    /**
     * valid for part1 too!
     */
    private static int getNumberPart2(String line) {
        var digits = findAllDigitsIncludingOverlaps(line);
        String result = "%d%d".formatted(ListFun.head(digits),
                                         ListFun.last(digits)
                                        );

        return Integer.parseInt(result);
    }

    private static int getNumberPart1(String line) {
        return Integer.parseInt("%d%d".formatted(findFirstDigit(line),
                                                 findFirstDigit(StrFun.reverse(line))
                                                )
                               );
    }

    private static int findFirstDigit(String line) {
        return line.chars()
                   .filter(Character::isDigit)
                   .findFirst()
                   .getAsInt() - '0';
    }

    /**
     * this method handles overlaps. It's ok since we just want the last one twoneight -> [two, one, eight]
     *
     * @return all digits including word overlaps
     */
    private static List<Integer> findAllDigitsIncludingOverlaps(String line) {
        return findAllDigitsIncludingOverlaps(line, "", new ArrayList<>());
    }


    private static List<Integer> findAllDigitsIncludingOverlaps(String remainder,
                                                                String letters,
                                                                List<Integer> digits
                                                               ) {
        if (remainder.isEmpty()) return digits;
        var head = remainder.charAt(0);
        var tail = remainder.substring(1);
        if (Character.isDigit(head))
            return findAllDigitsIncludingOverlaps(tail, "", ListFun.append(digits, head - '0'));
        var xs = letters + head;
        var opt = IntStream.rangeClosed(0, 9)
                           .filter(d -> xs.endsWith(WORD_REPRESENTATIONS_FOR_DIGITS.get(d)))
                           .findFirst();

        return opt.isPresent()
                ? findAllDigitsIncludingOverlaps(tail,
                                                 xs, // tricky! and not "" because otherwise we leave out overlaps
                                                 ListFun.append(digits,
                                                                opt.getAsInt())
                                                )
                : findAllDigitsIncludingOverlaps(tail,
                                                 xs,
                                                 digits);


    }


    @Override
    public Object solveFirst() throws Exception {
        return Files.readAllLines(Path.of(getInputPath()))
                    .stream()
                    .map(Trebuchet::getNumberPart2)
                    .reduce(0, Integer::sum);
    }

    @Override
    public Object solveSecond() throws Exception{
        return Files.readAllLines(Path.of(getInputPath()))
                    .stream()
                    .map(Trebuchet::getNumberPart1)
                    .reduce(0, Integer::sum);
    }

    @Override
    public String name() {
        return "Trebuchet";
    }

    @Override
    public int day() {
        return 1;
    }

    @Override
    public String getInputPath() {
        return "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/_2023/day1/input.txt";
    }

    @Override
    public String outputUnits() {
        return "calibration values";
    }


}
