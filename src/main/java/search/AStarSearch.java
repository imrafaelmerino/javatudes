package search;


import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class AStarSearch<State> {

    private final BiFunction<State, State, Integer> stepCost;
    private BranchBoundSearch<State> branchBoundSearch;

    public static <State> AStarSearch<State> fromActions(final Function<State, List<Action<State>>> getSuccessors,
                                                         final BiFunction<State, State, Integer> stepCost,
                                                         final Function<State, Integer> heuristic
                                                        ) {
        return new AStarSearch<>(getSuccessors, stepCost, heuristic);
    }

    public static <State> AStarSearch<State> fromStates(final Function<State, List<State>> getSuccessors,
                                                        final BiFunction<State, State, Integer> stepCost,
                                                        final Function<State, Integer> heuristic
                                                       ) {
        return new AStarSearch<>(it -> getSuccessors.apply(it)
                                                    .stream()
                                                    .map(Action::new)
                                                    .collect(Collectors.toList()),
                                 stepCost,
                                 heuristic
        );
    }


    AStarSearch(final Function<State, List<Action<State>>> getSuccessors,
                final BiFunction<State, State, Integer> stepCost,
                final Function<State, Integer> heuristic
               ) {
        Objects.requireNonNull(heuristic);
        this.stepCost = Objects.requireNonNull(stepCost);
        branchBoundSearch =
                BranchBoundSearch.fromActions(Objects.requireNonNull(getSuccessors),
                                              (a, b) -> stepCost.apply(a, b) + heuristic.apply(b)
                                             );
    }

    public SearchPathCost<State> findFirst(final State initial,
                                           final Predicate<State> isGoal
                                          ) {
        var pathCost = branchBoundSearch.findFirst(initial, isGoal);
        return new SearchPathCost<>(pathCost.path(),
                                    pathCost.path().cost(stepCost)
        );
    }


}
