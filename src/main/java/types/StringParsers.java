package types;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class StringParsers {


    /**
     * parse a string into a list of strings. Lists can be arbitrary nested. Examples:
     * <pre>
     * [[9,7,[]],[[4],[10,[],7],6,0],[[[3,6],4,1,7],1,10]]
     *
     * [[[2,[1,3,6],5,9,10]],[]]
     *
     * [[0,2,7],[],[10,[[0,7,3,6]]],[2,[8],3]]
     *
     * </pre>
     *
     * @param input a string
     * @return a list
     */
    public static List toList(String input) {
        return toList(requireNonNull(input).substring(1),
                      new ArrayList<>()
                     )
                .first();
    }

    @SuppressWarnings({"raw", "unchecked"})
    static Pair<List, String> toList(String input,
                                     List result
                                    ) {

        if (input.isEmpty()) return Pair.of(result, input);
        var head = input.charAt(0);
        if (head == ']') return Pair.of(result,
                                        input.substring(1)
                                       );
        if (head == '[') {
            var pair = toList(input.substring(1),
                              new ArrayList<>()
                             );
            result.add(pair.first());


            return toList(pair.second(),
                          result
                         );
        }
        if (head == ',')
            return toList(input.substring(1),
                          result
                         );


        var pair = readAlphanumeric(input);
        if (pair.first().isEmpty()) return Pair.of(result,
                                                   pair.second());

        result.add(pair.first());

        return toList(pair.second(),
                      result
                     );

    }

    /**
     * reads an int number and returns the remainder. Examples:
     * <pre>
     *     "12abc132  ->  (12, "abc13")
     *     "21"       ->  (21, "")
     *     "21, 22"       ->  (21, ",22")
     * </pre>
     *
     * @param input a string
     * @return a pair with the int number and the remainder
     */
    public static Pair<Integer, String> readInt(String input) {

        var n = "";
        var j = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) {
                n += input.charAt(i);
                j = i + 1;
            } else break;

        }
        return n.isEmpty() ?
                Pair.of(null, input) :
                Pair.of(Integer.parseInt(n),
                        input.substring(j)
                       );
    }


    /**
     * reads an alphabetic string and returns the remainder. Examples:
     * <pre>
     *     "abc, cde" ->  ("abc", ",cde")
     *     "21"       ->  (21, "")
     *     "21, 22"   ->  (21, ",22")
     * </pre>
     *
     * @param input a string
     * @return a pair with the int number and the remainder
     */
    public static Pair<String, String> readAlphanumeric(String input) {

        var n = "";
        var j = 0;
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (Character.isDigit(character) || Character.isAlphabetic(character)) {
                n += character;
                j = i + 1;
            } else break;

        }
        return n.isEmpty() ?
                Pair.of("", input) :
                Pair.of(n,
                        input.substring(j)
                       );
    }

    public static Pair<Integer, String> readInteger(String input) {

        var n = "";
        var j = 0;
        for (int i = 0; i < input.length(); i++) {
            if (Character.isDigit(input.charAt(i))) n += input.charAt(i);
            else {
                j = i;
                break;
            }
        }
        return Pair.of(Integer.parseInt(n),
                       input.substring(j + 1));
    }




}
