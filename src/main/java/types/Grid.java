package types;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public sealed interface Grid<T> extends Iterable<Cell<T>> permits MutableGrid, PersistentGrid {


    Grid<T> remove(Pos... pos);

    default Grid<T> remove(BiPredicate<Pos, T> p) {
        return filter(p.negate());
    }

    Grid<T> copy();

    Grid<T> put(Pos pos, T t);

    Grid<T> transpose();

    default Grid<T> put(BiPredicate<Pos, T> p, BiFunction<Pos, T, T> fn) {
        Grid<T> result = this;
        var iter = getCells().stream()
                             .filter(c -> p.test(c.pos(), c.value()))
                             .iterator();
        while (iter.hasNext()) {
            var next = iter.next();
            result = result.put(next.pos(), fn.apply(next.pos(), next.value()));
        }

        return result;


    }

    boolean containsValue(T value);

    boolean containsPos(Pos pos);

    Grid<T> filter(BiPredicate<Pos, T> p);

    <U> Grid<U> mapValues(BiFunction<Pos, T, U> fn);

    Grid<T> mapValues(BiFunction<Pos, T, T> fn,
                      BiPredicate<Pos, T> p
                     );

    Grid<T> mapPos(BiFunction<Pos, T, Pos> fn);


    Grid<T> merge(Grid<T> other);

    default List<Cell<T>> getCells(Comparator<Pos> comparator) {
        return getPositions(comparator)
                .stream()
                .map(pos -> new Cell<>(pos, getVal(pos)))
                .collect(Collectors.toList());
    }

    default Cell<T> findOne(BiPredicate<Pos, T> p) {
        return getCells().stream().filter(c -> p.test(c.pos(), c.value())).findFirst().orElse(null);
    }

    default List<Cell<T>> find(BiPredicate<Pos, T> p) {
        return getCells().stream().filter(c -> p.test(c.pos(), c.value())).collect(Collectors.toList());
    }

    default List<Cell<T>> getCells() {
        return getPositions()
                .stream()
                .map(pos -> new Cell<>(pos, getVal(pos)))
                .collect(Collectors.toList());
    }


    Set<Cell<T>> getCellsSet();


    Grid<T> fillWithRows(int top, int bottom, T value);

    Grid<T> fillWithColumns(int left, int right, T value);

    T getVal(Pos pos);

    default Optional<T> getOptVal(Pos pos) {
        return Optional.ofNullable(getVal(pos));
    }

    default List<Cell<T>> getRightBorder() {
        int xmax = xmax();
        List<List<Cell<T>>> columns = getColumns((pos, val) -> pos.x() == xmax);
        return columns.get(columns.size() - 1);
    }

    default List<Cell<T>> getLeftBorder() {
        int xmin = xmin();
        return getColumns((pos, val) -> pos.x() == xmin).get(0);

    }

    default List<Cell<T>> getTopBorder() {
        int ymax = ymax();
        List<List<Cell<T>>> rows = getRows((pos, val) -> pos.y() == ymax);
        return rows.get(rows.size() - 1);
    }

    default List<Cell<T>> getBottomBorder() {
        int ymin = ymin();
        return getRows((pos, val) -> pos.y() == ymin).get(0);
    }

    default List<Pos> getNeighbors(Pos pos) {
        return pos.getNeighbors()
                  .stream()
                  .filter(this::containsPos)
                  .collect(Collectors.toList());
    }

    default List<Pos> getVHNeighbors(Pos pos) {
        return getNeighbors(pos)
                .stream()
                .filter(p -> p.x() == pos.x() || p.y() == pos.y())
                .collect(Collectors.toList());
    }


    default List<Integer> ys() {

        return
                getPositions()
                        .stream()
                        .map(Pos::y)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
    }

    default int ymax() {
        List<Integer> ys = ys();
        return ys.get(ys.size() - 1);
    }

    default int ymin() {
        return ys().get(0);
    }

    default int ymin(int x) {
        return getPositions()
                .stream()
                .filter(it -> it.x() == x)
                .map(Pos::y)
                .sorted()
                .collect(Collectors.toList())
                .get(0);
    }

    default int ymax(int x) {
        List<Integer> ys = getPositions()
                .stream()
                .filter(it -> it.x() == x)
                .map(Pos::y)
                .sorted()
                .toList();
        return ys.get(ys.size() - 1);
    }

    default List<Integer> xs() {
        return
                getPositions()
                        .stream()
                        .map(Pos::x)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
    }

    default int xmax() {
        List<Integer> xs = xs();
        return xs().get(xs.size() - 1);
    }

    default int xmin() {
        return xs().get(0);
    }

    default int xmin(int y) {
        return getPositions()
                .stream()
                .filter(it -> it.y() == y)
                .map(Pos::x)
                .min(Integer::compareTo)
                .get();
    }

    default int xmax(int y) {
        return getPositions()
                .stream()
                .filter(it -> it.y() == y)
                .map(Pos::x)
                .max(Integer::compareTo)
                .get();
    }

    default List<Pos> getPositions() {
        return getPositions(Comparator.comparingInt(Pos::y)
                                      .thenComparingInt(Pos::x)
                           );
    }

    default List<List<Cell<T>>> getColumns() {

        Collection<List<Cell<T>>> columns =
                getCells()
                        .stream()
                        .collect(Collectors.groupingBy(e -> e.pos().x()))
                        .values();

        return columns
                .stream()
                .peek(c -> c.sort(Comparator.comparing(e -> e.pos().y())))
                .collect(Collectors.toList());
    }

    default List<List<Cell<T>>> getColumns(BiPredicate<Pos, T> p) {
        return getColumns().stream()
                           .map(row -> row.stream()
                                          .filter(c -> p.test(c.pos(),
                                                              c.value()
                                                             )
                                                 )
                                          .collect(Collectors.toList()))
                           .collect(Collectors.toList());
    }

    default <U> List<List<U>> getColumns(BiFunction<Pos, T, U> map,
                                         BiPredicate<Pos, T> p
                                        ) {
        return getColumns().stream()
                           .map(row -> row.stream()
                                          .filter(c -> p.test(c.pos(),
                                                              c.value()
                                                             )
                                                 )
                                          .map(c -> map.apply(c.pos(), c.value()))
                                          .collect(Collectors.toList()))
                           .collect(Collectors.toList());
    }

    default List<List<Cell<T>>> getRows() {
        Collection<List<Cell<T>>> rows =
                getCells()
                        .stream()
                        .collect(Collectors.groupingBy(e -> e.pos().y()))
                        .values();
        return rows
                .stream()
                .peek(c -> c.sort(Comparator.comparing(e -> e.pos().x())))
                .collect(Collectors.toList());
    }

    default List<List<Cell<T>>> getRows(BiPredicate<Pos, T> p) {
        return getRows().stream()
                        .map(row -> row.stream()
                                       .filter(c -> p.test(c.pos(),
                                                           c.value()
                                                          )
                                              )
                                       .collect(Collectors.toList()))
                        .collect(Collectors.toList());
    }

    default <U> List<List<U>> getRows(BiFunction<Pos, T, U> map,
                                      BiPredicate<Pos, T> p
                                     ) {
        return getRows().stream()
                        .map(row -> row.stream()
                                       .filter(c -> p.test(c.pos(),
                                                           c.value()
                                                          )
                                              )
                                       .map(c -> map.apply(c.pos(), c.value()))
                                       .collect(Collectors.toList()))
                        .collect(Collectors.toList());
    }

    List<Pos> getPositions(Comparator<Pos> comparator);

    int size();

    boolean isEmpty();

    int hashCode();

    default void printRows() {
        printRows(" ", "");
    }

    default void printRows(String defaultChar, String separator) {
        var xmax = xmax();
        var xmin = xmin();
        var ymax = ymax();
        var ymin = ymin();
        for (int y = ymin; y <= ymax; y++) {
            List<String> row = new ArrayList<>();
            for (int x = xmin; x <= xmax; x++) {
                var pos = new Pos(x, y);
                row.add(containsPos(pos) ?
                                getVal(pos) != null ? getVal(pos).toString() : defaultChar :
                                defaultChar
                       );
            }
            System.out.println(String.join(separator, row));
        }
    }


    default <U> List<U> reduceRows(Function<List<Cell<T>>, U> fn) {
        return getRows().stream()
                        .map(fn)
                        .collect(Collectors.toList());
    }

    default <U> List<U> reduceColumns(Function<List<Cell<T>>, U> fn) {
        return getRows().stream()
                        .map(fn)
                        .toList();

    }

    default List<String> joinRows(String separator,
                                  Function<Cell<T>, String> toStr
                                 ) {
        return reduceRows(l -> l.stream()
                                .map(toStr)
                                .collect(Collectors.joining(separator))
                         );
    }

    default List<String> joinRows() {
        return joinRows("",
                        c -> c.value().toString());
    }

    default List<String> joinColumns(String separator,
                                     Function<Cell<T>, String> toStr
                                    ) {
        return reduceColumns(l -> l.stream()
                                   .map(toStr)
                                   .collect(Collectors.joining(separator)));
    }

    default List<String> joinColumns() {
        return joinColumns("", c -> c.value().toString());
    }

    default Iterator<Cell<T>> iterator() {
        return getCells().stream().iterator();
    }


    default Grid<T> translate(int x, int y) {
        return mapPos((pos, val) -> pos.plus(new Pos(x, y)));
    }

    default Grid<T> rotate90() {
        return mapPos((pos, val) -> pos.swap());

    }

    default Grid<String> color(ControlChar color, BiPredicate<Pos, T> p) {
        return mapValues((pos, val) -> p.test(pos, val) ?
                                 String.format("%s%s%s",
                                               color.command,
                                               val,
                                               ControlChar.RESET.command
                                              ) :
                                 val.toString()
                        );
    }

    default Grid<String> color(ControlChar color) {
        return mapValues((pos, val) -> String.format("%s%s%s",
                                                     color.command,
                                                     val,
                                                     ControlChar.RESET.command
                                                    ));
    }

    default Grid<String> color(ControlChar color, Pos... positions) {
        List<Pos> list = Arrays.stream(positions).toList();
        return color(color, (p, v) -> list.contains(p));
    }


    default Grid<T> flipV() {
        int ymax = ymax();
        return mapPos((pos, val) -> new Pos(pos.x(), -pos.y() + ymax));
    }

    default Grid<T> flipH() {
        int xmax = xmax();
        return mapPos((pos, val) -> new Pos(-pos.x() + xmax, pos.y()));
    }


    default Grid<T> tiles(Iterable<Pos> centers,
                          BiFunction<Pos, Cell<T>, T> map
                         ) {
        Grid<T> acc;
        if (this instanceof PersistentGrid<T>) {
            acc = PersistentGrid.empty();
        } else if (this instanceof MutableGrid<T>) {
            acc = MutableGrid.empty();
        } else {
            throw new IllegalArgumentException();
        }
        for (Pos center : centers) {
            int x = (xmax() - xmin() + 1) * center.x();
            int y = (ymax() - ymin() + 1) * center.y();
            acc = acc.merge(translate(x, y)
                                    .mapValues((pos, val) -> map.apply(center,
                                                                       new Cell<>(pos, val)
                                                                      )));
        }
        return acc;
    }

    default Grid<T> tiles(Iterable<Pos> centers) {
        return tiles(centers,
                     (center, translated) -> translated.value());
    }


}
