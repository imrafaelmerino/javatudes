package types;

import fun.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class ListFun {

    public static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if (size > requireNonNull(list).size()) return Stream.empty();
        return IntStream.range(0,
                               list.size() - size + 1
                              )
                        .mapToObj(start -> list.subList(start,
                                                        start + size
                                                       )
                                 );
    }


    public static <T> List<T> tail(List<T> list) {
        return list.subList(1,
                            list.size()
                           );
    }

    public static <T> List<T> init(List<T> list) {
        return list.subList(0,
                            list.size() -  1
                           );
    }


    public static <T> List<T> mutableOf(T a, T... others) {
        var list = new ArrayList<T>();
        list.add(requireNonNull(a));
        list.addAll(Arrays.asList(requireNonNull(others)));
        return list;
    }

    public static <T> List<T> append(List<T> list, T a, T... others) {
        list.add(requireNonNull(a));
        list.addAll(Arrays.asList(requireNonNull(others)));
        return list;
    }

    public static <T> List<T> prepend(List<T> list, T a, T... others) {
        list.addAll(0, Arrays.asList(requireNonNull(others)));
        list.add(0, requireNonNull(a));
        return list;
    }

    public static <T> List<T> appendAll(List<T> list, List<T> a, List<T>... others) {
        list.addAll(requireNonNull(a));
        for (List<T> other : others) list.addAll(other);
        return list;
    }

    public static <T> void removeFirst(List<T> list, Predicate<T> predicate) {
        var iter = list.iterator();
        while (iter.hasNext()) {
            if (predicate.test(iter.next())) {
                iter.remove();
                break;
            }
        }
    }

    public static <T> List<T> remove(List<T> list, T... obj) {
        for (T t : obj) list.remove(t);
        return list;
    }

    public static <T> T getBeforeLast(List<T> list){
        return list.get(list.size()-2);
    }



    public static <T> List<List<T>> allPairs(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        for (int j = 0; j < list.size() - 1; j++) {
            for (int i = j + 1; i < list.size(); i++) {
                List<T> e = new ArrayList<>();
                e.add(list.get(j));
                e.add(list.get(i));
                result.add(e);
            }
        }
        return result;
    }

    public static <T> List<List<T>> allTriples(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        for (int j = 0; j < list.size() - 2; j++) {
            for (int i = j + 1; i < list.size() - 1; i++) {
                for (int k = i + 1; k < list.size(); k++) {
                    List<T> e = new ArrayList<>();
                    e.add(list.get(j));
                    e.add(list.get(i));
                    e.add(list.get(k));
                    result.add(e);
                }
            }
        }
        return result;
    }

    public static List<Integer> toListOfInt(List<String> input) {
        return input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(allPairs(List.of("a")));
    }


    public static <T> List<Pair<T, T>> zip(List<T> lefts, List<T> rights) {
        var result = new ArrayList<Pair<T, T>>();
        int min = Math.min(lefts.size(), rights.size());
        for (int i = 0; i < min; i++) result.add(Pair.of(lefts.get(i), rights.get(i)));
        return result;
    }

    public static <T> List<T> prependAll(List<T> list, List<T> a, List<T>... others) {
        list.addAll(0, requireNonNull(a));
        for (List<T> other : others) list.addAll(0, requireNonNull(other));
        return list;
    }
}
