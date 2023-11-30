package types;

import java.util.ArrayList;
import java.util.List;

public record Pos(int x, int y) {

    public static List<Pos> columns(Range xs, Range ys) {
        List<Pos> positions = new ArrayList<>();
        for (int x = xs.min(); x <= xs.max(); x++)
            for (int y = ys.min(); y <= ys.max(); y++)
                positions.add(new Pos(x, y));
        return positions;
    }

    public static List<Pos> rows(Range xs, Range ys) {
        List<Pos> positions = new ArrayList<>();
        for (int y = ys.min(); y <= ys.max(); y++)
            for (int x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x, y));
        return positions;
    }

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
        return List.of(
                right(),
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
        return List.of(
                right(),
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


