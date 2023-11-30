package advent_of_code_2022.day1;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.*;

public class Day_1 {


    public static void main(String[] args) {
        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/test_1.txt";
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day1/input_1.txt";
        List<String> supplies = readFile(new File(input));
        Map<Integer, List<Integer>> elfSuppliers = parse(supplies);

        Optional<Map.Entry<Integer, List<Integer>>> maxEntry =
                elfSuppliers
                        .entrySet()
                        .stream()
                        .max(Comparator.comparingLong(it -> sum(it.getValue())));

        System.out.println(sum(maxEntry.get().getValue()));

        Integer total = elfSuppliers
                .entrySet()
                .stream()
                .sorted(Comparator.comparingLong(it -> -sum(it.getValue())))
                .limit(3)
                .map(it -> sum(it.getValue()))
                .reduce(0, Integer::sum);

        System.out.println(total);

    }

    private static Integer sum(List<Integer> list) {
        return list
                .stream()
                .reduce(0, Integer::sum);
    }

    public static Map<Integer, List<Integer>> parse(List<String> allSupplies) {
        return parse(allSupplies,
                     1,
                     new ArrayList<>(),
                     new HashMap<>()
                    );
    }

    public static Map<Integer, List<Integer>> parse(List<String> allSupplies,
                                                    int elf,
                                                    List<Integer> elfSupplies,
                                                    Map<Integer, List<Integer>> result
                                                   ) {
        if (allSupplies.isEmpty()) {
            result.put(elf,
                       elfSupplies
                      );
            return result;
        }
        String line = allSupplies.get(0);
        if (line.isBlank()) {
            result.put(elf,
                       elfSupplies
                      );
            return parse(allSupplies.subList(1,
                                             allSupplies.size()
                                            ),
                         elf + 1,
                         new ArrayList<>(),
                         result
                        );
        }
        elfSupplies.add(Integer.parseInt(line));
        return parse(allSupplies.subList(1,
                                         allSupplies.size()
                                        ),
                     elf,
                     elfSupplies,
                     result
                    );
    }

    public static List<String> readFile(File file) {

        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
