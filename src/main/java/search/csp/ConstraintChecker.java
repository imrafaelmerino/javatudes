package search.csp;

import types.ListFun;

import java.util.*;
import java.util.function.BiPredicate;

public class ConstraintChecker<Variable,Value> implements BiPredicate<Variable, Map<Variable,Value>> {


    private final Map<Variable,List<BinaryConstraint<Variable,Value>>> binary = new HashMap<>();
    private final Map<Variable,List<BasicConstraint<Variable,Value>>> basic = new HashMap<>();

    public ConstraintChecker(Iterable<? extends Constraint<Variable,Value>> constraints) {
        for (Constraint<Variable,Value> constraint : constraints) {
                if(constraint instanceof BasicConstraint<Variable,Value> bc)
                    basic.compute(bc.variable,(v,list) -> list == null ? ListFun.mutableOf(bc) : ListFun.append(list, bc));
                else if(constraint instanceof BinaryConstraint<Variable,Value> bc){
                    binary.compute(bc.var1,(v,list) -> list == null ? ListFun.mutableOf(bc) : ListFun.append(list, bc));
                    binary.compute(bc.var2,(v,list) -> list == null ? ListFun.mutableOf(bc) : ListFun.append(list, bc));
                }
            }

    }

    private boolean checkBasic(Variable variable, Map<Variable, Value> assigments){
        if(!basic.containsKey(variable))return true;
        return basic.get(variable)
                    .stream()
                    .allMatch(c -> c.constraint.test(assigments.get(variable)));
    }

    private boolean checkBinary(Variable variable, Map<Variable, Value> assigments){

        if(!binary.containsKey(variable))return true;
        return binary.get(variable)
              .stream()
              .filter(bc->assigments.containsKey(bc.var1) && assigments.containsKey(bc.var2))
              .allMatch(bc-> bc.constraint.test(assigments.get(bc.var1),
                                                assigments.get(bc.var2))
                       );

    }

    @Override
    public boolean test(Variable variable, Map<Variable, Value> assigments) {

        return checkBasic(variable,assigments) && checkBinary(variable,assigments);
    }


}
