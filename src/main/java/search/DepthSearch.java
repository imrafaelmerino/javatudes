package search;


import types.ListFun;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DepthSearch<State> extends Search<State> {

    private final Function<State, List<Action<State>>> getSuccessors;

    private DepthSearch(final Function<State, List<Action<State>>> getSuccessors) {
        this.getSuccessors = Objects.requireNonNull(getSuccessors);
    }

    public static <State> DepthSearch<State> fromActions(final Function<State, List<Action<State>>> getSuccessors) {
        return new DepthSearch<>(getSuccessors);
    }

    public static <State> DepthSearch<State> fromStates(final Function<State, List<State>> getSuccessors) {
        return new DepthSearch<>(s -> getSuccessors.apply(s)
                                                   .stream()
                                                   .map(Action::new)
                                                   .collect(Collectors.toList()));
    }

    @Override
    public SearchPath<State> findFirst(State initial,
                                       Predicate<State> isGoal
                                      ) {
        return
                findFirst(initial,
                          getSuccessors,
                          isGoal,
                          (frontier,path) -> frontier.add(0,path)
                         );
    }

    public Stream<SearchPath<State>> stream(State initial,
                                            BiPredicate<SearchPath<State>, State> predicate
                                           ){
        return stream(SearchPath.of(new Action<>("",initial)),predicate);
    }
    public Stream<SearchPath<State>> stream(SearchPath<State> initial,
                                            BiPredicate<SearchPath<State>, State> predicate
                                           ) {
        return Stream.concat(Stream.of(initial),
                             rec(getSuccessors.apply(initial.last().state())
                                              .stream()
                                              .filter(suc -> predicate.test(initial, suc.state()))
                                              .map(initial::append)
                                              .collect(Collectors.toList()),
                                 predicate
                                )
                            );

    }

    private Stream<SearchPath<State>> rec(List<SearchPath<State>> xs,
                                         BiPredicate<SearchPath<State>, State> predicate) {
        if (xs.isEmpty()) return Stream.empty();
        var sp = xs.getFirst();
        return Stream.of(stream(sp, predicate),
                         rec(ListFun.tail(xs), predicate)
                        )
                     .flatMap(it -> it);
    }


}


