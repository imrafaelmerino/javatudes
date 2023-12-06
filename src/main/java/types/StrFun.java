package types;

import fun.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class StrFun {


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
    public static Pair<Integer, String> findRepetition(String input) {
        List<Pair<Integer, String>> all = new ArrayList<>();
        for (int i = 2; i < input.length() / 2; i++) {
            var result = findRepetition(input, i);
            if (result != null)
                if (all.isEmpty()) all.add(result);
                else if (!result.second()
                                .replaceAll(all.get(all.size() - 1)
                                               .second(),
                                            "")
                                .isEmpty())
                    all.add(result);
        }
        if (all.isEmpty()) return Pair.of(-1, "");

        all.sort(Comparator.comparing(it -> it.second().length()));

        return all.get(all.size() - 1);
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
    public static Pair<Integer, String> findRepetition(String input, int window) {
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

    public static List<Integer> toListOfInt(String str) {
        return Arrays.stream(Objects.requireNonNull(str)
                                    .trim()
                                    .split("\\s"))
                     .map(Integer::parseInt)
                     .collect(Collectors.toList());
    }


    public static List<Long> toListOfLong(String str) {
        return Arrays.stream(Objects.requireNonNull(str)
                                    .trim()
                                    .split("\\s"))
                     .map(Long::parseLong)
                     .collect(Collectors.toList());
    }
}
