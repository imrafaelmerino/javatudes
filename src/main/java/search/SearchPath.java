package search;

import types.ListFun;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SearchPath<State> implements Iterable<Action<State>>{

    public int cost(BiFunction<State, State, Integer> stepCost) {
        return ListFun.sliding(path, 2)
                      .map(pair -> stepCost.apply(pair.get(0).state(),
                                                  pair.get(1).state()
                                                 )
                          )
                      .reduce(0, Integer::sum);
    }

    private final List<Action<State>> path;

    public static <State> SearchPath<State> of(final Action<State> successor) {
        List<Action<State>> p = new ArrayList<>();
        p.add(successor);
        return new SearchPath<>(p);
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

    private SearchPath() {
        this.path = new ArrayList<>();
    }

    private SearchPath(final List<Action<State>> path) {
        this.path = path;
    }

    public static <State> SearchPath<State> empty() {
        return new SearchPath<>();
    }

    public SearchPath<State> append(final Action<State> actionState) {
        var copy = new ArrayList<>(path);
        copy.add(actionState);
        return new SearchPath<>(copy);
    }

    public boolean lastStateEq(State state) {
        return last().state().equals(state);
    }

    public boolean lastStateSuchThat(Predicate<State> predicate) {
        return predicate.test(last().state());
    }

    public boolean containsState(State state) {
        return path.stream()
                   .anyMatch(a -> a.state().equals(state));
    }

    public Stream<Action<State>> stream() {
        return path.stream();
    }

    public List<State> states() {
        return stream().map(Action::state)
                       .collect(Collectors.toList());
    }

    public Action<State> last() {
        return path.getLast();
    }

    public Action<State> first() {
        return path.getFirst();
    }

    public int size() {
        return path.size();
    }

    @Override
    public String toString() {
        return path.stream()
                   .map(Action::toString)
                   .collect(Collectors.joining(" -> "));
    }

    public SearchPath<State> prepend(Action<State> action) {
        var copy = new ArrayList<>(path);
        copy.add(0, action);
        return new SearchPath<>(copy);
    }

    @Override
    public Iterator<Action<State>> iterator() {
        return path.iterator();
    }
}


