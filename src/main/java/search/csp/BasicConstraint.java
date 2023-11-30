package search.csp;

import types.ListFun;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class BasicConstraint<Variable, Value> implements Constraint<Variable, Value> {

    public final Variable variable;
    public final Predicate<Value> constraint;

    public BasicConstraint(Variable variable, Predicate<Value> constraint) {
        this.variable = Objects.requireNonNull(variable);
        this.constraint = Objects.requireNonNull(constraint);
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)));
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1))
                                );

    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2))
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2,
                                                                          Variable variable3,
                                                                          Value value3
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2)),
                                 new BasicConstraint<>(variable3, v -> v.equals(value3))
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2,
                                                                          Variable variable3,
                                                                          Value value3,
                                                                          Variable variable4,
                                                                          Value value4
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2)),
                                 new BasicConstraint<>(variable3, v -> v.equals(value3)),
                                 new BasicConstraint<>(variable4, v -> v.equals(value4))
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2,
                                                                          Variable variable3,
                                                                          Value value3,
                                                                          Variable variable4,
                                                                          Value value4,
                                                                          Variable variable5,
                                                                          Value value5
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2)),
                                 new BasicConstraint<>(variable3, v -> v.equals(value3)),
                                 new BasicConstraint<>(variable4, v -> v.equals(value4)),
                                 new BasicConstraint<>(variable5, v -> v.equals(value5))
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2,
                                                                          Variable variable3,
                                                                          Value value3,
                                                                          Variable variable4,
                                                                          Value value4,
                                                                          Variable variable5,
                                                                          Value value5,
                                                                          Variable variable6,
                                                                          Value value6
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2)),
                                 new BasicConstraint<>(variable3, v -> v.equals(value3)),
                                 new BasicConstraint<>(variable4, v -> v.equals(value4)),
                                 new BasicConstraint<>(variable5, v -> v.equals(value5)),
                                 new BasicConstraint<>(variable6, v -> v.equals(value6))
                                );
    }


    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable,
                                                                          Value value,
                                                                          Variable variable1,
                                                                          Value value1,
                                                                          Variable variable2,
                                                                          Value value2,
                                                                          Variable variable3,
                                                                          Value value3,
                                                                          Variable variable4,
                                                                          Value value4,
                                                                          Variable variable5,
                                                                          Value value5,
                                                                          Variable variable6,
                                                                          Value value6,
                                                                          Variable variable7,
                                                                          Value value7
                                                                         ) {
        return ListFun.mutableOf(new BasicConstraint<>(variable, v -> v.equals(value)),
                                 new BasicConstraint<>(variable1, v -> v.equals(value1)),
                                 new BasicConstraint<>(variable2, v -> v.equals(value2)),
                                 new BasicConstraint<>(variable3, v -> v.equals(value3)),
                                 new BasicConstraint<>(variable4, v -> v.equals(value4)),
                                 new BasicConstraint<>(variable5, v -> v.equals(value5)),
                                 new BasicConstraint<>(variable6, v -> v.equals(value6)),
                                 new BasicConstraint<>(variable7, v -> v.equals(value7))
                                );
    }


}
