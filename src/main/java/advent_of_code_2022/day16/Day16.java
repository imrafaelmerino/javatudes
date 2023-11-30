package advent_of_code_2022.day16;

import fun.tuple.Pair;
import fun.tuple.Triple;
import io.vavr.collection.List;
import search.FloydWarshall;
import types.FileParsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day16 {


    record Valve(String name, int pressure, boolean opened) {
        public Valve close() {
            return new Valve(name, pressure, false);
        }
    }

    record State(Valve valve, int accTime, int accPressure) {
        @Override
        public String toString() {
            return valve.name + " " + accTime + " " + accPressure;
        }
    }

    static String regex = "^Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)$";


    static String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day16/input.txt";

    static Map<String, List<String>> nodes = new HashMap<>();
    static Map<String,Integer> pressures = new HashMap<>();

    static {
        FileParsers.toListOfLineMatchers(input, regex)
                   .forEach(m -> {
                       var xs = Arrays.stream(m.group(3)
                                               .split(","))
                                      .map(String::trim)
                                      .collect(List.collector());
                       String name = m.group(1).trim();
                       nodes.put(name,
                                 xs
                                );
                       pressures.put(name, Integer.parseInt(m.group(2).trim()));

                   });
    }
    static Map<Pair<String, String>, Integer> distances = FloydWarshall.search(nodes);



    public static void main(String[] args) {


        var xs = nodes.entrySet().stream()
                      .filter(it->pressures.get(it.getKey()) > 0)
                      .map(it->it.getKey())
                      .collect(List.collector());


        System.out.println(xs);

        System.out.println(max("AA",
                                xs,
                               30
                               )
                          );

        System.out.println(max2(Pair.of("AA","AA"),
                               xs,
                               Pair.of(26,26)
                              )
                          );
    }


    static Integer max(String start,
                       List<String> rest,
                       int time){
        int max = 0;
        for (String valve : rest) {
            var minutes = distances.get(Pair.of(start,valve));
            var pressure = pressures.get(valve);
            if(time - minutes - 1 > 0)
                max = Math.max(max, (time-minutes-1) * pressure + max(valve, rest.remove(valve), time - minutes -1));
        }
        return max;
    }

    static Map<Triple<String,List<String>,Integer>,Integer> cache = new HashMap<>();
    static Integer maxCache(String start,
                            List<String> rest,
                            int time){
        var a = Triple.of(start,rest,time);
        if(cache.containsKey(a))
            return cache.get(a);
        int max = 0;
        for (String valve : rest) {
            var minutes = distances.get(Pair.of(start,valve));
            var pressure = pressures.get(valve);
            if(time - minutes - 1 > 0)
                max = Math.max(max, (time-minutes-1) * pressure + max(valve, rest.remove(valve), time - minutes -1));
        }
        cache.put(a,max);
        return max;
    }



    static Map<Triple<Pair<String,String>,List<String>,Pair<Integer,Integer>>,Integer> c = new HashMap<>();
    static Integer max2(Pair<String,String> start,
                        List<String> rest,
                        Pair<Integer,Integer> time){
        var a = Triple.of(start,rest,time);
        if(c.containsKey(a))
            return c.get(a);
        int max = 0;
        for (String valve : rest) {
            for(String other: rest.remove(valve)){
                var minutes = distances.get(Pair.of(start.first(),valve));
                var minutesOther = distances.get(Pair.of(start.second(),other));
                if((time.first() - minutes - 1) > 0 && (time.second() - minutesOther - 1) > 0) {
                    max = Math.max(max,
                                           (time.second() - minutesOther - 1) * pressures.get(other)
                                           + (time.first() - minutes - 1) * pressures.get(valve)
                                           + max2(Pair.of(valve,other),
                                                  rest.remove(valve).remove(other),
                                                  Pair.of(time.first() - minutes -  1,time.second() - minutesOther -  1))
                                  );
                }
                else if((time.first() - minutes - 1) > 0) {
                    max = Math.max(max,
                                           (time.first() - minutes - 1) * pressures.get(valve)
                                           + maxCache(valve,
                                                      rest.remove(valve).remove(other),
                                                     time.first() - minutes -  1
                                                     )
                                  );
                }
                else if((time.second() - minutes - 1) > 0) {
                    max = Math.max(max,
                                   (time.second() - minutes - 1) * pressures.get(other)
                                           + maxCache(other,
                                                      rest.remove(valve).remove(other),
                                                      time.second() - minutesOther -1
                                                     )
                                  );
                }

            }

        }
        c.put(a,max);
        return max;
    }



}
