package types;

import java.util.ArrayList;
import java.util.List;

public record Pos(int x, int y) {

    /**
     * todo poner examples
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Pos> line(Pos start, Pos end) {
        return line(start, end, new ArrayList<>());
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
        List<Pos> positions = new ArrayList<>();
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
     *     xs = (0,2)
     *     ys = (0,2)
     *
     *     p00  p10  p20
     *     p01  p11  p21
     *     p02  p12  p22
     *
     *     result = [ p00, p10, p20, p01, p11, p21, p02, p12, p22]
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
        List<Pos> positions = new ArrayList<>();
        for (var y = ys.min(); y <= ys.max(); y++)
            for (var x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x, y));
        return positions;
    }


    /**
     * Returns the cartesian product of the given ranges xs and ys.
     *
     * @param xs x range
     * @param ys y range
     * @return a set of positions
     * <p>
     * Example:
     * <pre>
     *     Range xs = new Range(0, 2);
     *     Range ys = new Range(0, 2);
     *     List<Pos> result = Range.cartesian_product(xs, ys);
     * </pre>
     * @see #rows(IntRange, IntRange) to return the positions arranged in rows ( → ): 00 10 20 01 11 21 02 12 22
     * @see #rows(IntRange, IntRange) to return the positions arranged in columns ( ↓ ) : 00 01 02 10 11 12 20 21 22
     */
    public static List<Pos> cartesian_product(IntRange xs, IntRange ys) {
        List<Pos> positions = new ArrayList<>();
        for (var y = ys.min(); y <= ys.max(); y++)
            for (var x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x, y));
        return positions;
    }

    static List<Pos> line(Pos start, Pos end, List<Pos> result) {
        result.add(start);
        if (start.equals(end)) return result;
        if (start.x() > end.x()) {
            result.add(start.left());
            return line(start.left(), end, result);
        } else if (start.x() < end.x()) {
            result.add(start.right());
            return line(start.right(), end, result);
        } else if (start.y() > end.y()) {
            result.add(start.down());
            return line(start.down(), end, result);
        }

        result.add(start.up());
        return line(start.up(), end,
                    result
                   );


    }

    public Pos plus(Pos other) {
        return new Pos(x + other.x, y + other.y);
    }

    public Pos minus(Pos other) {
        return new Pos(x - other.x, y - other.y);
    }

    public Pos left() {
        return this.minus(new Pos(1, 0));
    }

    public Pos right() {
        return this.plus(new Pos(1, 0));
    }

    public Pos swap() {
        return new Pos(y, x);
    }

    public Pos up() {
        return this.plus(new Pos(0, 1));
    }

    public Pos down() {
        return this.minus(new Pos(0, 1));
    }

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

    public List<Pos> neighborsVH() {
        return List.of(right(),
                       left(),
                       down(),
                       up()
                      );
    }

    public Pos multiply(int s) {
        return new Pos(x * s, y * s);
    }

    public Pos divide(int s) {
        return new Pos(x / s, y / s);
    }

    public int manhattanDistance(Pos other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public double euclideanDistance(Pos other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }


}


