package advent_of_code.advent_of_code_2022.day21;

import types.FileParsers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Day21 {


    private Map<String, String> map = new HashMap<>();

    public Day21(String input) {

        FileParsers.toListOfLines(input)
                   .forEach(line -> {
                       var tokens = line.split(":");
                       map.put(tokens[0], tokens[1].trim());
                   });
    }

    public Object solve1() {

        return eval("root");

    }

    public Object solve2() {
        //root: jsrw + ptvl


        Function<Long, Long> f = n ->
        {
            map.put("humn", n + "");
            Long jsrw = eval("jsrw");
            System.out.println("fn " + jsrw);
            return jsrw;
        };

        var target = eval("ptvl");
        System.out.println("target " + target);
        var lo = 0L;
        long hi = 10000000000000L;

        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            System.out.println("mid " + mid);
            if (f.apply(mid) <= target) hi = mid;  // >= creciente o <= decreciente
            else lo = mid + 1;
        }


        return lo;

    }

    public Long eval(String name) {
        var result = map.get(name).trim();
        //System.out.println(result);
        if (result.chars().allMatch(Character::isDigit)) return Long.parseLong(result);
        var tokens = result.split("\s");
        var op1 = tokens[0].trim();
        var op2 = tokens[2].trim();
        if (result.contains("+")) return eval(op1) + eval(op2);
        if (result.contains("-")) return eval(op1) - eval(op2);
        if (result.contains("*")) return eval(op1) * eval(op2);
        if (result.contains("/")) return eval(op1) / eval(op2);
        throw new RuntimeException();
    }


    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day21/input.txt";

        //var output = new Day21(input).solve1();

        //System.out.println(output);

        System.out.println(new Day21(input).solve1());
        System.out.println(new Day21(input).solve2());

    }
}
