package types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.util.Objects.requireNonNull;

/**
 * Represents a closed range of long numbers between min and max
 *
 * @param min the lower bound of the range
 * @param max the upper bound of the range
 */
public record LongRange(long min, long max) implements Comparable<LongRange> {
    public LongRange {
        if (max < min)
            throw new IllegalArgumentException("max (%s) < min (%s)".formatted(max, min));
    }


    /**
     * Computes the union of a list of ranges.
     *
     * @param range a list of ranges
     * @return the union of the input ranges
     * <p>
     * Example:
     * <pre>
     *     List<Range> ranges = Arrays.asList(new Range(1, 3), new Range(2, 4), new Range(5, 7));
     *     List<Range> result = Range.union(ranges);
     * </pre>
     */
    public static List<LongRange> union(List<LongRange> range) {
        if (range.size() <= 1) return range;
        var result = new ArrayList<LongRange>();
        range.sort(LongRange::compareTo);
        LongRange a = range.get(0);
        LongRange b = range.get(1);
        result.addAll(a.union(b));
        for (int i = 2; i < range.size(); i++) {
            LongRange c = range.get(i);
            LongRange last = result.remove(result.size() - 1);
            result.addAll(last.union(c));
        }

        return result;

    }


    /**
     * Returns true if the given range is contained in this one
     *
     * @param other a range
     * @return true if the given range is contained
     */
    public boolean contain(final LongRange other) {
        return min <= requireNonNull(other).min && max >= other.max;
    }

    /**
     * Checks if the given value is contained in this range.
     *
     * @param s a value
     * @return true if the value is contained
     * <p>
     * Example:
     * <pre>
     *     Range range = new Range(1, 5);
     *     boolean isContained = range.contain(3);
     * </pre>
     */
    public boolean contain(final long s) {

        return s >= min && s <= max;
    }

    /**
     * Checks if this and the given range overlap.
     *
     * @param other a range
     * @return true if both ranges overlap
     * <p>
     * Example:
     * <pre>
     *     Range range1 = new Range(1, 5);
     *     Range range2 = new Range(3, 7);
     *     boolean overlap = range1.overlap(range2);
     * </pre>
     */
    public boolean overlap(LongRange other) {
        return !notOverlap(other);
    }

    /**
     * Checks if this and the given range don't overlap at all.
     *
     * @param other a range
     * @return true if both ranges don't overlap
     * <p>
     * Example:
     * <pre>
     *     Range range1 = new Range(1, 5);
     *     Range range2 = new Range(7, 10);
     *     boolean noOverlap = range1.notOverlap(range2);
     * </pre>
     */
    public boolean notOverlap(LongRange other) {
        if (min < other.min()) return max < other.min();
        if (max > other.max()) return min > other.max();
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", min, max);
    }


    /**
     * Computes the union of a list of ranges.
     *
     * @param range a list of ranges
     * @return the union of the input ranges
     * <p>
     * Example:
     * <pre>
     *     List<Range> ranges = Arrays.asList(new Range(1, 3), new Range(2, 4), new Range(5, 7));
     *     List<Range> result = Range.union(ranges);
     * </pre>
     */
    public List<LongRange> union(LongRange range) {
        var result = new ArrayList<LongRange>();
        if (range.equals(this)) result.add(this);
        //contained
        if (min >= range.min && max <= range.max) result.add(range);
        else if (range.min >= min && range.max <= max) result.add(this);
        else if (range.max - min == 1) result.add(new LongRange(range.min, max));
        else if (range.min - max == 1) result.add(new LongRange(min, range.max));
        else if (max < range.min || min > range.max) {
            result.add(this);
            result.add(range);
        }

        //overlap
        else if (min >= range.min) result.add(new LongRange(range.min, max));

        else result.add(new LongRange(min, range.max));


        return result;
    }

    @Override
    public int compareTo(LongRange o2) {
        if (this.equals(o2)) return 0;
        if (this.min < o2.min) return -1;
        if (this.min == o2.min) return this.max < o2.max ? -1 : 1;
        return 1;
    }


    public long length() {
        return max - min + 1;
    }


    public LongRange intersection(LongRange other) {
        var min = Math.max(this.min, other.min);
        var max = Math.min(this.max, other.max);
        var result = min <= max ? new LongRange(min, max) : null;
        return result;
    }

    public List<LongRange> intersection(Set<LongRange> others) {
        return others.stream()
                     .map(this::intersection)
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }

    public List<LongRange> difference(List<LongRange> others) {
        List<LongRange> result = ListFun.append(new ArrayList<>(), this);

        for (var other : others)
            result = result.stream()
                           .flatMap(current -> current.difference(other).stream())
                           .toList();

        return result;
    }


    public List<LongRange> difference(LongRange other) {
        List<LongRange> result = new ArrayList<>();
        if (other.equals(this)) return result;

        var min = Math.max(this.min,
                           other.min);
        var max = Math.min(this.max,
                           other.max);

        if (min < max) {
            if (this.min < min)
                result.add(new LongRange(this.min, min - 1));
            if (this.max > max)
                result.add(new LongRange(max + 1, this.max));

        } else if (min == max) {
            // Case when min is equal to max (single point)
            if (min == this.min) result.add(new LongRange(min + 1, this.max));
            else if (max == this.max) result.add(new LongRange(this.min, max - 1));
        } else result.add(this);


        return result;
    }


    public LongRange translate(long d) {
        return new LongRange(min + d, max + d);
    }


    public LongStream stream() {
        return LongStream.rangeClosed(min, max);
    }
}
