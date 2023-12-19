package types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a mxn matrix of elements of type T
 *
 * @param <T>
 */
public class Matrix<T> {

    public final List<List<T>> rows;
    public final int nColumns;
    public final int nRows;

    private Matrix(List<List<T>> rows) {
        assert rows != null;
        this.rows = rows;
        nRows = rows.size();
        nColumns = rows.getFirst().size();
        for (int i = 1; i < rows.size(); i++) assert rows.get(i).size() == nColumns;
        assert nColumns > 0;
        assert nRows > 0;
    }

    public static void main(String[] args) {
        var cs = Matrix.fromColumns(List.of(List.of(1, 2, 3), List.of(1, 2, 3)));
        var rs = Matrix.fromRows(List.of(List.of(1, 1), List.of(2, 2), List.of(3, 3)));
        System.out.println(cs.equals(rs));
        System.out.println(cs.getColumns().equals(rs.getColumns()));
        System.out.println(cs.getRows().equals(rs.getRows()));
        System.out.println("matrix");
        cs.printRows();
        System.out.println("rotate 90");
        cs.rotate90().printRows();
        System.out.println("rotate -90");
        cs.rotateMinus90().printRows();
    }

    public static <T> Matrix<T> fromColumns(List<List<T>> columns) {
        List<List<T>> rows = new ArrayList<>();
        for (int i = 0; i < columns.get(0).size(); i++) {
            List<T> row = new ArrayList<>();
            for (var column : columns) row.add(column.get(i));
            rows.add(row);
        }
        return Matrix.fromRows(rows);
    }

    public static <T> Matrix<T> fromRows(List<List<T>> rows) {
        return new Matrix<>(rows);
    }

    public List<List<T>> getColumns() {
        List<List<T>> columns = new ArrayList<>();
        for (int i = 0; i < nColumns; i++) {
            var column = new ArrayList<T>();
            for (var row : rows) column.add(row.get(i));
            columns.add(column);
        }
        return columns;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Matrix<?> matrix = (Matrix<?>) object;
        return Objects.equals(rows, matrix.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows);
    }

    public List<List<T>> getRows() {
        return rows;
    }

    public Matrix<T> rotateMinus90() {
        return Matrix.fromRows(getColumns().stream().map(List::reversed).toList());
    }

    public Matrix<T> rotate90() {
        return Matrix.fromColumns(rows);
    }

    public void printRows() {
        rows.forEach(row -> {
            System.out.println(row.stream()
                                  .map(Object::toString)
                                  .collect(Collectors.joining()));
        });
    }
}
