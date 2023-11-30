package search.csp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Fun {

    public static <Variable, Value> Predicate<Map<Variable, Value>> isAssignmentComplete(Iterable<Variable> vars) {
        return assignment -> {
            for (Variable var : vars) if (!assignment.containsKey(var)) return false;
            return true;
        };
    }

    public static <Variable, Value> Map<Variable, Value> assign(Variable variable,
                                                                Value value,
                                                                Map<Variable, Value> assignment
                                                               ) {

        var xs = new HashMap<>(assignment);
        xs.put(variable, value);
        return xs;
    }

    public static <Variable, Value> Map<Variable, List<Value>> reduce(Variable variable,
                                                                      List<Value> value,
                                                                      Map<Variable, List<Value>> domain
                                                                     ) {

        var xs = new HashMap<>(domain);
        xs.put(variable, value);
        return xs;
    }

    public static <Variable, Value> Map<Variable, Value> getOneValueDomainAssignments(java.util.Map<Variable, List<Value>> domain) {

        return domain
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey,
                                          e -> e.getValue().get(0)));
    }


    public static <Variable, Value> List<Variable> getNotAssigned(Iterable<Variable> vars,
                                                                  Map<Variable, Value> assignment
                                                                 ) {


        List<Variable> result = new ArrayList<>();
        for (Variable var : vars) if (!assignment.containsKey(var)) result.add(var);
        return result;

    }


}
