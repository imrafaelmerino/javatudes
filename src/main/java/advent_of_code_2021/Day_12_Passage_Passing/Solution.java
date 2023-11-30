package advent_of_code_2021.Day_12_Passage_Passing;

import search.DepthSearch;
import search.SearchPath;
import types.FileParsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Solution {

    public static void main(String[] args) {
        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2021/Day_12_Passage_Passing/input.txt";
        Map<String, List<String>> caves = new HashMap<>();
        FileParsers.toListOfLines(input).forEach(line -> {
            var nodes = line.split("-");
            if (!caves.containsKey(nodes[0])) caves.put(nodes[0], new ArrayList<>());
            if (!caves.containsKey(nodes[1])) caves.put(nodes[1], new ArrayList<>());
            caves.get(nodes[0]).add(nodes[1]);
            caves.get(nodes[1]).add(nodes[0]);
        });

        caves.put("end",new ArrayList<>());

        var bb = DepthSearch.<String>fromStates(key -> caves.get(key)
                                                            .stream()
                                                            .filter(it -> !it.equals("start"))
                                                            .collect(Collectors.toList())
                                               );

        Predicate<String> notBorder = it -> !it.equals("start") && !it.equals("end");
        Predicate<String> isLower = cave -> cave.chars().allMatch(Character::isLowerCase);
        Predicate<String> isUpper = isLower.negate();


        BiPredicate<SearchPath<String>, String> filter =
                (path, cave) -> isUpper.test(cave) || !path.containsState(cave);


        var n = bb.stream("start", filter)
                  .filter(path -> path.lastStateEq("end"))
                  .count();

        BiPredicate<SearchPath<String>, String> filter1 =
                (path, cave) ->
                        isUpper.test(cave) ||
                                (
                                        !path.containsState(cave) || path.states()
                                                                         .stream()
                                                                         .filter(notBorder.and(isLower))
                                                                         .collect(Collectors.groupingBy(it->it,
                                                                                                            Collectors.counting())
                                                                                     )
                                                                             .values()
                                                                             .stream()
                                                                             .allMatch(it->it<2)




                                );

        var m = bb.stream("start", filter1)
                  .filter(path -> path.lastStateEq("end"))
                  .count();

        System.out.println(n);
        System.out.println(m);
    }
}
