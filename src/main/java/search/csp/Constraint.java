package search.csp;

import types.ListFun;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public sealed interface Constraint<Variable, Value> permits BasicConstraint, BinaryConstraint {


    static <Variable, Value> List<Constraint<Variable, Value>> allDifferent(Iterable<Variable> vars) {
        return allSuchThat(vars, (v1, v2) -> !v1.equals(v2));
    }

    static <Variable, Value> List<Constraint<Variable, Value>> allSuchThat(Iterable<Variable> vars,
                                                                           BiPredicate<Value, Value> predicate
                                                                          ) {
        List<Variable> list = new ArrayList<>();
        for (Variable var : vars) list.add(var);

        return ListFun.allPairs(list)
                      .stream()
                      .map(pair -> new BinaryConstraint<>(pair.get(0),
                                                          pair.get(1),
                                                          predicate
                           )
                          )
                      .collect(Collectors.toList());
    }


}
