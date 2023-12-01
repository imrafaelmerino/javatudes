package advent_of_code_2022.day11;

import types.FileParsers;

import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;


/**
 * modulus arithmetic for part 2
 * <p>
 * not interested in real value of worry, just need to keep test divisible operations invariants
 * <p>
 * if x is divisible by y, then k*x is also divisible by y. Demonstration
 * <p>
 * x % y = 0 ->  kx % y = ( (k % y) (x % y)) % y = 0 since  (x % y) is zero
 * <p>
 * since we have to keep the invariants x % y = 0, x % y1 = 0, x % y2 = 0 ...
 * <p>
 * we can apply the op (x % y1*y2*y3...) to reduce the worry and this op doesn't alter the result, since
 * <p>
 * x % y1 or x % y2 or x % y3 has the same result
 */

public class Day11 {

    record Next(int ifTrue, int ifFalse) {}


    record Monkey(List<Long> items, ToLongFunction<Long> op, int div, Next next) {}

    // refactor for part 2, part 1 just a function n / 3
    static Function<Integer, LongFunction<Long>> reliefFun2 = p -> n -> n % p;
    static Function<Integer, LongFunction<Long>> reliefFun1 = p -> n -> n / 3;

    public static void main(String[] args) {

        Map<Integer, Integer> inspections = new HashMap<>();


        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day11/input_test_2.txt";
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day11/input.txt";


        var monkeys = FileParsers.toGroupsOfLines(input)
                                 .stream()
                                 .map(Day11::ptm)
                                 .collect(Collectors.toList());

        var product = monkeys.stream()
                             .map(it -> it.div)
                             .reduce(1, (a, b) -> a * b);

        var relief = reliefFun2.apply(product);

        int rounds = 10000;

        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                var m = monkeys.get(j);
                var iter = m.items.iterator();
                while (iter.hasNext()) {
                    inspections.compute(j, (a, n) -> n == null ? 1 : n + 1);
                    var worry = iter.next();

                    var newWorry = relief.apply(m.op.applyAsLong(worry));

                    var newMonkey = (newWorry % m.div == 0) ?
                            m.next.ifTrue :
                            m.next.ifFalse;
                    monkeys.get(newMonkey).items.add(newWorry);
                    iter.remove();
                }

            }
        }

        var sorted = inspections.entrySet()
                                .stream()
                                .sorted(Comparator.<Map.Entry<Integer, Integer>>comparingInt(Map.Entry::getValue).reversed())
                                .toList();

        //part2 needs biginteger
        BigInteger x = BigInteger.valueOf(sorted.get(0).getValue())
                                 .multiply(BigInteger.valueOf(sorted.get(1).getValue()));
        System.out.println(x);


    }

    private static Monkey ptm(List<String> ml) {
        var items = Arrays.stream(ml.get(1).replaceAll("[^0-9]", " ")
                                    .split("\s")
                                 )
                          .filter(it -> !it.isEmpty())
                          .map(Long::parseLong)
                          .collect(Collectors.toList());

        var xs = Arrays.stream(ml.get(2).split("\s")).toList();
        var ops = xs.subList(xs.size() - 2, xs.size());
        String var2 = ops.get(1);
        String op = ops.get(0);
        ToLongFunction<Long> f;
        if (op.equals("*")) f = a -> a * (var2.equals("old") ? a : Long.parseLong(var2));
        else f = a -> a + (var2.equals("old") ? a : Long.parseLong(var2));


        int ifTrue = Integer.parseInt(ml.get(4)
                                        .replaceAll("[^0-9]", "")
                                     );
        int ifFalse = Integer.parseInt(ml.get(5)
                                         .replaceAll("[^0-9]", "")
                                      );

        return new Monkey(items, f, Integer.parseInt(ml.get(3)
                                                       .replaceAll("[^0-9]", "")),
                          new Next(ifTrue, ifFalse)
        );


    }


}
