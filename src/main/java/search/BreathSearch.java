package search;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BreathSearch<State> extends Search<State> {

    private final Function<State, List<Action<State>>> getSuccessors;

    private BreathSearch(final Function<State, List<Action<State>>> getSuccessors) {
        this.getSuccessors = Objects.requireNonNull(getSuccessors);
    }

    public static <State> BreathSearch<State> fromActions(final Function<State, List<Action<State>>> getSuccessors) {
       return new BreathSearch<>(getSuccessors);
    }

    public static <State> BreathSearch<State> fromStates(final Function<State, List<State>> getSuccessors) {
        return new BreathSearch<>(s -> getSuccessors.apply(s)
                                                         .stream()
                                                         .map(Action::new)
                                                         .collect(Collectors.toList()));
    }

    @Override
    public SearchPath<State> findFirst(State initial,
                                       Predicate<State> isGoal
                                      ) {
        return findFirst(initial,
                         getSuccessors,
                         isGoal,
                         List::add
                        );
    }

    public Stream<SearchPath<State>> stream(State initial,
                                            BiPredicate<SearchPath<State>,State> predicate
                                            ){

        return Stream.iterate(List.of(SearchPath.of(new Action<>("", initial))),
                              it-> !it.isEmpty(),
                              paths -> {
                                  List<SearchPath<State>> collect = paths.stream()
                                                                         .flatMap(path -> getSuccessors.apply(path.last().state())
                                                                                                   .stream()
                                                                                                   .filter(suc -> predicate.test(path, suc.state()))
                                                                                                   .map(path::append))
                                                                         .collect(Collectors.toList());
                                  return collect;
                              }

                             )
                     .flatMap(Collection::stream);

    }

}


