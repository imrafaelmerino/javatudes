package types;

import fun.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class StrFun {


    public static List<Pair<Integer, String>> findRepetitions(String input) {
        List<Pair<Integer, String>> all = new ArrayList<>();
        for (int i = 2; i < input.length() / 2; i++) {
            var result = findLongestRepetition(input, i);
            if (result != null)
                if (all.isEmpty()) all.add(result);
                else if (!result.second()
                                .replaceAll(all.get(all.size() - 1)
                                               .second(),
                                            "")
                                .isEmpty())
                    all.add(result);
        }
        if (all.isEmpty()) return new ArrayList<>();

        all.sort(Comparator.comparing(it -> it.second().length()));

        return all;
    }


    /**
     * Finds the longest repeated substring in the given input string.
     *
     * @param input The input string to search for repeated substrings.
     * @return A {@link Pair} containing the starting index and the repeated substring, or a pair with index -1 and an
     * empty string if no repetition is found.
     *
     * <pre>
     * {@code
     * Example:
     * String input = "abcabcd";
     * Pair<Integer, String> result = StrFun.findRepetition(input);
     * // Result: Pair.of(0, "abc")
     * }
     * </pre>
     */
    public static Pair<Integer, String> findLongestRepetition(String input) {
        var repetitions = findRepetitions(input);

        return repetitions.isEmpty() ?
                Pair.of(-1, "") :
                repetitions.get(repetitions.size() - 1);
    }

    /**
     * Finds the repeated substring of a given window size in the input string.
     *
     * @param input  The input string to search for repeated substrings.
     * @param window The size of the window for finding repetitions.
     * @return A {@link Pair} containing the starting index and the repeated substring, or {@code null} if no repetition
     * is found.
     *
     * <pre>{@code
     * Example:
     * String input = "abcabcd";
     * Pair<Integer, String> result = StrFun.findRepetition(input, 2);
     * // Result: Pair.of(0, "ab")
     * }</pre>
     */
    public static Pair<Integer, String> findLongestRepetition(String input, int window) {
        for (int i = 0; i <= input.length() - 2 * window; i++) {
            var w = input.substring(i, window + i);
            var remaining = input.substring(window + i);
            var w1 = remaining.substring(0, window);
            if (w.equals(w1)) return Pair.of(i, w);
        }
        return null;
    }

    /**
     * Reverses the given string.
     *
     * @param str The input string to reverse.
     * @return The reversed string.
     *
     * <pre>{@code
     * Example:
     * String reversed = StrFun.reverse("hello");
     * // Result: "olleh"
     * }</pre>
     */
    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Finds space-separated integers in the given input string, excluding any existing non-numeric strings.
     *
     * @param input The input string containing space-separated integers and other strings.
     * @return A list of integers found in the input string.
     * @throws NullPointerException  If the input string is null.
     * @throws NumberFormatException If a non-numeric string cannot be parsed into an integer.
     *
     *                               <pre>
     *                                {@code
     *                                                                                                                                                                                                                                                 Example:
     *                                                                                                                                                                                                                                                 String input = "abc 1 def 2 3 ghi";
     *                                                                                                                                                                                                                                                 List<Integer> result = StringParser.findSpacedInts(input);
     *                                                                                                                                                                                                                                                 // Result: [1, 2, 3]
     *                                                                                                                                                                                                                                                 }
     *                                                                                                                                                                                                                                                 </pre>
     */
    public static List<Integer> parseSpacedInts(String input) {
        return Arrays.stream(Objects.requireNonNull(input)
                                    .trim()
                                    .split("\\s"))
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    /**
     * Parses a string of space-separated long integers into a list of long integers.
     *
     * @param input The input string containing space-separated long integers.
     * @return A list of long integers parsed from the input string.
     *
     * <pre>
     * {@code
     * Example:
     * String input = "100 200 300 400 500";
     * List<Long> result = StrFun.parseLongs(input);
     * // Result: [100L, 200L, 300L, 400L, 500L]
     * }
     * </pre>
     */
    public static List<Long> parseSpacedLongs(String input) {
        return Arrays.stream(Objects.requireNonNull(input)
                                    .trim()
                                    .split("\\s"))
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }

    /**
     * Finds space-separated integers in the given input string, excluding any existing non-numeric strings.
     *
     * @param input The input string containing space-separated integers and other strings.
     * @return A list of integers found in the input string.
     *
     * <pre>
     * {@code
     * Example:
     * String input = "abc 1 def 2 3 ghi";
     * List<Integer> result = StrFun.findSpacedInts(input);
     * // Result: [1, 2, 3]
     * }
     * </pre>
     */
    public static List<Integer> findSpacedInts(String input) {
        return Arrays.stream(Objects.requireNonNull(input)
                                    .trim()
                                    .split("\\s"))
                     .filter(s -> s.matches("^-?\\d+$"))
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }

    /**
     * Finds space-separated long integers in the given input string, excluding any existing non-numeric strings.
     *
     * @param input The input string containing space-separated long integers and other strings.
     * @return A list of long integers found in the input string.
     *
     * <pre>
     * {@code
     * Example:
     * String input = "abc 100 def 200 300 ghi";
     * List<Long> result = StringParser.findSpacedLongs(input);
     * // Result: [100L, 200L, 300L]
     * }
     * </pre>
     */
    public static List<Long> findSpacedLongs(String input) {
        return Arrays.stream(Objects.requireNonNull(input)
                                    .trim()
                                    .split("\\s"))
                     .filter(s -> s.matches("^-?\\d+$"))
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }

}
