package search.csp;

import types.ListFun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FindAll<Variable, Value> {

    private final Iterable<Variable> vars;
    private final Map<Variable, List<Value>> domain;
    private final Map<Variable, Value> assignments;
    private final ConstraintChecker<Variable, Value> checker;

    public FindAll(Iterable<Variable> vars,
                   Map<Variable, List<Value>> domain,
                   Iterable<Constraint<Variable, Value>> constraints
                  ) {
        this.vars = vars;
        this.domain = domain;
        this.assignments = Fun.getOneValueDomainAssignments(domain);
        this.checker = new ConstraintChecker<>(constraints);
    }



    public Stream<Map<Variable, Value>> stream() {
        List<Variable> xs = new ArrayList<>();
        for (Variable var : vars) xs.add(var);
        return backtracking(xs, assignments);
    }

    private Stream<Map<Variable, Value>> backtracking(List<Variable> vars,
                                                      Map<Variable, Value> assignments
                                                     ) {
        if (vars.isEmpty()) return Stream.empty();
        Variable head = ListFun.head(vars);


        return assignVariable(head,
                              ListFun.tail(vars),
                              domain.get(head),
                              assignments
                             );
    }

    private Stream<Map<Variable, Value>> assignVariable(Variable var,
                                                        List<Variable> others,
                                                        List<Value> domain,
                                                        Map<Variable, Value> assignments
                                                       ) {

        return domain.stream()
                     .map(value -> Fun.assign(var, value, assignments))
                     .filter(ass -> checker.test(var, ass))
                     .flatMap(ass -> Stream.of(Stream.of(ass),
                                               backtracking(others, ass)
                                              )
                             )
                     .flatMap(it -> it);


    }

}


