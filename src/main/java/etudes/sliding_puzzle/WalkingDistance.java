package etudes.sliding_puzzle;

import org.paukov.combinatorics3.Generator;
import search.AStarSearch;
import search.Action;
import types.Grid;
import types.ListFun;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://computerpuzzle.net/english/15puzzle/wd.gif
 */
public class WalkingDistance {

    static class Bag {

        boolean containsSpace() {
            return items.contains("X");
        }

        Bag add(String s) {
            var bag = new Bag(new ArrayList<>(items));
            bag.items.add(s);
            return bag;
        }

        Bag removeSpace() {
            return remove("X");
        }

        Bag addSpace() {
            return add("X");
        }

        Bag remove(String s) {
            List<String> copy = new ArrayList<>(items);
            ListFun.removeFirst(copy, i -> i.equals(s));
            return new Bag(copy);
        }

        final List<String> items;

        public Bag(List<String> items) {
            this.items = items;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj instanceof Bag bag)
                return bag.items.size() == items.size() &&
                        items.containsAll(bag.items) && bag.items.containsAll(items);
            return false;
        }

        private static final Map<String, Integer> HASHCODE_MAP = Map.of("A", 1, "B", 2, "C", 3, "D", 4, "X", 5);

        @Override
        public int hashCode() {
            return items.stream()
                        .map(HASHCODE_MAP::get)
                        .reduce(0, Integer::sum);
        }
    }

    record MultiBag(Bag _1, Bag _2, Bag _3, Bag _4) {

        static Map<String, String> ROW_DIGITS = new HashMap<>();

        static {
            ROW_DIGITS.put("1", "A");
            ROW_DIGITS.put("2", "A");
            ROW_DIGITS.put("3", "A");
            ROW_DIGITS.put("4", "A");
            ROW_DIGITS.put("5", "B");
            ROW_DIGITS.put("6", "B");
            ROW_DIGITS.put("7", "B");
            ROW_DIGITS.put("8", "B");
            ROW_DIGITS.put("9", "C");
            ROW_DIGITS.put("10", "C");
            ROW_DIGITS.put("11", "C");
            ROW_DIGITS.put("12", "C");
            ROW_DIGITS.put("13", "D");
            ROW_DIGITS.put("14", "D");
            ROW_DIGITS.put("15", "D");
            ROW_DIGITS.put("X", "X");
        }

        static Map<String, String> COLUMN_DIGITS = new HashMap<>();

        static {
            COLUMN_DIGITS.put("1", "A");
            COLUMN_DIGITS.put("5", "A");
            COLUMN_DIGITS.put("9", "A");
            COLUMN_DIGITS.put("13", "A");
            COLUMN_DIGITS.put("2", "B");
            COLUMN_DIGITS.put("6", "B");
            COLUMN_DIGITS.put("10", "B");
            COLUMN_DIGITS.put("14", "B");
            COLUMN_DIGITS.put("3", "C");
            COLUMN_DIGITS.put("7", "C");
            COLUMN_DIGITS.put("11", "C");
            COLUMN_DIGITS.put("15", "C");
            COLUMN_DIGITS.put("4", "D");
            COLUMN_DIGITS.put("8", "D");
            COLUMN_DIGITS.put("12", "D");
            COLUMN_DIGITS.put("X", "X");
        }

        public static MultiBag fromString(String s) {
            var seqs = Arrays.stream(s.split(";"))
                             .map(it -> Arrays.stream(it.split(","))
                                              .collect(Collectors.toList())
                                 )
                             .collect(Collectors.toList());
            return new MultiBag(new Bag(seqs.get(0)),
                                new Bag(seqs.get(1)),
                                new Bag(seqs.get(2)),
                                new Bag(seqs.get(3))
            );
        }

        public static MultiBag fromColumns(Grid<String> board) {
            var columns = board.getColumns();
            var bags = columns.stream().map(it -> new Bag(it.stream()
                                                            .map(e -> COLUMN_DIGITS.get(e.value()))
                                                            .collect(Collectors.toList()))
                                           )
                              .collect(Collectors.toList());
            return new MultiBag(bags.get(0), bags.get(1), bags.get(2), bags.get(3));
        }

        public static MultiBag fromRows(Grid<String> board) {
            var rows = board.getRows();
            var bags = rows.stream().map(it -> new Bag(it.stream()
                                                         .map(e -> ROW_DIGITS.get(e.value()))
                                                         .collect(Collectors.toList()))
                                        )
                           .collect(Collectors.toList());
            return new MultiBag(bags.get(0), bags.get(1), bags.get(2), bags.get(3));
        }

        public MultiBag mv_from_1_to_2(String a) {
            return new MultiBag(_1.remove(a).addSpace(),
                                _2.add(a).removeSpace(),
                                _3,
                                _4
            );
        }

        public MultiBag mv_from_2_to_1(String a) {
            return new MultiBag(_1.add(a).removeSpace(),
                                _2.remove(a).addSpace(),
                                _3,
                                _4
            );
        }

        public MultiBag mv_from_2_to_3(String a) {
            return new MultiBag(_1,
                                _2.remove(a).addSpace(),
                                _3.add(a).removeSpace(),
                                _4
            );
        }

        public MultiBag mv_from_3_to_2(String a) {
            return new MultiBag(_1,
                                _2.add(a).removeSpace(),
                                _3.remove(a).addSpace(),
                                _4
            );
        }

        public MultiBag mv_from_3_to_4(String a) {
            return new MultiBag(_1,
                                _2,
                                _3.remove(a).addSpace(),
                                _4.add(a).removeSpace()
            );
        }

        public MultiBag mv_from_4_to_3(String a) {
            return new MultiBag(_1,
                                _2,
                                _3.add(a).removeSpace(),
                                _4.remove(a).addSpace()
            );
        }

        @Override
        public String toString() {
            return String.format("%s;%s;%s;%s",
                                 String.join(",", _1.items),
                                 String.join(",", _2.items),
                                 String.join(",", _3.items),
                                 String.join(",", _4.items)
                                );

        }
    }

    static Function<MultiBag, List<Action<MultiBag>>> succesors = bags -> {
        if (bags._1.containsSpace())
            return bags._2.items.stream()
                                .distinct()
                                .map(e -> new Action<>("2 -> 1", bags.mv_from_2_to_1(e)))
                                .toList();

        if (bags._2.containsSpace())
            return Stream.of(bags._1.items.stream()
                                          .distinct()
                                          .map(e -> new Action<>("1 -> 2", bags.mv_from_1_to_2(e))),
                             bags._3.items.stream()
                                          .distinct()
                                          .map(e -> new Action<>("3 -> 2", bags.mv_from_3_to_2(e)))
                            )
                         .flatMap(it -> it)
                         .toList();

        if (bags._3.containsSpace())
            return Stream.of(bags._2.items.stream()
                                          .distinct()
                                          .map(e -> new Action<>("2 -> 3", bags.mv_from_2_to_3(e))),
                             bags._4.items.stream()
                                          .distinct()
                                          .map(e -> new Action<>("4 -> 3", bags.mv_from_4_to_3(e)))
                            )
                         .flatMap(it -> it)
                         .toList();


        return bags._3.items.stream()
                            .distinct()
                            .map(e -> new Action<>("3 -> 4", bags.mv_from_3_to_4(e)))
                            .toList();

    };


    static MultiBag GOAL = new MultiBag(new Bag(List.of("A", "A", "A", "A")),
                                        new Bag(List.of("B", "B", "B", "B")),
                                        new Bag(List.of("C", "C", "C", "C")),
                                        new Bag(List.of("D", "D", "D", "X"))
    );


    public static Function<MultiBag, Integer> h = mb ->
            mb._1.items.stream()
                       .map(l -> {
                           if (l.equals("A")) return 0;
                           if (l.equals("B")) return 1;
                           if (l.equals("C")) return 2;
                           return 3;
                       })
                       .reduce(0, Integer::sum) +
                    mb._2.items.stream()
                               .map(l -> {
                                   if (l.equals("A")) return 1;
                                   if (l.equals("B")) return 0;
                                   if (l.equals("C")) return 1;
                                   return 2;
                               })
                               .reduce(0, Integer::sum) +
                    mb._3.items.stream()
                               .map(l -> {
                                   if (l.equals("A")) return 2;
                                   if (l.equals("B")) return 1;
                                   if (l.equals("C")) return 0;
                                   return 1;
                               })
                               .reduce(0, Integer::sum) +
                    mb._4.items.stream()
                               .map(l -> {
                                   if (l.equals("A")) return 3;
                                   if (l.equals("B")) return 2;
                                   if (l.equals("C")) return 1;
                                   return 0;
                               })
                               .reduce(0, Integer::sum);


    public static Function<MultiBag, Integer> distanceFromMB =
            mb ->
            {
                AStarSearch<MultiBag> bb = AStarSearch.fromActions(succesors,
                                                                   (a, b) -> 1,
                                                                   h
                                                                  );
                return bb.findFirst(mb, s -> s.equals(GOAL)).path().size() - 1;
            };

    public static Function<Grid<String>, Integer> distanceBB =
            grid ->
                    distanceFromMB.apply(MultiBag.fromRows(grid)) +
                            distanceFromMB.apply(MultiBag.fromColumns(grid));

    @SafeVarargs
    public static List<String> diff(List<String> xs, List<String>... ys) {
        var xsc = new ArrayList<>(xs);
        for (var zs : ys) for (String y : zs) ListFun.removeFirst(xsc, y::equals);
        return xsc;

    }

    public static void main(String[] args) throws IOException {

        //System.out.println(distanceBB.apply(new NPuzzle("1 2 3 X|5 6 7 8|9 10 11 12|13 4 14 15").board));
        //System.out.println(distanceFromMB.apply(MultiBag.fromString("C,D,D,X;C,C,C,D;B,B,B,B;A,A,A,A")));
        var path = Paths.get("/Users/rmerino/Projects/javatudes/src/main/java/etudes/sliding_puzzle/database.txt");
        createDatabase(path);

    }

    public static void createDatabase(Path path) throws IOException {
        var options = Stream.of(
                                    Stream.of("A", "A", "A", "A"),
                                    Stream.of("B", "B", "B", "B"),
                                    Stream.of("C", "C", "C", "C"),
                                    Stream.of("D", "D", "D", "X")
                               )
                            .flatMap(Function.identity())
                            .toList();


        for (var xs : Generator.combination(options).simple(4).stream()
                               .map(Bag::new)
                               .collect(Collectors.toSet()))
            for (var ys : Generator.combination(diff(options, xs.items)).simple(4).stream()
                                   .map(Bag::new)
                                   .collect(Collectors.toSet()))
                for (var zs : Generator.combination(diff(options, xs.items, ys.items)).simple(4).stream()
                                       .map(Bag::new)
                                       .collect(Collectors.toSet())) {
                    var rest = diff(options, xs.items, ys.items, zs.items);
                    MultiBag bag = new MultiBag(xs, ys, zs, new Bag(rest));
                    Files.writeString(path, bag + "\n",
                                      StandardOpenOption.CREATE,
                                      StandardOpenOption.APPEND
                                     );
                    Files.writeString(path, distanceFromMB.apply(bag) + "\n",
                                      StandardOpenOption.CREATE,
                                      StandardOpenOption.APPEND
                                     );
                }
    }


}
