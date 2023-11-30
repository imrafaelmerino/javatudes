package types;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StrFun {

    /**
     *
     * @param input
     * @return a pair with the index where the pattern starts and the string which is repeated
     */
    public static Pair<Integer, String> findRepetition(String input) {
        List<Pair<Integer, String>> all = new ArrayList<>();
        for (int i = 2; i < input.length() / 2; i++) {
            Pair<Integer, String> result = findRepetition(input, i);
            if (result != null)
                if (all.isEmpty())
                    all.add(result);
                else if (!result.second().replaceAll(all.get(all.size() - 1).second(), "").isEmpty())
                    all.add(result);
        }

        all.sort(Comparator.comparing(it -> it.second().length()));

        return all.isEmpty() ? Pair.of(-1, "") : all.get(all.size() - 1);
    }

    public static Pair<Integer, String> findRepetition(String input,  int window) {
        for(int i = 0; i <= input.length() - 2*window  ;i++){
            String w = input.substring(i, window+i);
            String remaining = input.substring(window+i);
            String w1 = remaining.substring(0, window);
            if (w.equals(w1)) return Pair.of(i, w);
        }
        return null;
    }

}
