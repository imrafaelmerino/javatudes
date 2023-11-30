package search.csp;

import types.ListFun;

import java.util.List;
import java.util.function.BiPredicate;

public final class BinaryConstraint<Variable, Value> implements Constraint<Variable, Value> {

    public final Variable var1;
    public final Variable var2;
    public final BiPredicate<Value, Value> constraint;

    public BinaryConstraint(Variable var1, Variable var2, BiPredicate<Value, Value> constraint) {
        this.var1 = var1;
        this.var2 = var2;
        this.constraint = constraint;
    }


    public static <Variable, Value> Iterable<Constraint<Variable, Value>> all(Variable variable1,
                                                                              Variable variable2,
                                                                              BiPredicate<Value, Value> predicate
                                                                             ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate));
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1)
                                );

    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6,
                                                                          Variable variable15,
                                                                          Variable variable16,
                                                                          BiPredicate<Value, Value> predicate7
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6),
                                 new BinaryConstraint<>(variable15, variable16, predicate7)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6,
                                                                          Variable variable15,
                                                                          Variable variable16,
                                                                          BiPredicate<Value, Value> predicate7,
                                                                          Variable variable17,
                                                                          Variable variable18,
                                                                          BiPredicate<Value, Value> predicate8
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6),
                                 new BinaryConstraint<>(variable15, variable16, predicate7),
                                 new BinaryConstraint<>(variable17, variable18, predicate8)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6,
                                                                          Variable variable15,
                                                                          Variable variable16,
                                                                          BiPredicate<Value, Value> predicate7,
                                                                          Variable variable17,
                                                                          Variable variable18,
                                                                          BiPredicate<Value, Value> predicate8,
                                                                          Variable variable19,
                                                                          Variable variable20,
                                                                          BiPredicate<Value, Value> predicate9
                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6),
                                 new BinaryConstraint<>(variable15, variable16, predicate7),
                                 new BinaryConstraint<>(variable17, variable18, predicate8),
                                 new BinaryConstraint<>(variable19, variable20, predicate9)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6,
                                                                          Variable variable15,
                                                                          Variable variable16,
                                                                          BiPredicate<Value, Value> predicate7,
                                                                          Variable variable17,
                                                                          Variable variable18,
                                                                          BiPredicate<Value, Value> predicate8,
                                                                          Variable variable19,
                                                                          Variable variable20,
                                                                          BiPredicate<Value, Value> predicate9,
                                                                          Variable variable21,
                                                                          Variable variable22,
                                                                          BiPredicate<Value, Value> predicate10

                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6),
                                 new BinaryConstraint<>(variable15, variable16, predicate7),
                                 new BinaryConstraint<>(variable17, variable18, predicate8),
                                 new BinaryConstraint<>(variable19, variable20, predicate9),
                                 new BinaryConstraint<>(variable21, variable22, predicate10)
                                );
    }

    public static <Variable, Value> List<Constraint<Variable, Value>> all(Variable variable1,
                                                                          Variable variable2,
                                                                          BiPredicate<Value, Value> predicate,
                                                                          Variable variable3,
                                                                          Variable variable4,
                                                                          BiPredicate<Value, Value> predicate1,
                                                                          Variable variable5,
                                                                          Variable variable6,
                                                                          BiPredicate<Value, Value> predicate2,
                                                                          Variable variable7,
                                                                          Variable variable8,
                                                                          BiPredicate<Value, Value> predicate3,
                                                                          Variable variable9,
                                                                          Variable variable10,
                                                                          BiPredicate<Value, Value> predicate4,
                                                                          Variable variable11,
                                                                          Variable variable12,
                                                                          BiPredicate<Value, Value> predicate5,
                                                                          Variable variable13,
                                                                          Variable variable14,
                                                                          BiPredicate<Value, Value> predicate6,
                                                                          Variable variable15,
                                                                          Variable variable16,
                                                                          BiPredicate<Value, Value> predicate7,
                                                                          Variable variable17,
                                                                          Variable variable18,
                                                                          BiPredicate<Value, Value> predicate8,
                                                                          Variable variable19,
                                                                          Variable variable20,
                                                                          BiPredicate<Value, Value> predicate9,
                                                                          Variable variable21,
                                                                          Variable variable22,
                                                                          BiPredicate<Value, Value> predicate10,
                                                                          Variable variable23,
                                                                          Variable variable24,
                                                                          BiPredicate<Value, Value> predicate11

                                                                         ) {
        return ListFun.mutableOf(new BinaryConstraint<>(variable1, variable2, predicate),
                                 new BinaryConstraint<>(variable3, variable4, predicate1),
                                 new BinaryConstraint<>(variable5, variable6, predicate2),
                                 new BinaryConstraint<>(variable7, variable8, predicate3),
                                 new BinaryConstraint<>(variable9, variable10, predicate4),
                                 new BinaryConstraint<>(variable11, variable12, predicate5),
                                 new BinaryConstraint<>(variable13, variable14, predicate6),
                                 new BinaryConstraint<>(variable15, variable16, predicate7),
                                 new BinaryConstraint<>(variable17, variable18, predicate8),
                                 new BinaryConstraint<>(variable19, variable20, predicate9),
                                 new BinaryConstraint<>(variable21, variable22, predicate10),
                                 new BinaryConstraint<>(variable23, variable24, predicate11)
                                );
    }


    @Override
    public String toString() {
        return "(" + var1 + ","+ var2 + ')';
    }
}
