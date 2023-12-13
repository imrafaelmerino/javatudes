package advent_of_code._2023.day10;

import advent_of_code._2023._2023_Puzzle;
import search.DepthSearch1;
import search.SearchPath;
import types.Cell;
import types.Grid;
import types.MutableGrid;
import types.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class PipeMaze implements _2023_Puzzle {


    private static final String SOUTH = "SOUTH";
    private static final String NORTH = "NORTH";
    private static final String EAST = "EAST";
    private static final String WEST = "WEST";
    static Map<String, List<String>> CONNECTIONS = Map.of(
            "|", List.of(SOUTH, NORTH),
            "L", List.of(NORTH, EAST),
            "-", List.of(EAST, WEST),
            "J", List.of(NORTH, WEST),
            "7", List.of(SOUTH, WEST),
            "F", List.of(SOUTH, EAST)
                                                         );
    static String input1 = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
                        """;
    static String input2 = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """;


    @Override
    public Object solveFirst() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath());
        //var grid = MutableGrid.fromString(input1);
        var start = grid.findOne((pos, sym) -> sym.equals("S"));
        var pipes = CONNECTIONS.keySet();
        for (var pipe : pipes) {
//            var path = findLoop(grid.put(start.pos(), pipe), new Cell<>(start.pos(), pipe));
//            if (!path.isEmpty()) {
//                var loop_size = path.size();
//                return (loop_size - 1) % 2 == 0
//                        ? loop_size / 2
//                        : loop_size / 2 + 1;
//            }
            return null;
        }
        throw new RuntimeException("Sorry, you didn't find the loop!");
    }

    private SearchPath<Cell<String>> findLoop(Grid<String> grid,
                                              Cell<String> start
                                             ) {
        return DepthSearch1.fromStates(getSuccessors(grid))
                           .findFirst(start, path -> path.size() > 3 && path.last().state().pos()
                                                                            .equals(path.first()
                                                                                        .state()
                                                                                        .pos()));


    }

    private Function<SearchPath<Cell<String>>, List<Cell<String>>> getSuccessors(Grid<String> grid) {

        return path -> {

            List<Cell<String>> successors = new ArrayList<>();

            var pos = path.last().state().pos();
            var symbol = path.last().state().value();
            var connections = CONNECTIONS.get(symbol);

            var up = pos.up();
            var upVal = grid.getVal(up);
            var upCell = new Cell<>(up, upVal);
            if (grid.containsPos(up) && !upVal.equals(".") && connections.contains(SOUTH) && CONNECTIONS.get(upVal).contains(NORTH)) {
                if (!path.containsState(upCell) || (path.size() > 2 && path.first().state().equals(upCell)))
                    successors.add(upCell);
            }

            var down = pos.down();
            var downVal = grid.getVal(down);
            var downCell = new Cell<>(down, downVal);
            if (grid.containsPos(down) && !downVal.equals(".") && connections.contains(NORTH) && CONNECTIONS.get(downVal).contains(SOUTH))
                if (!path.containsState(downCell) || (path.size() > 2 && path.first().state().equals(downCell)))
                    successors.add(downCell);

            var right = pos.right();
            var rightVal = grid.getVal(right);
            var righCell = new Cell<>(right, rightVal);

            if (grid.containsPos(right) && !rightVal.equals(".") && connections.contains(EAST) && CONNECTIONS.get(rightVal).contains(WEST))
                if (!path.containsState(righCell) || (path.size() > 2 && path.first().state().equals(righCell)))
                    successors.add(righCell);

            var left = pos.left();
            var leftVal = grid.getVal(left);
            var leftCell = new Cell<>(left, leftVal);

            if (grid.containsPos(left) && !leftVal.equals(".") && connections.contains(WEST) && CONNECTIONS.get(leftVal).contains(EAST))
                if (!path.containsState(leftCell) || (path.size() > 2 && path.first().state().equals(leftCell)))
                    successors.add(leftCell);
            return successors;
        };

    }

    /**
     * One simple way of finding whether the point is inside or outside a simple polygon is to test how many times a
     * ray, starting from the point and going in any fixed direction, intersects the edges of the polygon. If the point
     * is on the outside of the polygon the ray will intersect its edge an even number of times. If the point is on the
     * inside of the polygon then it will intersect the edge an odd number of times. The status of a point on the edge
     * of the polygon depends on the details of the ray intersection algorithm.
     * <p>
     * This algorithm is sometimes also known as the crossing number algorithm or the even–odd rule algorithm, and was
     * known as early as 1962.[3] The algorithm is based on a simple observation that if a point moves along a ray from
     * infinity to the probe point and if it crosses the boundary of a polygon, possibly several times, then it
     * alternately goes from the outside to inside, then from the inside to the outside, etc. As a result, after every
     * two "border crossings" the moving point goes outside. This observation may be mathematically proved using the
     * Jordan curve theorem.
     * <p>
     * A similar problem arises with horizontal segments that happen to fall on the ray. The issue is solved as follows:
     * If the intersection point is a vertex of a tested polygon side, then the intersection counts only if the other
     * vertex of the side lies below the ray. This is effectively equivalent to considering vertices on the ray as lying
     * slightly above the ray.
     * <p>
     * En nuestro caso -------7  count as 1 -------J  count as 0 L-------  count as 0 F-------- count as 1 por lo tanto
     * contar 7F| del path por linea, par fuera, impar dentro
     */
    @Override
    public Object solveSecond() throws Exception {
        var grid = MutableGrid.fromFile(getInputPath());
        //var grid = MutableGrid.fromString(input1);
        var start = grid.findOne((pos, sym) -> sym.equals("S"));
        var pipes = CONNECTIONS.keySet();
        for (var pipe : pipes) {
            var path = findLoop(grid.put(start.pos(), pipe), new Cell<>(start.pos(), pipe));

            if (!path.isEmpty()) {

                var xs = path.states().stream().map(Cell::pos).toList();
                var positions = xs.subList(0, path.size() - 1);
                grid.put((pos, val) -> !positions.contains(pos), (pos, val) -> ".");
                grid.put((pos, val) -> val.equals("7"), (pos, val) -> "┐");
                grid.put((pos, val) -> val.equals("J"), (pos, val) -> "┘");
                grid.put((pos, val) -> val.equals("L"), (pos, val) -> "└");
                grid.put((pos, val) -> val.equals("F"), (pos, val) -> "┌");

                grid.printRows();

                var candidates = grid.find((pos, val) -> !positions.contains(pos)).stream().map(Cell::pos).toList();

                var count = 0;
                Set<String> strings = Set.of("┐", "┌", "|");
                for (Pos candidate : candidates) {
                    var c = positions.stream()
                                     .filter(p -> p.y() == candidate.y() && p.x() > candidate.x())
                                     .filter(p -> strings.contains(grid.getVal(p))).count();
                    if (c % 2 == 1) {

                        count = count + 1;
                    }
                }

                return count;

            }
        }
        throw new RuntimeException("Sorry, you didn't find the loop!");


    }

    @Override
    public String name() {
        return "Pipe Maze";
    }

    @Override
    public int day() {
        return 10;
    }

    @Override
    public String outputUnitsPart1() {
        return "steps";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }

}
