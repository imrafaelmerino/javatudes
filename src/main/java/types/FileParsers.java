package types;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileParsers {


    public static List<Matcher> toListOfLineMatchers(String input,
                                                     String regex,
                                                     Predicate<String> skipLine
                                                    ) {
        Pattern pattern = Pattern.compile(regex);
        return toListOfLines(input, skipLine).stream()
                                             .map(pattern::matcher)
                                             .filter(Matcher::matches)
                                             .collect(Collectors.toList());
    }

    public static List<Matcher> toListOfLineMatchers(String input,
                                                     String regex
                                                    ) {
        return toListOfLineMatchers(input,
                                    regex,
                                    pos -> false);
    }

    /**
     * parse a file returning a list with all the lines. Blank lines are ignored depending on the given ignoreBlank
     * parameter
     *
     * @param input    the absolute file path
     * @param skipLine if true, lines are ignored
     * @return a list of string
     */
    public static List<String> toListOfLines(String input,
                                             Predicate<String> skipLine
                                            ) {
        try {
            List<String> xs = Files.readAllLines(new File(input).toPath());
            return
                    xs.stream()
                      .filter(skipLine.negate())
                      .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> toListOfLines(String input) {
        return toListOfLines(input, str -> false);
    }


    /**
     * Parse a file into a list of groups, being a group a bunch of consecutive lines without any blank line between
     * them. Example:
     *
     * <pre>
     * AAAA
     * BBBB
     *              =>  List(List("AAAA", "BBBB"), List("CCCC",  "DDDD"), List("EEEE","FFFF"))
     * CCCC
     * DDDD
     *
     * EEEE
     * FFFF
     * </pre>
     *
     * @param input the absolute file path
     * @return a list of groups
     */
    public static List<List<String>> toGroupsOfLines(String input,
                                                     Predicate<String> lineIsGroupSeparator
                                                    ) {
        List<List<String>> result = new ArrayList<>();

        List<String> lines = toListOfLines(input, e -> false);

        List<String> group = new ArrayList<>();

        for (var line : lines) {
            if (lineIsGroupSeparator.test(line)) {
                if (!group.isEmpty()) result.add(group);
                group = new ArrayList<>();
            } else group.add(line);
        }
        if (!lines.isEmpty()) result.add(group);
        return result;
    }

    public static List<List<String>> toGroupsOfLines(String input) {
        return toGroupsOfLines(input, e -> e.isEmpty() || e.isBlank());
    }

    public static List<List<String>> toListOfSplitLines(String path) {
        return toListOfSplitLines(path, "", e -> false);
    }

    public static List<List<String>> toListOfSplitLines(String path,
                                                        String separator,
                                                        Predicate<String> skipLine
                                                       ) {
        Path file = Path.of(path);
        assert (file.toFile().exists());
        try {
            return
                    Files.lines(file, StandardCharsets.UTF_8)
                         .filter(skipLine.negate())
                         .map(it -> Arrays.stream(it.split(separator)).toList())
                         .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}




