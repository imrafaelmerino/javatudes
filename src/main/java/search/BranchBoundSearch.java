package search;

import fun.tuple.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BranchBoundSearch<State> {

    private final Function<State, List<Action<State>>> getSuccessors;
    private final BiFunction<State, State, Integer> stepCost;

    private BranchBoundSearch(final Function<State, List<Action<State>>> getSuccessors,
                              final BiFunction<State, State, Integer> stepCost
                             ) {
        this.getSuccessors = Objects.requireNonNull(getSuccessors);
        this.stepCost = Objects.requireNonNull(stepCost);
    }


    public static <State> BranchBoundSearch<State> fromActions(Function<State, List<Action<State>>> getSuccessors,
                                                               BiFunction<State, State, Integer> stepCost
                                                              ) {
        return new BranchBoundSearch<>(getSuccessors, stepCost);
    }

    public static <State> BranchBoundSearch<State> fromStates(Function<State, List<State>> getSuccessors,
                                                              BiFunction<State, State, Integer> stepCost
                                                             ) {
        return new BranchBoundSearch<>(s -> getSuccessors.apply(s)
                                                         .stream()
                                                         .map(Action::new)
                                                         .collect(Collectors.toList()),
                                       stepCost
        );
    }


    private SearchPath<State> getPath(HashMap<Action<State>, Action<State>> previous,
                                      Action<State> s,
                                      SearchPath<State> path
                                     ) {
        var p = previous.get(s);
        if (p == null) return path;
        return getPath(previous, p, path.prepend(p));
    }

    public SearchPathCost<State> findFirst(final State initial,
                                           final Predicate<State> isGoal
                                          ) {
        var first = Pair.of(new Action<>(initial), 0);
        var frontier = new PriorityQueue<Pair<Action<State>, Integer>>(Comparator.comparingInt(Pair::second));
        var mapCost = new HashMap<State, Integer>();
        mapCost.put(initial, 0);
        var previous = new HashMap<Action<State>, Action<State>>();
        previous.put(new Action<>(initial), null);
        frontier.add(first);

        while (!frontier.isEmpty()) {
            var pair = frontier.remove();
            State state = pair.first().state();
            if (isGoal.test(state))
                return new SearchPathCost<>(getPath(previous,
                                                    pair.first(),
                                                    SearchPath.of(pair.first())
                                                   ),
                                            mapCost.get(state));
            for (var successor : getSuccessors.apply(state)) {
                var g = mapCost.get(state) + stepCost.apply(state, successor.state());
                if (!mapCost.containsKey(successor.state()) || g < mapCost.get(successor.state())) {
                    frontier.add(Pair.of(successor, g));
                    mapCost.put(successor.state(), g);
                    previous.put(successor, pair.first());
                }
            }
        }

        return new SearchPathCost<>(SearchPath.empty(), -1);
    }

    public Stream<SearchPathCost<State>> stream(State initial,
                                                BiPredicate<SearchPath<State>, State> filter
                                               ) {

        return Stream.iterate(List.of(new SearchPathCost<>(SearchPath.of(new Action<>("START", initial)),
                                                           0
                                      )
                                     ),
                              it -> !it.isEmpty(),
                              paths ->
                                      paths.stream()
                                           .flatMap(pathCost ->
                                                    {
                                                        var path = pathCost.path();
                                                        var state = path.last().state();
                                                        return getSuccessors.apply(state)
                                                                            .stream()
                                                                            .filter(suc -> filter.test(path,
                                                                                                       suc.state()
                                                                                                      )
                                                                                   )
                                                                            .map(suc -> new SearchPathCost<>(path.append(suc),
                                                                                                             pathCost.cost() + stepCost.apply(state,
                                                                                                                                              suc.state()
                                                                                                                                             )
                                                                                 )
                                                                                );
                                                    }
                                                   )
                                           .sorted(Comparator.comparingInt(SearchPathCost::cost))
                                           .collect(Collectors.toList())

                             ).flatMap(Collection::stream);

    }

}


