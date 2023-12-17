package search.csp;


import search.*;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class AStarSearch1<State> {

    private final BiFunction<State, State, Integer> stepCost;
    private BranchBoundSearch1<State> branchBoundSearch;

    public static <State> AStarSearch1<State> fromActions(final Function<SearchPath<State>, List<Action<State>>> getSuccessors,
                                                          final BiFunction<State, State, Integer> stepCost,
                                                          final Function<State, Integer> heuristic
                                                         ) {
        return new AStarSearch1<>(getSuccessors, stepCost, heuristic);
    }

    public static <State> AStarSearch1<State> fromStates(final Function<SearchPath<State>, List<State>> getSuccessors,
                                                         final BiFunction<State, State, Integer> stepCost,
                                                         final Function<State, Integer> heuristic
                                                        ) {
        return new AStarSearch1<>(it -> getSuccessors.apply(it)
                                                     .stream()
                                                     .map(Action::new)
                                                     .collect(Collectors.toList()),
                                  stepCost,
                                  heuristic
        );
    }


    AStarSearch1(final Function<SearchPath<State>, List<Action<State>>> getSuccessors,
                 final BiFunction<State, State, Integer> stepCost,
                 final Function<State, Integer> heuristic
                ) {
        Objects.requireNonNull(heuristic);
        this.stepCost = Objects.requireNonNull(stepCost);
        branchBoundSearch =
                BranchBoundSearch1.fromActions(Objects.requireNonNull(getSuccessors),
                                              (a, b) -> stepCost.apply(a, b) + heuristic.apply(b)
                                             );
    }

    public SearchPathCost<State> findFirst(final List<State> initial,
                                           final Predicate<State> isGoal
                                          ) {
        var pathCost = branchBoundSearch.findFirst(initial, isGoal);
        return new SearchPathCost<>(pathCost.path(),
                                    pathCost.path().cost(stepCost)
        );
    }


}
