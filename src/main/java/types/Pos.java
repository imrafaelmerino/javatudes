package types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record Pos(int x, int y) {

    /**
     * Generates a line of positions between the specified start and end points.
     *
     * @param start The starting position.
     * @param end   The ending position.
     * @return A list of positions representing the line from start to end.
     */
    public static List<Pos> line(Pos start, Pos end) {
        var result = new ArrayList<Pos>();
        result.add(start);
        while (!start.equals(end)) {
            if (start.x() > end.x()) start = start.left();
            else if (start.x() < end.x()) start = start.right();
            else if (start.y() > end.y()) start = start.down();
            else start = start.up();
            result.add(start);
        }
        return result;
    }

    /**
     * Returns the cartesian product of the given ranges xs and ys:
     * <pre>
     *
     *     xs = (0,2)
     *     ys = (0,3)
     *
     *     p00  p10  p20
     *     p01  p11  p21
     *     p02  p12  p22
     *     p03  p13  p33
     *
     *     result = [ p00, p01, p02, p03, p10, p11, p12, p13, p20, p21, p22, p33]
     *
     *
     * </pre>
     *
     * @param xs x range
     * @param ys y range
     * @return a list of positions
     */
    public static List<Pos> columns(IntRange xs, IntRange ys) {
        var positions = new ArrayList<Pos>();
        for (var x = xs.min(); x <= xs.max(); x++)
            for (var y = ys.min(); y <= ys.max(); y++)
                positions.add(new Pos(x,
                                      y)
                             );
        return positions;
    }

    /**
     * Returns the cartesian product of the given ranges xs and ys:
     * <pre>
     *
     *     xs = (0,3)
     *     ys = (0,2)
     *
     *     p00  p10  p20  p30
     *     p01  p11  p21  p31
     *     p02  p12  p22  p32
     *
     *     result = [ p00, p10, p20, p30, p01, p11, p21, p31, p02, p12, p22, p32]
     *
     *
     * </pre>
     *
     * @param xs x range
     * @param ys y range
     * @return a list of positions
     */
    public static List<Pos> rows(IntRange xs,
                                 IntRange ys
                                ) {
        var positions = new ArrayList<Pos>();
        for (var y = ys.min(); y <= ys.max(); y++)
            for (var x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x, y));
        return positions;
    }

    /**
     * Adds another Pos to this Pos and returns the result.
     *
     * @param other The Pos to be added.
     * @return A new Pos representing the sum of this Pos and the other Pos.
     */
    public Pos plus(Pos other) {
        return new Pos(x + other.x, y + other.y);
    }

    /**
     * Subtracts another Pos from this Pos and returns the result.
     *
     * @param other The Pos to be subtracted.
     * @return A new Pos representing the difference between this Pos and the other Pos.
     */
    public Pos minus(Pos other) {
        return new Pos(x - other.x, y - other.y);
    }

    /**
     * Returns the position to the left of this Pos.
     *
     * @return A new Pos to the left of this Pos.
     */
    public Pos left() {
        return this.minus(new Pos(1, 0));
    }

    /**
     * Returns the position to the right of this Pos.
     *
     * @return A new Pos to the right of this Pos.
     */
    public Pos right() {
        return this.plus(new Pos(1, 0));
    }

    /**
     * Swaps the x and y coordinates of this Pos.
     *
     * @return A new Pos with swapped x and y coordinates.
     */
    public Pos swap() {
        return new Pos(y, x);
    }

    /**
     * Returns the position above this Pos.
     *
     * @return A new Pos above this Pos.
     */
    public Pos up() {
        return this.plus(new Pos(0, 1));
    }

    /**
     * Returns the position below this Pos.
     *
     * @return A new Pos below this Pos.
     */
    public Pos down() {
        return this.minus(new Pos(0, 1));
    }

    /**
     * Returns a list of neighboring positions, including diagonal neighbors.
     *
     * @return A list of neighboring positions.
     */
    public List<Pos> getNeighbors() {
        return List.of(right(),
                       left(),
                       down(),
                       up(),
                       up().left(),
                       up().right(),
                       down().left(),
                       down().right()
                      );
    }

    /**
     * Returns a list of neighboring positions in vertical and horizontal directions only.
     *
     * @return A list of neighboring positions in vertical and horizontal directions.
     */
    public List<Pos> neighborsVH() {
        return List.of(right(),
                       left(),
                       down(),
                       up()
                      );
    }

    /**
     * Multiplies the coordinates of this Pos by a scalar value.
     *
     * @param s The scalar value.
     * @return A new Pos with coordinates multiplied by the scalar value.
     */
    public Pos multiply(int s) {
        return new Pos(x * s, y * s);
    }

    /**
     * Divides the coordinates of this Pos by a scalar value.
     *
     * @param s The scalar value.
     * @return A new Pos with coordinates divided by the scalar value.
     */
    public Pos divide(int s) {
        return new Pos(x / s, y / s);
    }

    /**
     * Calculates the Manhattan distance between this Pos and another Pos.
     *
     * @param other The other Pos.
     * @return The Manhattan distance between this Pos and the other Pos.
     */
    public int manhattanDistance(Pos other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    /**
     * Calculates the Euclidean distance between this Pos and another Pos.
     *
     * @param other The other Pos.
     * @return The Euclidean distance between this Pos and the other Pos.
     */
    public double euclideanDistance(Pos other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }





}


