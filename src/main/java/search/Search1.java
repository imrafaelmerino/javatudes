package search;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class Search1<State> {

    public abstract SearchPath<State> findFirst(State initial,
                                                Predicate<SearchPath<State>> isGoal
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
                                          Function<SearchPath<State>, List<Action<State>>> getSuccessors,
                                          Predicate<SearchPath<State>> isGoal,
                                          BiConsumer<List<SearchPath<State>>, SearchPath<State>> addPathToFrontier
                                         ) {
        var initialPath = SearchPath.of(new Action<>("", initial));
        var explored = new HashSet<State>();
        var frontier = new ArrayList<SearchPath<State>>();
        frontier.add(initialPath);
        while (!frontier.isEmpty()) {
            var path = frontier.remove(0);
            System.out.println(path);
            var last = path.last();
            if (isGoal.test(path)) return path;
            if(!last.state().equals(initial))explored.add(last.state());
            var successors = getSuccessors.apply(path);
            for (var successor : successors) {
                if (!explored.contains(successor.state())) {
                    var path1 = path.append(successor);
                    addPathToFrontier.accept(frontier,
                                             path1
                                            );
                }
            }
        }
        return SearchPath.empty();
    }
}
