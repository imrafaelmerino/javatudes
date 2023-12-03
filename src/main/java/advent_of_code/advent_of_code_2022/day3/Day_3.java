package advent_of_code.advent_of_code_2022.day3;

import types.FileParsers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day_3 {


    public static void main(String[] args) throws IOException {

        part1();

        part2();

    }

    private static void part2() {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day3/input_3.txt";

        List<String> rucksacks = FileParsers.toListOfLines(input);

        Optional<Integer> sum = Stream.iterate(List.of(0, 3),
                                               indexes -> List.of(indexes.get(0) + 3,
                                                                  indexes.get(1) + 3
                                                                 )
                                              )
                                      .takeWhile(indexes ->
                                                         indexes.get(1) <= rucksacks.size())
                                      .map(indexes -> rucksacks.subList(indexes.get(0),
                                                                        indexes.get(1)
                                                                       )
                                          )
                                      .map(group -> {
                                          Set<Character> rucksack1 = toSet(group.get(0));
                                          Set<Character> rucksack2 = toSet(group.get(1));
                                          Set<Character> rucksack3 = toSet(group.get(2));
                                          rucksack1.retainAll(rucksack2);
                                          rucksack1.retainAll(rucksack3);
                                          return rucksack1.stream().toList().get(0);
                                      })
                                      .map(Day_3::getPriority)
                                      .reduce(Integer::sum);

        System.out.println(sum.get());
    }

    private static void part1() {
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day3/input_3.txt";

        List<String> rucksacks = FileParsers.toListOfLines(input);

        Optional<Integer> sumPriorities = rucksacks.stream()
                                                   .map(rucksack ->
                                                        {
                                                            int nItems = rucksack.length() / 2;
                                                            return List.of(rucksack.substring(0, nItems),
                                                                           rucksack.substring(nItems)
                                                                          );
                                                        }
                                                       )
                                                   .map(compartments -> {
                                                       Set<Character> s1 = toSet(compartments.get(0));
                                                       Set<Character> s2 = toSet(compartments.get(1));
                                                       s1.retainAll(s2);
                                                       return s1.stream().toList().get(0);
                                                   })
                                                   .peek(System.out::println)
                                                   .map(Day_3::getPriority)
                                                   .peek(System.out::println)
                                                   .reduce(Integer::sum);


        System.out.println(sumPriorities.get());
    }

    static Set<Character> toSet(String letters) {
        return letters.chars().mapToObj(it -> ((char) it)).collect(Collectors.toSet());
    }

    static String lowerCases = "abcdefghijklmnopqrstuvwxyz";

    static String upperCases = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static int getPriority(char character) {
        String letter = String.valueOf(character);
        int i = lowerCases.indexOf(letter);
        if (i != -1) return i + 1;
        int j = upperCases.indexOf(letter);
        if (j == -1) throw new IllegalArgumentException(letter);
        return j + 1 + 26;
    }


}
