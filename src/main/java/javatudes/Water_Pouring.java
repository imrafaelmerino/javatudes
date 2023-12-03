package javatudes;


import search.Action;
import search.BreathSearch;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * https://en.wikipedia.org/wiki/Water_pouring_puzzle#:~:text=Water%20pouring%20puzzles%20(also%20called,such%20as%20liters%20or%20gallons).
 */
public class Water_Pouring {

    static State MAX_CAPACITY = new State(8, 5, 3);

    static State GOAL = new State(4, 4, 0);

    record State(int cap1, int cap2, int cap3) {}

    public static void main(String[] args) {

        Predicate<State> isValid = s ->
                s.cap1 >= 0 && s.cap1 <= MAX_CAPACITY.cap1
                        && s.cap2 >= 0 && s.cap2 <= MAX_CAPACITY.cap2
                        && s.cap3 >= 0 && s.cap3 <= MAX_CAPACITY.cap3;


        Function<State, List<Action<State>>> succ = s -> {

            if (s.equals(GOAL)) return List.of();

            return Stream.of(new Action<>("POUR 1 -> 2", new State(s.cap1 - MAX_CAPACITY.cap2 + s.cap2, MAX_CAPACITY.cap2, s.cap3)),
                             new Action<>("EMPTY 1 -> 2", new State(0, s.cap2 + s.cap1, s.cap3)),
                             new Action<>("POUR 1 -> 3", new State(s.cap1 - MAX_CAPACITY.cap3 + s.cap3, s.cap2, MAX_CAPACITY.cap3)),
                             new Action<>("EMPTY 1 -> 3", new State(0, s.cap2, s.cap1 + s.cap3)),

                             new Action<>("POUR 2 -> 1", new State(MAX_CAPACITY.cap1, s.cap2 - MAX_CAPACITY.cap1 + s.cap1, s.cap3)),
                             new Action<>("EMPTY 2 -> 1", new State(s.cap1 + s.cap2, 0, s.cap3)),
                             new Action<>("POUR 2 -> 3", new State(s.cap1, s.cap2 - MAX_CAPACITY.cap3 + s.cap3, MAX_CAPACITY.cap3)),
                             new Action<>("EMPTY 2 -> 3", new State(s.cap1, 0, s.cap3 + s.cap2)),

                             new Action<>("POUR 3 -> 1", new State(MAX_CAPACITY.cap1, s.cap2, s.cap3 - MAX_CAPACITY.cap1 + s.cap1)),
                             new Action<>("EMPTY 3 -> 1", new State(s.cap1 + s.cap3, s.cap2, 0)),
                             new Action<>("POUR 3 -> 2", new State(s.cap1, MAX_CAPACITY.cap2, s.cap3 - MAX_CAPACITY.cap2 + s.cap2)),
                             new Action<>("EMPTY 3 -> 2", new State(s.cap1, s.cap2 + s.cap3, 0))
                            )
                         .filter(a -> isValid.test(a.state()))
                         .toList();

        };

        var solution = BreathSearch.fromActions(succ)
                                   .findFirst(new State(8,0,0),s->s.equals(GOAL));

        solution.stream().forEach(System.out::println);

    }
}

