package advent_of_code.advent_of_code_2022.day8;

import types.*;

import java.io.IOException;
import java.util.*;

public class Day8 {

    public static void main(String[] args) throws IOException {

        //String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day8/input_test_2.txt";
        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day8/input.txt";

        var grid = PersistentGrid.fromFile(input).mapValues((pos, v) -> Integer.parseInt(v));


        var trees = grid.getCells()
                        .stream()
                        .filter(e -> isVisible(grid,
                                               e.pos(),
                                               e.value()
                                              ))
                        .map(Cell::pos)
                        .toList();

        grid.color(ControlChar.RED,(p,v)->trees.contains(p)).printRows();

        System.out.println(trees.size());

        var scores = grid.mapValues((pos, high) -> score(grid, pos, high));

        Cell<Integer> max = scores.getCells()
                                  .stream()
                                  .max(Comparator.comparingInt(Cell::value))
                                  .get();
        System.out.println(max);

        //extra, plot the views from the treetop tree house
        var views = view(grid,
                         max.pos(),
                         grid.getVal(max.pos())
                        );

        grid.color(ControlChar.RED,(p,v)->views.contains(v)).printRows();


    }

    private static boolean isVisible(Grid<Integer> lines, Pos pos, int height) {
        return isVisibleUp(lines, pos, height)
                || isVisibleDown(lines, pos, height)
                || isVisibleRight(lines, pos, height)
                || isVisibleLeft(lines, pos, height);
    }


    static boolean isVisibleUp(Grid<Integer> grid, Pos pos, int height) {
        return pos.y() == grid.ymin()
                || (height > grid.getVal(pos.down())
                && isVisibleUp(grid, pos.down(), height));

    }

    static boolean isVisibleDown(Grid<Integer> grid, Pos pos, int height) {

        return pos.y() == grid.ymax()
                || (height > grid.getVal(pos.up())
                && isVisibleDown(grid, pos.up(), height));

    }

    static boolean isVisibleRight(Grid<Integer> grid, Pos pos, int height) {

        return pos.x() == grid.xmax()
                || (height > grid.getVal(pos.right())
                && isVisibleRight(grid, pos.right(), height));

    }

    static boolean isVisibleLeft(Grid<Integer> grid, Pos pos, int height) {

        return pos.x() == grid.xmin()
                || (height > grid.getVal(pos.left())
                && isVisibleLeft(grid, pos.left(), height));

    }


    private static int score(Grid<Integer> lines, Pos pos, int height) {
        return countUp(lines, pos, height, 0) * countDown(lines, pos, height, 0) *
                countRight(lines, pos, height, 0) * countLeft(lines, pos, height, 0);
    }


    static int countUp(Grid<Integer> grid, Pos pos, int height, int r) {

        if (pos.y() == grid.ymin()) return r;
        if (height <= grid.getVal(pos.down())) return r + 1;
        return countUp(grid, pos.down(), height, r + 1);

    }

    static int countDown(Grid<Integer> grid, Pos pos, int height, int r) {

        if (pos.y() == grid.ymax()) return r;
        if (height <= grid.getVal(pos.up())) return r + 1;
        return countDown(grid, pos.up(), height, r + 1);

    }

    static int countRight(Grid<Integer> grid, Pos pos, int height, int r) {

        if (pos.x() == grid.xmax()) return r;
        if (height <= grid.getVal(pos.right())) return r + 1;
        return countRight(grid, pos.right(), height, r + 1);

    }

    static int countLeft(Grid<Integer> grid, Pos pos, int height, int r) {

        if (pos.x() == grid.xmin()) return r;
        if (height <= grid.getVal(pos.left())) return r + 1;
        return countLeft(grid, pos.left(), height, r + 1);


    }

    private static List<Pos> view(Grid<Integer> lines, Pos pos, int height) {
        var trees = new ArrayList<Pos>();
        trees.add(pos);
        trees.addAll(viewUp(lines, pos, height, trees));
        trees.addAll(viewDown(lines, pos, height, trees));
        trees.addAll(viewR(lines, pos, height, trees));
        trees.addAll(viewL(lines, pos, height, trees));
        return trees;
    }


    static List<Pos> viewUp(Grid<Integer> grid, Pos pos, int height, List<Pos> trees) {

        if (pos.y() == grid.ymin()) {
            trees.add(pos);
            return trees;
        }
        Pos down = pos.down();
        trees.add(down);
        return height <= grid.getVal(down) ?
                trees :
                viewUp(grid, down, height, trees);

    }

    static List<Pos> viewDown(Grid<Integer> grid, Pos pos, int height, List<Pos> trees) {

        if (pos.y() == grid.ymax()) {
            trees.add(pos);
            return trees;
        }
        var up = pos.up();
        trees.add(up);
        return height <= grid.getVal(up) ?
                trees :
                viewDown(grid, up, height, trees);

    }

    static List<Pos> viewR(Grid<Integer> grid, Pos pos, int height, List<Pos> trees) {

        if (pos.x() == grid.xmax()) {
            trees.add(pos);
            return trees;
        }
        var right = pos.right();
        trees.add(right);
        return height <= grid.getVal(right) ?
                trees :
                viewR(grid, right, height, trees);

    }

    static List<Pos> viewL(Grid<Integer> grid, Pos pos, int height, List<Pos> trees) {

        if (pos.x() == grid.xmin()) {
            trees.add(pos);
            return trees;
        }
        var left = pos.left();
        trees.add(left);
        return height <= grid.getVal(left) ?
                trees :
                viewL(grid, left, height, trees);


    }


}
