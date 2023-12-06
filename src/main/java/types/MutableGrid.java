package types;


import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class MutableGrid<T> implements Grid<T> {

    private final Map<Pos, T> grid;

    public MutableGrid(Map<Pos, T> grid) {
        this.grid = grid;
    }


    @Override
    public Grid<T> remove(Pos... pos) {
        for (Pos p : pos) {
            grid.remove(p);
        }
        return this;
    }

    @Override
    public MutableGrid<T> copy() {
        return new MutableGrid<>(new HashMap<>(grid));
    }

    @Override
    public MutableGrid<T> put(Pos pos, T t) {
        grid.put(pos, t);
        return this;
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
    public MutableGrid<T> filter(BiPredicate<Pos, T> p) {
        grid.entrySet().removeIf(elem -> !p.test(elem.getKey(),
                                                 elem.getValue())
                                );
        return this;
    }

    @Override
    public <U> MutableGrid<U> mapValues(BiFunction<Pos, T, U> fn) {
        return new MutableGrid<>(grid.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(Map.Entry::getKey,
                                                               e -> fn.apply(e.getKey(), e.getValue())
                                                              )
                                             )
        );
    }

    @Override
    public Grid<T> mapValues(BiFunction<Pos, T, T> fn,
                             BiPredicate<Pos, T> p) {
        return new MutableGrid<>(grid.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(Map.Entry::getKey,
                                                               e -> p.test(e.getKey(), e.getValue()) ?
                                                                       fn.apply(e.getKey(), e.getValue()):
                                                                       e.getValue()
                                                              )
                                             )
        );
    }

    @Override
    public MutableGrid<T> mapPos(BiFunction<Pos, T, Pos> fn) {
        return new MutableGrid<>(grid.entrySet()
                                     .stream()
                                     .collect(Collectors.toMap(e -> fn.apply(e.getKey(),
                                                                             e.getValue()
                                                                            ),
                                                               e -> e.getValue()
                                                              )
                                             )
        );


    }



    @Override
    public MutableGrid<T> merge(Grid<T> other) {
        for (Cell<T> cell : other.getCells()) grid.put(cell.pos(), cell.value());
        return this;
    }

    @Override
    public Set<Cell<T>> getCellsSet() {
        return grid.entrySet().stream().map(it-> new Cell<>(it.getKey(),it.getValue())).collect(Collectors.toSet());
    }

    @Override
    public MutableGrid<T> fillWithRows(int top, int bottom, T value) {
        MutableGrid<T> xs =
                MutableGrid.fromGen(new Range(xmin(),
                                                 xmax()
                                       ),
                                       new Range(ymin() - bottom,
                                                 ymin()
                                       ),
                                       pos -> Optional.of(value)
                                      );
        MutableGrid<T> ys =
                MutableGrid.fromGen(new Range(xmin(),
                                                 xmax()
                                       ),
                                       new Range(ymax(),
                                                 ymax()+bottom
                                       ),
                                       pos -> Optional.of(value)
                                      );
        return xs.merge(ys).merge(this);
    }
    @Override
    public MutableGrid<T> fillWithColumns(int left, int right, T value) {
        MutableGrid<T> xs =
                MutableGrid.fromGen(new Range(xmin()-left,
                                                 xmin()
                                       ),
                                       new Range(ymin(),
                                                 ymax()
                                       ),
                                       pos -> Optional.of(value)
                                      );
        MutableGrid<T> ys =
                MutableGrid.fromGen(new Range(xmax(),
                                                 xmax()+right
                                       ),
                                       new Range(ymin(),
                                                 ymax()
                                       ),
                                       pos -> Optional.of(value)
                                      );
        return xs.merge(ys).merge(this);
    }



    @Override
    public T getVal(Pos pos) {
        return grid.get(pos);
    }


    @Override
    public List<Pos> getPositions(Comparator<Pos> comparator) {
        return grid.keySet()
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


    public static <T> MutableGrid<T> empty() {
        return new MutableGrid<>(new HashMap<>());
    }


    public static <T> MutableGrid<T> fromGen(Range xrange,
                                             Range yrange,
                                             Function<Pos, Optional<T>> gen
                                            ) {
        var acc = new HashMap<Pos, T>();
        for (var i = xrange.min(); i <= xrange.max(); i++) {
            for (var j = yrange.min(); j <= yrange.max(); j++) {
                Pos pos = new Pos(i, j);
                gen.apply(new Pos(i, j)).ifPresent(t -> acc.put(pos, t));
            }
        }
        return new MutableGrid<>(acc);
    }

    public static <T> MutableGrid<T> fromColumns(List<List<T>> columns) {
        return fromGen(new Range(0, columns.size() - 1),
                       new Range(0, columns.stream().map(it -> it.size() - 1)
                                           .max(Integer::compare)
                                           .orElse(0)),
                       pos -> {
                           List<T> column = columns.get(pos.x());
                           return column.size() > pos.y() ?
                                   Optional.of(column.get(pos.y())) :
                                   Optional.empty();
                       }
                      );
    }

    public static <T> MutableGrid<T> fromCells(List<? extends Cell<T>> entries) {
        var map = new HashMap<Pos, T>();
        for (Cell<T> entry : entries) map.put(entry.pos(), entry.value());
        return new MutableGrid<>(map);
    }

    public static <T> MutableGrid<T> fromRows(List<List<T>> rows) {
        return fromGen(new Range(0, rows.stream().map(it -> it.size() - 1)
                                        .max(Integer::compare)
                                        .orElse(0)),
                       new Range(0, rows.size() - 1),
                       pos -> {
                           List<T> row = rows.get(pos.y());
                           return row.size() > pos.x() ?
                                   Optional.of(row.get(pos.x())) :
                                   Optional.empty();
                       }
                      );
    }

    public static MutableGrid<String> fromString(String input,
                                                 String rowSeparator,
                                                 String columnSeparator,
                                                 Predicate<String> skipLine
                                                ) {
        List<String> a = Arrays.stream(input.split(rowSeparator)).toList();
        return MutableGrid.fromRows(a.stream()
                                     .filter(skipLine.negate())
                                     .map(it -> Arrays.stream(it.split(columnSeparator))
                                                      .toList()).toList());
    }

    public static MutableGrid<String> fromLines(List<String> lines,
                                                String columSeparator,
                                                Predicate<String> skipLine
                                               ) {
        return MutableGrid.fromRows(lines.stream()
                                         .filter(skipLine.negate())
                                         .map(line -> Arrays.stream(line.split(columSeparator))
                                                            .collect(Collectors.toList()))
                                         .collect(Collectors.toList()));
    }

    public static MutableGrid<String> fromLines(List<String> lines) {
        return MutableGrid.fromLines(lines, "", e -> false);
    }

    public static MutableGrid<String> fromFile(String path,
                                               String columnSeparator,
                                               Predicate<String> skipLine
                                              ) {
        return MutableGrid.fromRows(FileParsers.toListOfSplitLines(path,
                                                                   columnSeparator,
                                                                   skipLine
                                                                  ));

    }

    public static MutableGrid<String> fromString(String input) {

        return fromString(input,
                          "\\n",
                          "",
                          e -> false
                         );
    }

    public static MutableGrid<String> fromFile(String path) {
        return fromFile(path,
                        "",
                        e -> false
                       );

    }

    public static List<MutableGrid<String>> fromGroupsOfLines(List<List<String>> groups) {
        return groups.stream()
                     .map(MutableGrid::fromLines)
                     .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableGrid<?> other = (MutableGrid<?>) o;
        return this.grid.equals(other.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grid);
    }

}
