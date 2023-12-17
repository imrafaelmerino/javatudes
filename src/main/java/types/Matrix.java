package types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Matrix<T> {

    List<List<T>> rows;

    int nColumns;
    int nRows;


    public Matrix(List<List<T>> rows) {
        this.rows = requireNonNull(rows);
        nRows = requireNonNull(rows.get(0)).size();
        nColumns = rows.size();
    }


    public List<List<T>> getColumns(){
//        List<List<T>> columns = new ArrayList<>();
//        for (int r = 0; r < nRows; r++) {
//            for (int c = 0; c < nColumns; c++) {
//                var column = new ArrayList<T>();
//                column.add(rows.get())
//            }
//        }
        return null;
    }

    public List<List<T>> getRows(){
        return rows;
    }
}
