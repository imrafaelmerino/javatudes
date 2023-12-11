package search;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class Search<State> {

    public abstract SearchPath<State> findFirst(State initial,
                                                Predicate<State> isGoal
                                               );

    /**
     * @param initial           the initial state
     * @param getSuccessors     function that takes a state and produces pairs with the new states and the actions to
     *                          reach them
     * @param isGoal            predicate to check if we are done and a solution has been found
     * @param addPathToFrontier consumer that add new paths into the consumer
     * @return the first path to a solution of the problem
     */
    protected SearchPath<State> findFirst(State initial,
                                          Function<State, List<Action<State>>> getSuccessors,
                                          Predicate<State> isGoal,
                                          BiConsumer<List<SearchPath<State>>, SearchPath<State>> addPathToFrontier
                                         ) {
        var initialPath = SearchPath.of(new Action<>("", initial));
        if (isGoal.test(initial)) return initialPath;
        var explored = new HashSet<State>();
        var frontier = new ArrayList<SearchPath<State>>();
        frontier.add(initialPath);
        while (!frontier.isEmpty()) {
            var path = frontier.remove(0);
            var last = path.last();
            var successors = getSuccessors.apply(last.state());
            for (var successor : successors) {
                if (!explored.contains(successor.state())) {
                    explored.add(successor.state());
                    var path1 = path.append(successor);
                    if (isGoal.test(successor.state())) return path1;
                    addPathToFrontier.accept(frontier,
                                             path1
                                            );
                }
            }
        }
        return SearchPath.empty();
    }
}
