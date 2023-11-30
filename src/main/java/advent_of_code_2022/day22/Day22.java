package advent_of_code_2022.day22;

import fun.tuple.Pair;
import types.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day22 {

    static String R = "R";
    static String L = "L";
    static Map<String, Integer> WEIGHT = Map.of(">", 0, "v", 1, "<", 2, "^", 3);

    Grid<String> grid;
    List<Object> instructions = new ArrayList<>();
    Pos start;
    int s;
    Predicate<Pos> isA2;
    Predicate<Pos> isC3;
    Predicate<Pos> isD3;
    Predicate<Pos> isE4;
    Predicate<Pos> isE5;
    Predicate<Pos> isD5;
    Predicate<Pos> isC1;
    Predicate<Pos> isA1;
    Predicate<Pos> isB1;
    Predicate<Pos> isB6;
    Predicate<Pos> isG6;
    Predicate<Pos> isF4;
    Predicate<Pos> isF2;
    Predicate<Pos> isG2;

    Function<Pos, Pair<Pos, String>> WA2_1;
    Function<Pos, Pair<Pos, String>> WA1_2;

    Function<Pos, Pair<Pos, String>> WB1_6;
    Function<Pos, Pair<Pos, String>> WB6_1;

    Function<Pos, Pair<Pos, String>> WC1_3;
    Function<Pos, Pair<Pos, String>> WC3_1;

    Function<Pos, Pair<Pos, String>> WD5_3;
    Function<Pos, Pair<Pos, String>> WD3_5;

    Function<Pos, Pair<Pos, String>> WE4_5;
    Function<Pos, Pair<Pos, String>> WE5_4;

    Function<Pos, Pair<Pos, String>> WG2_6;
    Function<Pos, Pair<Pos, String>> WG6_2;

    Function<Pos, Pair<Pos, String>> WF2_4;
    Function<Pos, Pair<Pos, String>> WF4_2;


    public Day22(String input, int s) {
        this.s = s;


        isA2 = pos -> pos.y() == 0 && pos.x() >= 2 * s && pos.x() <= 3 * s - 1;
        isC3 = pos -> pos.y() == 0 && pos.x() >= s && pos.x() <=  2 * s - 1 ;
        isD3 = pos -> pos.x() == s && pos.y() >= 0 && pos.y() <= s-1;
        isE4 = pos -> pos.x() == s && pos.y() >= s && pos.y() <= 2*s-1;
        isE5 = pos -> pos.y() == 2 * s && pos.x() >= 0 && pos.x() <= s-1;
        isD5 = pos -> pos.x() ==0 && pos.y() >= 2*s && pos.y() <= 3*s-1;
        isC1 = pos -> pos.x() ==0 && pos.y() >= 3*s && pos.y() <= 4*s-1;
        isA1 = pos -> pos.y() ==4 * s-1 && pos.x() >= 0 && pos.x() <= s-1;
        isB1 = pos -> pos.x() ==s-1 && pos.y() >= 3*s && pos.y() <= 4*s-1;
        isB6 = pos -> pos.y() ==3 * s-1 && pos.x() >= s && pos.x() <= 2*s-1;
        isG6 = pos -> pos.x() ==2 * s-1 && pos.y() >= 2*s && pos.y() <= 3*s-1;
        isF4 = pos -> pos.x() ==2 * s-1 && pos.y() >= s && pos.y() <= 2*s-1;
        isF2 = pos -> pos.y() ==s-1 && pos.x() >= 2*s && pos.x() <= 3*s-1;
        isG2 = pos -> pos.x() ==3 * s-1 && pos.y() >= 0 && pos.y() <= s-1;

        WA2_1 = pos -> Pair.of(new Pos(s-1-(3*s-1-pos.x()), 4 * s - 1), "^");
        WA1_2 = pos -> Pair.of(new Pos(2*s+pos.x(), 0), "v");

        WE5_4 = pos -> Pair.of(new Pos(s, s+ pos.x()), ">");
        WE4_5 = pos -> Pair.of(new Pos(pos.y()-s,2*s), "v");

        WC3_1 = pos ->Pair.of(new Pos(0, 2*s+ pos.x()), ">");
        WC1_3 = pos ->Pair.of(new Pos(pos.y()-2*s,0), "v");

        WB1_6 = pos ->Pair.of(new Pos(-2*s + pos.y(), 3*s-1), "^");
        WB6_1 = pos ->Pair.of(new Pos(s-1, 2*s+pos.x()), "<");

        WD5_3 = pos ->Pair.of(new Pos(s, 3*s - pos.y()-1), ">");
        WD3_5 = pos ->Pair.of(new Pos(0, 3*s - pos.y()-1), ">");

        WG6_2 =  pos ->Pair.of(new Pos(3*s-1, 3*s - pos.y()-1), "<");
        WG2_6 = pos ->Pair.of(new Pos(2*s-1, 3*s - pos.y()-1), "<");

        WF2_4 = pos -> Pair.of(new Pos(2*s-1,s+ pos.x()-2*s), "<");
        WF4_2 = pos -> Pair.of(new Pos(2*s + pos.y()-s, s-1), "^");

        var lines = FileParsers.toListOfLines(input);

        var last = String.join("", lines.remove(lines.size() - 1));

        grid = MutableGrid.fromLines(lines)
                             .remove((pos,val) -> val.equals(" "));

        while (!last.isEmpty()) {
            var head = last.substring(0, 1);
            if (head.equals(L) || head.equals(R)) {
                instructions.add(head);
                last = last.substring(1);
            } else {
                var pair = StringParsers.readInt(last);
                instructions.add(pair.first());
                last = pair.second();
            }

        }


        List<Cell<String>> bottomBorder = grid.getBottomBorder();
        List<Cell<String>> collect = bottomBorder
                .stream()
                .filter(
                        cell -> cell.value().equals("."))
                .sorted(comparator)
                .collect(Collectors.toList());
        start = collect
                    .get(0)
                    .pos();

    }

    private static Comparator<? super Cell<String>> comparator = Comparator.comparingInt(it -> it.pos().x());

    public String rotate(String dir, Object rot) {
        return switch (dir) {
            case "<" -> rot.equals(R) ? "^" : "v";
            case ">" -> rot.equals(R) ? "v" : "^";
            case "^" -> rot.equals(R) ? ">" : "<";
            default -> rot.equals(R) ? "<" : ">";
        };
    }

    public Pos moveN(String dir, Pos source, int n) {
        if (n == 0) return source;
        var next = move(dir, source);
        if (next.equals(source)) return source;
        return moveN(dir, next, n - 1);
    }

    public  Pair<Pos,String> move2(String dir, Pos source) {
        return switch (dir) {
            case "<" -> {
                var target = source.left();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield  Pair.of(target,dir);
                if (val != null && val.equals("#")) yield Pair.of(source,dir);
                Pos wrapped;
                String newDIR;
                if(isD3.test(source)) {
                    wrapped = WD3_5.apply(source).first();
                    newDIR = WD3_5.apply(source).second();
                }
                else if(isE4.test(source)){
                    wrapped = WE4_5.apply(source).first();
                    newDIR = WE4_5.apply(source).second();
                }
                else if(isD5.test(source)){
                    wrapped = WD5_3.apply(source).first();
                    newDIR = WD5_3.apply(source).second();
                }
                else {
                    wrapped = WC1_3.apply(source).first();
                    newDIR = WC1_3.apply(source).second();
                }
                if (grid.getVal(wrapped).equals(".")) yield Pair.of(wrapped,newDIR);
                else yield Pair.of(source,dir);
            }
            case ">" -> {
                var target = source.right();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield  Pair.of(target,dir);
                if (val != null && val.equals("#")) yield Pair.of(source,dir);
                Pos wrapped;
                String newDIR;
                if(isB1.test(source)) {
                    wrapped = WB1_6.apply(source).first();
                    newDIR = WB1_6.apply(source).second();
                }
                else if(isG6.test(source)){
                    wrapped = WG6_2.apply(source).first();
                    newDIR = WG6_2.apply(source).second();
                }
                else if(isF4.test(source)){
                    wrapped = WF4_2.apply(source).first();
                    newDIR = WF4_2.apply(source).second();
                }
                else {
                    wrapped = WG2_6.apply(source).first();
                    newDIR = WG2_6.apply(source).second();
                }
                if (grid.getVal(wrapped).equals("."))yield Pair.of(wrapped,newDIR);
                else yield Pair.of(source,dir);
            }
            case "^" -> {
                var target = source.down();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield Pair.of(target,dir);
                if (val != null && val.equals("#")) yield Pair.of(source,dir);
                Pos wrapped;
                String newDIR;
                if(isE5.test(source)) {
                    wrapped = WE5_4.apply(source).first();
                    newDIR = WE5_4.apply(source).second();
                }
                else if(isC3.test(source)){
                    wrapped = WC3_1.apply(source).first();
                    newDIR = WC3_1.apply(source).second();
                }
                else {
                    wrapped = WA2_1.apply(source).first();
                    newDIR = WA2_1.apply(source).second();
                }
                if (grid.getVal(wrapped).equals("."))yield Pair.of(wrapped,newDIR);
                else yield Pair.of(source,dir);
            }
            default -> {
                var target = source.up();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield Pair.of(target,dir);
                if (val != null && val.equals("#")) yield Pair.of(source,dir);
                Pos wrapped;
                String newDIR;
                if(isF2.test(source)) {
                    wrapped = WF2_4.apply(source).first();
                    newDIR = WF2_4.apply(source).second();
                }
                else if(isB6.test(source)){
                    wrapped = WB6_1.apply(source).first();
                    newDIR = WB6_1.apply(source).second();
                }
                else {
                    wrapped = WA1_2.apply(source).first();
                    newDIR = WA1_2.apply(source).second();
                }
                if (grid.getVal(wrapped).equals("."))yield Pair.of(wrapped,newDIR);
                else yield Pair.of(source,dir);
            }
        };
    }

    public Pair<Pos,String> move2N(String dir, Pos source, int n) {
        if (n == 0) return Pair.of(source,dir);
        var next = move2(dir, source);
        if (next.first().equals(source)) return Pair.of(source,dir);
        return move2N(next.second(), next.first(), n - 1);
    }

    public  Pos move(String dir, Pos source) {
        return switch (dir) {
            case "<" -> {
                var target = source.left();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield  target;
                if (val != null && val.equals("#")) yield source;
                var wrapped = new Pos(grid.xmax(source.y()), source.y());
                if (grid.getVal(wrapped).equals(".")) yield wrapped;
                else yield source;
            }
            case ">" -> {
                var target = source.right();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield target;
                if (val != null && val.equals("#")) yield source;
                var wrapped = new Pos(grid.xmin(source.y()), source.y());
                if (grid.getVal(wrapped).equals(".")) yield wrapped;
                else yield source;
            }
            case "^" -> {
                var target = source.down();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield target;
                if (val != null && val.equals("#")) yield source;
                var wrapped = new Pos(source.x(), grid.ymax(source.x()));
                if (grid.getVal(wrapped).equals(".")) yield wrapped;
                else yield source;
            }
            default -> {
                var target = source.up();
                String val = grid.getVal(target);
                if (val != null && val.equals(".")) yield target;
                if (val != null && val.equals("#")) yield source;
                var wrapped = new Pos(source.x(), grid.ymin(source.x()));
                if (grid.getVal(wrapped).equals(".")) yield wrapped;
                else yield source;
            }
        };
    }

    public Object solve() {

        grid.put(start, ">");

        var face = ">";
        var pos = start;

        while (!instructions.isEmpty()) {

            var ins = instructions.remove(0);
            if (ins.equals(L) || ins.equals(R)) face = rotate(face, ins);
            else pos = moveN(face, pos, ((int) ins));

        }

        return 1000 * (pos.y() + 1) + 4 * (pos.x() + 1) + WEIGHT.get(face);

    }

    public Object solve2() {
        var face = ">";
        var pos = start;

        while (!instructions.isEmpty()) {

            var ins = instructions.remove(0);
            if (ins.equals(L) || ins.equals(R)) face = rotate(face, ins);
            else {
                Pair<Pos, String> t = move2N(face, pos, ((int) ins));
                pos = t.first();
                face = t.second();
            }

        }

        return 1000 * (pos.y() + 1) + 4 * (pos.x() + 1) + WEIGHT.get(face);

    }

    public static void main(String[] args) {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day22/input.txt";

        System.out.println(new Day22(input, 50).solve());
        System.out.println(new Day22(input, 50).solve2());


    }
}
