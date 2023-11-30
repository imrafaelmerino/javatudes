package search.csp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class FindFirst<Variable,Value> {


    private final Iterable<Variable> vars;
    private final Map<Variable,List<Value>> domain;
    private final Map<Variable,Value> assignments;
    private final ConstraintChecker<Variable, Value> ckecker;
    private final Predicate<Map<Variable, Value>> isComplete;

    public FindFirst(Iterable<Variable> vars,
                     Map<Variable, List<Value>> domain,
                     Iterable<? extends Constraint<Variable, Value>> constraints) {
        this.vars = vars;
        this.domain = domain;
        this.assignments = Fun.getOneValueDomainAssignments(domain);
        this.ckecker = new ConstraintChecker<>(constraints);
        this.isComplete = Fun.isAssignmentComplete(vars);
    }




    public Map<Variable,Value> findFirst(){
        return backtracking(vars,assignments);
    }

    private Map<Variable,Value> backtracking(Iterable<Variable> vars,
                                             Map<Variable, Value> assignments
                                             ){
        if(isComplete.test(assignments)) return assignments;
        for (Variable var : vars) {
            for (Value value : domain.get(var)) {
                var newAssignments = Fun.assign(var,value,assignments);
                if(ckecker.test(var,newAssignments)){
                      var notAssigned = Fun.getNotAssigned(vars,newAssignments);
                      var result = backtracking(notAssigned,newAssignments);
                      if(!result.isEmpty()) return result;
                }
            }
            return new HashMap<>();
        }

        return new HashMap<>();

    }
}
