package types;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Represents a closed range between min and max
 *
 * @param min the lower bound of the range
 * @param max the upper bound of the range
 */
public record Range(long min,
                    long max
) implements Comparable<Range> {
    public Range {
        if (max < min)
            throw new IllegalArgumentException("max < min");
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
     *     result = [ p00, p01, p02, p10, p11, p12, p20, p21, p22]
     *
     *
     * </pre>
     *
     * @param xs x range
     * @param ys y range
     * @return a list of positions
     */
    public static List<Pos> columns(Range xs, Range ys) {
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
    public static List<Pos> rows(Range xs, Range ys) {
        List<Pos> positions = new ArrayList<>();
        for (var y = ys.min(); y <= ys.max(); y++)
            for (var x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x,
                                      y));
        return positions;
    }


    /**
     * Returns the cartesian product of the given ranges xs and ys. Since the method returns a set, it returns the
     * positions in an arbitrary order.
     *
     * @param xs x range
     * @param ys y range
     * @return a set of positions
     * @see #rows(Range, Range) to return the positions arranged in rows ( → ):  00 10 20 01 11 21 02 12 22
     * @see #rows(Range, Range) to return the positions arranged in columns ( ↓ ) : 00 01 02 10 11 12 20 21 22
     */
    public static Set<Pos> cartesian_product(Range xs, Range ys) {
        Set<Pos> positions = new HashSet<>();
        for (var y = ys.min(); y <= ys.max(); y++)
            for (var x = xs.min(); x <= xs.max(); x++)
                positions.add(new Pos(x,
                                      y));
        return positions;
    }

    public static List<Range> union(List<Range> range) {
        if (range.size() <= 1) return range;
        var result = new ArrayList<Range>();
        range.sort(Range::compareTo);
        Range a = range.get(0);
        Range b = range.get(1);
        result.addAll(a.union(b));
        for (int i = 2; i < range.size(); i++) {
            Range c = range.get(i);
            Range last = result.remove(result.size() - 1);
            result.addAll(last.union(c));
        }

        return result;

    }

    public static void main(String[] args) {
        var xs = new ArrayList<Range>();
        var e = new Range(20, 100);
        xs.add(e);
        var f = new Range(100, 200);
        xs.add(f);

        xs.sort(Range::compareTo);

        //System.out.println(xs);

        System.out.println(e.union(f));

        var range = new ArrayList<Range>();
        range.add(new Range(200, 300));
        range.add(new Range(30, 100));
        range.add(new Range(20, 30));
        range.add(new Range(-10, 20));
        System.out.println(Range.union(range));

    }

    /**
     * Returns true if the given range is contained in this one
     *
     * @param other a range
     * @return true if the given range is contained
     */
    public boolean contain(final Range other) {

        return min <= Objects.requireNonNull(other).min && max >= other.max;
    }

    public boolean contain(final long s) {

        return s >= min && s <= max;
    }

    /**
     * Returns true if this and the given ranges overlap
     *
     * @param other a range
     * @return true is both ranges overlap
     */
    public boolean overlap(Range other) {
        return !notOverlap(other);
    }

    /**
     * Returns true if this and the given ranges don't overlap at all
     *
     * @param other a range
     * @return true if both ranges don't overlap
     */
    public boolean notOverlap(Range other) {
        if (min < other.min()) return max < other.min();
        if (max > other.max()) return min > other.max();
        return false;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", min, max);
    }


    public List<Range> union(Range range) {
        var result = new ArrayList<Range>();
        if (range.equals(this)) result.add(this);
        //contained
        if (min >= range.min && max <= range.max)
            result.add(range);
        else if (range.min >= min && range.max <= max)
            result.add(this);
            //no overlap but consecutive
        else if (range.max - min == 1) result.add(new Range(range.min, max));
        else if (range.min - max == 1) result.add(new Range(min, range.max));
            //no overlap and not consecutive
        else if (max < range.min || min > range.max) {
            result.add(this);
            result.add(range);
        }

        //overlap
        else if (min >= range.min) result.add(new Range(range.min, max));

        else result.add(new Range(min, range.max));


        return result;
    }

    @Override
    public int compareTo(Range o2) {
        if (this.equals(o2)) return 0;
        if (this.min < o2.min) return -1;
        if (this.min == o2.min) return this.max < o2.max ? -1 : 1;
        return 1;
    }


    public long length() {
        return max - min + 1;
    }





    public Range intersection(Range other) {
        var min = Math.max(this.min, other.min);
        var max = Math.min(this.max, other.max);
        // Check if there is a valid intersection
        // Return null or some special value to indicate no intersection
        return min <= max ? new Range(min, max) : null;
    }

    public List<Range> intersection(Set<Range> others) {
        return others.stream()
                     .map(this::intersection)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }

    public List<Range> difference(List<Range> others) {
        List<Range> result = ListFun.append(new ArrayList<>(), this);

        for (var other : others)
            result = result.stream()
                           .flatMap(current -> current.difference(other).stream())
                           .toList();

        return result;
    }


    public List<Range> difference(Range other) {
        List<Range> result = new ArrayList<>();

        var min = Math.max(this.min, other.min);
        var max = Math.min(this.max, other.max);

        if (min <= max) {
            // There is an intersection, split into two ranges
            if (this.min < min) result.add(new Range(this.min, min - 1));
            if (this.max > max) result.add(new Range(max + 1, this.max));
        } else result.add(this);            // No intersection, the entire range is retained


        return result;
    }



    public Range translate(long d) {
        return new Range(min + d, max + d);
    }


    public LongStream stream() {
        return LongStream.rangeClosed(min, max);
    }
}
