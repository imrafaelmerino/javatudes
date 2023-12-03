package javatudes.four_color_map;


import search.csp.BinaryConstraint;
import search.csp.FindFirst;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class ColoringMap {


    public static void main(String[] args) throws IOException, InterruptedException {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/etudes/four_color_map/provinces.txt";

        var provinces = Files.readAllLines(Path.of(input));

        Map<String, List<String>> provinceNeighborsMap =
                provinces.stream()
                         .map(ColoringMap::toEntry)
                         .collect(Collectors.toMap(it -> it.getKey(),
                                                   it -> it.getValue()
                                                  )
                                 );

        var colors = List.of("red", "blue", "green", "orange");

        Map<String, List<String>> domain = provinceNeighborsMap
                .keySet()
                .stream()
                .collect(Collectors.toMap(it -> it,
                                          it -> colors
                                         ));

        System.out.println(provinceNeighborsMap);
        System.out.println(domain);


        List<BinaryConstraint<String, String>> constraints =
                provinceNeighborsMap.entrySet()
                                    .stream()
                                    .flatMap(e -> {
                                        var province = e.getKey();
                                        var neighbors = e.getValue();
                                        return neighbors.stream()
                                                        .map(n -> new BinaryConstraint<String, String>(province,
                                                                                                       n,
                                                                                                       (a, b) -> !a.equals(b)));
                                    })
                                    .toList();

        System.out.println(constraints);

        var solution = new FindFirst<>(
                                       provinceNeighborsMap.keySet(),
                                       domain,
                                       constraints
        ).findFirst();

        System.out.println(solution);

        Files.writeString(new File("./output.svg").toPath(),
                          colorMap(solution),
                          StandardOpenOption.CREATE);


    }

    private static Map.Entry<String, List<String>> toEntry(String line) {
        var tokens = line.split("->");
        var province = tokens[0].trim();
        var neighbors = Arrays.stream(tokens[1].split(","))
                              .map(String::trim)
                              .toList();
        return new AbstractMap.SimpleEntry<>(province, neighbors);
    }


    private static String colorMap(Map<String,String> assignments) {
        var map = SpainMap.SVG;
        for (Map.Entry<String, String> entry : assignments.entrySet()) {
            var province = entry.getKey();
            var color = entry.getValue();
            map = map.replaceFirst("#\\(" + province + "\\)", color);
        }

        return map;

    }
}

