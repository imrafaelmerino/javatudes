package types;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class PersistentGrid<T> implements Grid<T> {


    private final Map<Pos, T> grid;

    public PersistentGrid(Map<Pos, T> grid) {
        this.grid = grid;
    }


    @Override
    public Grid<T> remove(Pos... pos) {
        Map<Pos, T>  result = grid;
        for (var p : pos) result = result.remove(p);
        return new PersistentGrid<>(result);
    }

    @Override
    public PersistentGrid<T> copy() {
        return this;
    }

    @Override
    public PersistentGrid<T> put(Pos pos, T t) {
        return new PersistentGrid<>(grid.put(pos, t));
    }

    @Override
    public boolean containsValue(T value) {
        return grid.containsValue(value);
    }

    @Override
    public boolean containsPos(Pos pos) {
        return grid.containsKey(pos);
    }

    @Override
    public PersistentGrid<T> filter(BiPredicate<Pos, T> p) {

        return new PersistentGrid<>(grid.filter(p));
    }

    @Override
    public <U> PersistentGrid<U> mapValues(BiFunction<Pos, T, U> fn) {

        return new PersistentGrid<>(grid.map((p, v) -> new Tuple2<>(p, fn.apply(p, v))));
    }

    @Override
    public Grid<T> mapValues(BiFunction<Pos, T, T> fn,
                             BiPredicate<Pos, T> p) {
        return new PersistentGrid<>(grid.map((pos, v) -> new Tuple2<>(pos, p.test(pos,v) ? fn.apply(pos, v):v)));

    }


    @Override
    public Grid<T> mapPos(BiFunction<Pos, T, Pos> fn) {
        return PersistentGrid.fromCells(getCells().stream()
                                                  .map(cell -> new Cell<>(fn.apply(cell.pos(), cell.value()), cell.value()))
                                                  .collect(Collectors.toList())
                                       );
    }



    @Override
    public PersistentGrid<T> merge(Grid<T> other) {
        PersistentGrid<T> result = this;
        for (Cell<T> cell : other.getCells()) result = result.put(cell.pos(), cell.value());
        return result;
    }

    @Override
    public Set<Cell<T>> getCellsSet() {
        return grid.toSet()
                   .map(it->new Cell<>(it._1,it._2)).toJavaSet();
    }

    @Override
    public PersistentGrid<T> fillWithRows(int top, int bottom, T value) {
        PersistentGrid<T> xs = bottom > 0 ?
                PersistentGrid.fromGen(new Range(xmin(),
                                                 xmax()
                                       ),
                                       new Range(ymin() - bottom,
                                                 ymin()
                                       ),
                                       pos -> Optional.of(value)
                                      ):this;
        PersistentGrid<T> ys = top > 0 ?
                PersistentGrid.fromGen(new Range(xmin(),
                                                 xmax()
                                       ),
                                       new Range(ymax(),
                                                 ymax()+top
                                       ),
                                       pos -> Optional.of(value)
                                      ) : this;
        return xs.merge(ys).merge(this);
    }
    @Override
    public PersistentGrid<T> fillWithColumns(int left, int right, T value) {
        PersistentGrid<T> xs = left > 0 ?
                PersistentGrid.fromGen(new Range(xmin()-left,
                                                 xmin()
                                       ),
                                       new Range(ymin(),
                                                 ymax()
                                       ),
                                       pos -> Optional.of(value)
                                      ): this;
        PersistentGrid<T> ys = right > 0 ?
                PersistentGrid.fromGen(new Range(xmax(),
                                                 xmax()+right
                                       ),
                                       new Range(ymin(),
                                                 ymax()
                                       ),
                                       pos -> Optional.of(value)
                                      ):this;
        return xs.merge(ys).merge(this);
    }


    @Override
    public T getVal(Pos pos) {
        return grid.get(pos).getOrNull();
    }

    @Override
    public List<Pos> getPositions(Comparator<Pos> comparator) {
        return grid.keySet()
                   .toJavaList()
                   .stream()
                   .sorted(comparator)
                   .toList();
    }



    @Override
    public int size() {
        return grid.size();
    }

    @Override
    public boolean isEmpty() {
        return grid.isEmpty();
    }

    public static <T> PersistentGrid<T> empty() {
        return new PersistentGrid<>(HashMap.empty());
    }

    public static <T> PersistentGrid<T> fromGen(Range xrange,
                                                Range yrange,
                                                Function<Pos, Optional<T>> gen
                                               ) {
        var acc = HashMap.<Pos, T>empty();
        for (var i = xrange.min(); i <= xrange.max(); i++) {
            for (var j = yrange.min(); j <= yrange.max(); j++) {
                var pos = new Pos(i, j);
                Optional<T> opt = gen.apply(new Pos(i, j));
                if (opt.isPresent()) acc = acc.put(pos, opt.get());
            }
        }
        return new PersistentGrid<>(acc);
    }

    public static <T> PersistentGrid<T> fromColumns(List<List<T>> columns) {
        return fromGen(
                new Range(0, columns.size() - 1),
                new Range(0, columns.stream().map(it -> it.size() - 1).max(Integer::compare).orElse(0)),
                pos -> {
                    List<T> column = columns.get(pos.x());
                    return column.size() > pos.y() ?
                            Optional.of(column.get(pos.y())) :
                            Optional.empty();
                }
                      );
    }

    public static <T> PersistentGrid<T> fromCells(List<? extends Cell<T>> entries) {
        var map = HashMap.<Pos, T>empty();
        for (Cell<T> entry : entries) map = map.put(entry.pos(), entry.value());
        return new PersistentGrid<>(map);
    }

    public static <T> PersistentGrid<T> fromRows(List<List<T>> rows) {
        return fromGen(
                new Range(0, rows.stream().map(it -> it.size() - 1).max(Integer::compare).orElse(0)),
                new Range(0, rows.size() - 1),
                pos -> {
                    List<T> row = rows.get(pos.y());
                    return row.size() > pos.x() ?
                            Optional.of(row.get(pos.x())) :
                            Optional.empty();
                }
                      );
    }

    public static PersistentGrid<String> fromString(String input,
                                                    String rowSeparator,
                                                    String columnSeparator,
                                                    Predicate<String> skipLine
                                                   ) {
        List<String> a = Arrays.stream(input.split(rowSeparator)).toList();
        return PersistentGrid.fromRows(a.stream()
                                        .filter(skipLine.negate())
                                        .map(it -> Arrays.stream(it.split(columnSeparator))
                                                         .toList()).toList()
                                      );
    }

    public static PersistentGrid<String> fromString(String input) {
        return fromString(input, "\\n", "", e -> false);
    }

    public static PersistentGrid<String> fromFile(String path,
                                                  String columnSeparator,
                                                  Predicate<String> skipLine
                                                 ) {
        return PersistentGrid.fromRows(FileParsers.toListOfSplitLines(path,
                                                                      columnSeparator,
                                                                      skipLine
                                                                     ));

    }

    public static PersistentGrid<String> fromFile(String path) {
        return fromFile(path, "", e -> false);

    }

    public static PersistentGrid<String> fromLines(List<String> lines,
                                                   String columSeparator,
                                                   Predicate<String> skipLine
                                                  ) {
        return PersistentGrid.fromRows(lines.stream()
                                            .filter(skipLine.negate())
                                            .map(line -> Arrays.stream(line.split(columSeparator))
                                                               .collect(Collectors.toList()))
                                            .collect(Collectors.toList()));
    }

    public static PersistentGrid<String> fromLines(List<String> lines) {
        return PersistentGrid.fromLines(lines, "", e -> false);
    }

    public static List<PersistentGrid<String>> fromGroupsOfLines(List<List<String>> groups) {
        return groups.stream()
                     .map(PersistentGrid::fromLines)
                     .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersistentGrid<?> other = (PersistentGrid<?>) o;
        return this.grid.equals(other.grid);
    }

    int hc = -1;
    //Todo
    @Override
    public int hashCode() {
        if(hc==-1) hc = grid.toJavaMap().hashCode();
        return hc;
    }



}
