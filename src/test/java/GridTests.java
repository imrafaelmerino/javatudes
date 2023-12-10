import org.junit.Assert;
import org.junit.Test;
import types.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GridTests {

    @Test
    public void tiles() {

        var grid = PersistentGrid.fromRows(List.of(List.of(1, 1), List.of(2, 2)));

        var grid1 =
                grid.tiles(List.of(new Pos(0, 0),
                                   new Pos(0, 1),
                                   new Pos(1, 0),
                                   new Pos(1, 1)
                                  )
                          );

        Assert.assertEquals(PersistentGrid.fromRows(
                                    List.of(List.of(1, 1, 1, 1),
                                            List.of(2, 2, 2, 2),
                                            List.of(1, 1, 1, 1),
                                            List.of(2, 2, 2, 2)
                                           )),
                            grid1);

    }


    @Test
    public void create_Grid_From_Columns() {

        Grid<Integer> grid =
                PersistentGrid.fromColumns(List.of(
                                                   List.of(1, 2, 3, 4),
                                                   List.of(1, 2, 3, 4),
                                                   List.of(1, 2, 3, 4)
                                                  )
                                          );

        Grid<Integer> grid1 =
                PersistentGrid.fromRows(List.of(
                                                List.of(1, 1, 1),
                                                List.of(2, 2, 2),
                                                List.of(3, 3, 3),
                                                List.of(4, 4, 4)
                                               )
                                       );

        Assert.assertEquals(grid, grid1);
        Assert.assertEquals(grid.hashCode(), grid1.hashCode());


    }

    @Test
    public void x_max_and_x_min() {

        var xs = PersistentGrid.fromRows(List.of(
                                                 List.of(1, 1, 1),
                                                 List.of(2, 2, 2),
                                                 List.of(3, 3, 3),
                                                 List.of(4, 4, 4)
                                                )
                                        );

        Assert.assertEquals(0, xs.xmin());
        Assert.assertEquals(2, xs.xmax());
        Assert.assertEquals(0, xs.ymin());
        Assert.assertEquals(3, xs.ymax());

    }

    @Test
    public void get_positions() {

        var xs = PersistentGrid.fromRows(List.of(
                                                 List.of(1, 1, 1),
                                                 List.of(2, 2, 2),
                                                 List.of(3, 3, 3),
                                                 List.of(4, 4, 4)
                                                )
                                        );

        System.out.println(xs.getPositions());

    }

    @Test
    public void merge() {

        PersistentGrid.fromGen(new IntRange(0, 2),
                               new IntRange(0, 2),
                               pos -> Optional.of(1)
                              )
                      .merge(PersistentGrid.fromGen(new IntRange(0, 2),
                                                    new IntRange(3, 5),
                                                    e -> Optional.of(2)
                                                   )
                            )
                      .printRows();

    }

    @Test
    public void borders_persistent_grid() {

        var xs = PersistentGrid.fromRows(List.of(List.of(1, 2, 3),
                                                 List.of(2, 2, 4),
                                                 List.of(3, 3, 5),
                                                 List.of(4, 5, 6)
                                                )
                                        );

        Assert.assertEquals(List.of(1, 2, 3),
                            xs.getBottomBorder()
                              .stream()
                              .map(Cell::value)
                              .collect(Collectors.toList()));
        Assert.assertEquals(List.of(4, 5, 6),
                            xs.getTopBorder()
                              .stream()
                              .map(Cell::value)
                              .collect(Collectors.toList()));
        Assert.assertEquals(List.of(1, 2, 3, 4),
                            xs.getLeftBorder()
                              .stream()
                              .map(Cell::value)
                              .collect(Collectors.toList()));
        Assert.assertEquals(List.of(3, 4, 5, 6),
                            xs.getRightBorder()
                              .stream()
                              .map(Cell::value)
                              .collect(Collectors.toList()));


    }

    @Test
    public void borders_mutable_grid() {

        var xs = MutableGrid.fromRows(List.of(
                                              List.of(1, 2, 3),
                                              List.of(2, 2, 4),
                                              List.of(3, 3, 5),
                                              List.of(4, 5, 6)
                                             )
                                     );

        Assert.assertEquals(List.of(1, 2, 3), xs.getBottomBorder()
                                                .stream().map(Cell::value).toList());
        Assert.assertEquals(List.of(4, 5, 6), xs.getTopBorder()
                                                .stream().map(Cell::value).toList());
        Assert.assertEquals(List.of(1, 2, 3, 4), xs.getLeftBorder()
                                                   .stream().map(Cell::value).toList());
        Assert.assertEquals(List.of(3, 4, 5, 6), xs.getRightBorder()
                                                   .stream().map(Cell::value).toList());


    }


}
