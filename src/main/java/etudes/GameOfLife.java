package etudes;

import types.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameOfLife {

    final String cellSymbol;

    public GameOfLife(String cellSymbol) {
        this.cellSymbol = cellSymbol;
    }

    public GameOfLife() {
        this("\u03BF");
    }

    public long countNeighborCells(Grid<String> grid, Pos p) {
        return grid.getNeighbors(p)
                   .stream()
                   .filter(e -> grid.getVal(e).equals(cellSymbol))
                   .count();

    }

    public Grid<String> births(Grid<String> desserts) {
        Stream<Pos> candidates = desserts.filter((pos, val) -> val.equals(cellSymbol))
                                         .getPositions()
                                         .stream()
                                         .flatMap(pos -> pos.getNeighbors().stream())
                                         .filter(pos -> desserts.getVal(pos) == null);
        var newbirths = candidates
                .filter(p -> countNeighborCells(desserts, p) == 3)
                .collect(Collectors.toSet());

        return PersistentGrid.fromCells(newbirths.stream()
                                                 .map(pos-> new Cell<>(pos,cellSymbol))
                                                 .collect(Collectors.toList())
                                       );

    }

    public Grid<String> removeDeaths(Grid<String> grid) {
        var deaths = grid.filter((pos, val) -> {
                             long n = countNeighborCells(grid, pos);
                             return n < 2 || n > 3;
                         })
                         .getPositions();


        return grid.filter((pos, val) -> !deaths.contains(pos));

    }

    public Grid<String> nextGen(Grid<String> grid) {
        return removeDeaths(grid).merge(births(grid));
    }


    public Stream<Grid<String>> gen(Grid<String> grid) {
        return Stream.iterate(grid,
                              g-> !g.filter((pos,val)->val.equals(cellSymbol)).isEmpty(),
                              this::nextGen
                             );
    }


    public static void main(String[] args) {

        String first = "οοοοο οοοοο οοοοο";

        var stream = new GameOfLife().gen(PersistentGrid.fromString(first));

        stream.limit(100)
              .forEach(it -> {
            it.printRows();
            System.out.println("\n");
        });


    }


}
