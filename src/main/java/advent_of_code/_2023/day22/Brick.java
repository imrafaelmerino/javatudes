package advent_of_code._2023.day22;

import types.IntRange;

import java.util.Objects;


final class Brick {

    public int sx;
    public int sy;
    public int sz;

    public int ex;
    public int ey;
    public int ez;

    public Brick(int sx, int sy, int sz,
                 int ex, int ey, int ez
                ) {
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.ex = ex;
        this.ey = ey;
        this.ez = ez;
        assert ez > 0;
        assert sz > 0;
        assert ex >= 0;
        assert ey >= 0;
        assert sx >= 0;
        assert sy >= 0;
    }

    boolean intersect(Brick brick) {
        return new IntRange(sx, ex).intersection(new IntRange(brick.sx, brick.ex)) != null &&
               new IntRange(sy, ey).intersection(new IntRange(brick.sy, brick.ey)) != null;

    }

    @Override
    public String toString() {
        return "%s,%s,%s~%s,%s,%s".formatted(sx, sy, sz, ex, ey, ez);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Brick brick = (Brick) object;
        return sx == brick.sx && sy == brick.sy && sz == brick.sz && ex == brick.ex && ey == brick.ey && ez == brick.ez;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sx, sy, sz, ex, ey, ez);
    }
}