package advent_of_code_2022.day18;

import types.FileParsers;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Day18_1 {


    record Cube(int x, int y, int z) {
        List<Cube> faces(){
            return List.of(new Cube(x+1,y,z),
                           new Cube(x-1,y,z),
                           new Cube(x,y+1,z),
                           new Cube(x,y-1,z),
                           new Cube(x,y,z+1),
                           new Cube(x,y,z-1));
        }
    }

    //static String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day18/input_test_2.txt";
    static String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day18/input.txt";
    static List<Cube> cubes = FileParsers.toListOfLines(input)
                                  .stream()
                                  .map(it -> it.split(","))
                                  .map(it -> new Cube(parseInt(it[0]), parseInt(it[1]), parseInt(it[2])))
                                  .toList();

    static int  xmax = cubes.stream().map(it->it.x).max(Integer::compareTo).get();
    static int  xmin = cubes.stream().map(it->it.x).min(Integer::compareTo).get();
    static int  ymax = cubes.stream().map(it->it.y).max(Integer::compareTo).get();
    static int  ymin = cubes.stream().map(it->it.y).min(Integer::compareTo).get();
    static int  zmax = cubes.stream().map(it->it.z).max(Integer::compareTo).get();
    static int  zmin = cubes.stream().map(it->it.z).min(Integer::compareTo).get();

    public static void main(String[] args) {


        int n =
                cubes
                .stream()
                .map(cube -> cube.faces()
                              .stream()
                              .filter(it->!cubes.contains(it))
                              .toList()
                              .size()
                 )
                .reduce(0, Integer::sum);

        System.out.println(n);

        int m = cubes.stream()
                     .map(cube -> cube.faces()
                                      .stream()
                                      .filter(it -> !cubes.contains(it) && connected_to_air(it))
                                      .toList()
                                      .size()
                         )
                     .reduce(0,Integer::sum);

        System.out.println(m);

    }



    static boolean connected_to_air(Cube cube){
        HashSet<Cube> seen = new HashSet<>();
        List<Cube> frontier = new ArrayList<>();
        frontier.add(cube);

        while (!frontier.isEmpty()){

           var c = frontier.remove(0);
            if(c.x >= xmax + 1
                    || c.x <= xmin -1
                    || c.y >= ymax +  1
                    || c.y <= ymin - 1
                    || c.z >= zmax + 1
                    || c.z <= zmin - 1
            ) return true;

            if(seen.contains(c))continue;
            seen.add(c);
            if(cubes.contains(c)) continue;

            c.faces()
             .forEach(it -> frontier.add(0,it));
        }

        return false;

    }
}
