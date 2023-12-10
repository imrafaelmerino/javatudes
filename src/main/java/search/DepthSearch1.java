package search;


import types.ListFun;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DepthSearch1<State> extends Search1<State> {

    private final Function<SearchPath<State>, List<Action<State>>> getSuccessors;

    private DepthSearch1(final Function<SearchPath<State>, List<Action<State>>> getSuccessors) {
        this.getSuccessors = Objects.requireNonNull(getSuccessors);
    }

    public static <State> DepthSearch1<State> fromActions(final Function<SearchPath<State>, List<Action<State>>> getSuccessors) {
        return new DepthSearch1<>(getSuccessors);
    }

    public static <State> DepthSearch1<State> fromStates(final Function<SearchPath<State>, List<State>> getSuccessors) {
        return new DepthSearch1<>(s -> getSuccessors.apply(s)
                                                    .stream()
                                                    .map(Action::new)
                                                    .collect(Collectors.toList()));
    }

    @Override
    public SearchPath<State> findFirst(State initial,
                                       Predicate<SearchPath<State>> isGoal
                                      ) {
        return
                findFirst(initial,
                          getSuccessors,
                          isGoal,
                          (frontier, path) -> frontier.add(0, path)
                         );
    }






}


