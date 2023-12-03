package javatudes;

import search.Action;
import search.BranchBoundSearch;
import types.ListFun;
import types.decorators.Log2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * https://en.wikipedia.org/wiki/Bridge_and_torch_problem
 * <p>
 * Four people come to a river in the night.
 * There is a narrow bridge, but it can only
 * hold two people at a time. They have one
 * torch and, because it's night, the torch
 * has to be used when crossing the bridge.
 * Person A can cross the bridge in 1 minute,
 * B in 2 minutes, C in 5 minutes, and D in
 * 8 minutes. When two people cross the bridge
 * together, they must move at the slower
 * person's pace. The question is, can they all
 * get across the bridge if the torch lasts
 * only 15 minutes?
 */
public class Bridge_And_Torch {

    static Map<String, Integer> costs = Map.of("A", 1, "B", 2, "C", 5, "D", 8);

    static List<String> ALL = List.of("A", "B", "C", "D", "TORCH");

    record State(List<String> here, List<String> across) {

        boolean torchHere() {
            return here.contains("TORCH");
        }

        State crossAcross(List<String> stuff) {
            var here = new ArrayList<>(this.here);
            here.removeAll(stuff);
            var across = new ArrayList<>(this.across);
            across.addAll(stuff);
            return new State(here, across);
        }

        State cross(List<String> stuff) {
            return torchHere() ? crossAcross(stuff) : crossHere(stuff);
        }

        State crossHere(List<String> stuff) {
            var swap = new State(across, here).crossAcross(stuff);
            return new State(swap.across, swap.here);
        }

        boolean isGoal() {
            return across.containsAll(ALL);
        }
    }

    public static void main(String[] args) {

        var ALL_HERE = new State(ALL, List.of());

        Function<List<String>, String> action = s -> String.join(" and ", s);

        Function<List<String>, List<List<String>>> combinations = stuff ->
        {
            List<List<String>> all = ListFun.allTriples(stuff);
            all.addAll(ListFun.allPairs(stuff));
            return all
                   .stream()
                   .filter(s -> s.contains("TORCH"))
                   .collect(Collectors.toList());
        };

        Function<State, List<Action<State>>> succ = s ->
                combinations
                        .apply(s.torchHere() ? s.here : s.across)
                        .stream()
                        .map(com -> new Action<>(action.apply(com),
                                                 s.cross(com)))
                        .collect(Collectors.toList());

        BiFunction<State, State, Integer> cost = (a, b) ->
                b.torchHere() ?
                        b.here.stream()
                                      .filter(it -> a.across.contains(it) && !it.equals("TORCH"))
                                      .map(it -> costs.get(it))
                                      .max(Integer::compareTo)
                                      .get()
                        : b.across.stream()
                                  .filter(it -> a.here.contains(it) && !it.equals("TORCH"))
                                  .map(it -> costs.get(it))
                                  .max(Integer::compareTo)
                                  .get();


        var sol = BranchBoundSearch.fromActions(succ, (a, b) -> new Log2<>(cost).apply(a, b))
                                   .findFirst(ALL_HERE, State::isGoal);
        System.out.println(sol);


    }
}
