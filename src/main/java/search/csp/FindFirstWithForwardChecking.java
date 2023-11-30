package search.csp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FindFirstWithForwardChecking<Variable, Value> {


    private final Iterable<Variable> vars;
    private final Map<Variable, List<Value>> domain;
    private final Map<Variable, Value> assignments;
    private final ConstraintChecker<Variable, Value> ckecker;
    private final Predicate<Map<Variable, Value>> isComplete;
    private final Map<Variable, List<Variable>> get_neighbors;

    public FindFirstWithForwardChecking(Iterable<Variable> vars,
                                        Map<Variable, List<Value>> domain,
                                        Iterable<? extends Constraint<Variable, Value>> constraints,
                                        Map<Variable, List<Variable>> get_neighbors
                                       ) {
        this.vars = vars;
        this.domain = domain;
       this.assignments = Fun.getOneValueDomainAssignments(domain);
        this.ckecker = new ConstraintChecker<>(constraints);
        this.isComplete = Fun.isAssignmentComplete(vars);
        this.get_neighbors = get_neighbors;
    }



    private Map<Variable, List<Value>> reduceDomain(Variable variable,
                                                    Map<Variable, List<Value>> domain,
                                                    Map<Variable, Value> assignments
                                                   ) {
        var neighborsNotAssigned = get_neighbors.get(variable)
                                                .stream()
                                                .filter(n -> !assignments.containsKey(n))
                                                .toList();
        var newDomain = domain;
        for (Variable v : neighborsNotAssigned) {
            var varDomain = domain.get(v);
            var varDomainReduced = varDomain.stream()
                                           .filter(value -> ckecker.test(v,Fun.assign(v,value,assignments)))
                                           .collect(Collectors.toList());
            newDomain = Fun.reduce(v,varDomainReduced,domain);
        }

        return newDomain;
        
    }


    /**
     * private def reduce_domain(variable: Variable, domain: Domain, assignments: Assignments) =
     *
     * @tailrec def reduce_neighbors_domain(neighbors: Seq[Variable], assignments: Assignments, result: Domain): Domain =
     * if neighbors.isEmpty then result
     * else
     * val neighbor = neighbors.head
     * val values = domain(neighbor)
     * val reduced_domain = filter_values(values, v => is_consistent(neighbor, c_assign_value(neighbor, assignments)(v)))
     * if reduced_domain.isEmpty then Map.empty
     * else reduce_neighbors_domain(neighbors.tail, assignments, result.updated(neighbor, reduced_domain.toSeq))
     * <p>
     * val neighbors = get_neighbors(variable).filter(!assignments.contains(_))
     * reduce_neighbors_domain(neighbors, assignments, domain)
     **/


    public Map<Variable, Value> findFirst() {
        return backtracking(vars, assignments, domain);
    }

    private Map<Variable, Value> backtracking(Iterable<Variable> vars,
                                              Map<Variable, Value> assignments,
                                              Map<Variable, List<Value>> p_domain
                                             ) {
        if (isComplete.test(assignments)) return assignments;
        for (Variable var : vars) {
            for (Value value : p_domain.get(var)) {
                var newAssignments = Fun.assign(var, value, assignments);
                if (ckecker.test(var, newAssignments)) {
                    var notAssigned = Fun.getNotAssigned(vars, newAssignments);
                    var result = backtracking(notAssigned, newAssignments,reduceDomain(var,domain,assignments));
                    if (!result.isEmpty()) return result;
                }
            }
            return new HashMap<>();
        }

        return new HashMap<>();

    }
}
