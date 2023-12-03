package javatudes;

import search.csp.*;

import types.PersistentGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


public class Zebra_Puzzle {


    public static void main(String[] args) {
        var nationalities = List.of("englishman", "japanese", "norwegian", "ukrainian", "spaniard");
        var cigarettes = List.of("parliaments", "lucky", "kools", "chesterfields", "oldGold");
        var drink = List.of("coffee", "milk", "tea", "juice", "water");
        var colors = List.of("green", "blue", "yellow", "red", "ivory");
        var animals = List.of("snails", "fox", "dog", "horse", "zebra");
        var variables = new ArrayList<String>();
        variables.addAll(nationalities);
        variables.addAll(cigarettes);
        variables.addAll(drink);
        variables.addAll(colors);
        variables.addAll(animals);
        List<Integer> houses = List.of(1, 2, 3, 4, 5);
        var domain = variables.stream()
                              .collect(Collectors.toMap(var -> var,
                                                        var -> houses
                                                       ));


        BiPredicate<Integer, Integer> equals = Integer::equals;

        BiPredicate<Integer, Integer> next = (a, b) -> Math.abs(a - b) == 1;

        BiPredicate<Integer, Integer> right = (a, b) -> a - b == 1;

        List<Constraint<String, Integer>> constraints = BinaryConstraint.all("englishman", "red", equals,
                                                                             "spaniard", "dog", equals,
                                                                             "coffee", "green", equals,
                                                                             "ukrainian", "tea", equals,
                                                                             "green", "ivory", right,
                                                                             "oldGold", "snails", equals,
                                                                             "kools", "yellow", equals,
                                                                             "chesterfields", "fox", next,
                                                                             "kools", "horse", next,
                                                                             "lucky", "juice", equals,
                                                                             "japanese", "parliaments", equals,
                                                                             "norwegian", "blue", next
                                                                            );
        constraints.addAll(BasicConstraint.all("milk", 3,
                                               "norwegian", 1
                                              )
                          );
        constraints.addAll(Constraint.allDifferent(nationalities));
        constraints.addAll(Constraint.allDifferent(cigarettes));
        constraints.addAll(Constraint.allDifferent(drink));
        constraints.addAll(Constraint.allDifferent(colors));
        constraints.addAll(Constraint.allDifferent(animals));


        var solution = new FindFirst<>(variables, domain, constraints).findFirst();


        toString(solution);


       /* var solutions =
                new BacktrackingStream<>(variables, domain, constraints)
                        .getAssignments()
                        .filter(ass -> Fun.<String, Integer>isAssignmentComplete(variables).test(ass))
                        .toList();

        solutions.forEach(it -> {
            toString(it);
            System.out.println("\n");
        });*/


    }

    public static void toString(Map<String, Integer> assignments) {

        var a =
                assignments.entrySet()
                           .stream()
                           .collect(Collectors.groupingBy(e -> e.getValue(),
                                                          Collectors.toList()
                                                         )
                                   );


        List<List<Object>> ys = a.values()
                                 .stream()
                                 .map(it -> {
                                     var x = new ArrayList<>();
                                     x.add(String.format("%-15s", it.get(0)
                                                                    .getValue())
                                          );
                                     x.addAll(it.stream()
                                                .map(i -> String.format("%-15s", i.getKey()))
                                                .collect(Collectors.toList())
                                             );
                                     return x;
                                 })
                                 .collect(Collectors.toList());

        PersistentGrid.fromColumns(ys).printRows();

    }
}
