package types;

import java.util.List;

public final class Polygon {


    /**
     * Returns the area using the shoelace formula. The shoelace formula, also known as Gauss's area formula and the
     * surveyor's formula,[1] is a mathematical algorithm to determine the area of a simple polygon whose vertices are
     * described by their Cartesian coordinates in the plane.[2] It is called the shoelace formula because of the
     * constant cross-multiplying for the coordinates making up the polygon, like threading shoelaces.[2] It has
     * applications in surveying and forestry,[3] among other areas.
     *
     * @param points the list of points
     * @return the area
     */
    public static double area(List<Pos> points) {
        assert points != null;
        assert points.size() > 2 : "The points must represent a polygon. Two point made up a line...";
        var minuend = 0L;
        var subtrahend = 0L;
        for (int i = 1; i < points.size(); i++) {
            minuend = minuend + ((long) points.get(i - 1).x() * points.get(i).y());
            subtrahend = subtrahend + ((long) points.get(i).x() * points.get(i - 1).y());
        }
        minuend = minuend + ((long) points.getLast().x() * points.getFirst().y());
        subtrahend = subtrahend + ((long) points.getFirst().x() * points.getLast().y());

        return Math.abs(minuend - subtrahend) / 2d;

    }

    /**
     * In geometry, Pick's theorem provides a formula for the area of a simple polygon with integer vertex coordinates,
     * in terms of the number of integer points within it and on its boundary. The result was first described by Georg
     * Alexander Pick in 1899.[2] It was popularized in English by Hugo Steinhaus in the 1950 edition of his book
     * Mathematical Snapshots.[3][4] It has multiple proofs, and can be generalized to formulas for certain kinds of
     * non-simple polygons.
     *
     * @param internalPoints the number of integer points interior to the polygon
     * @param boundaryPoints the number of integer points on its boundary (including both vertices and points along the
     *                       sides)
     * @return the area
     */
    public static double area(long internalPoints, long boundaryPoints) {
        assert internalPoints > 0;
        assert boundaryPoints > 0;
        return internalPoints + boundaryPoints / 2d - 1;
    }

    /**
     * Based on the Pick's theorem
     *
     * @param area           the area
     * @param boundaryPoints the number of integer points on its boundary (including both vertices and points along the
     *                       sides)
     * @return the number of integer points interior to the polygon
     */
    public static long internalPoints(double area, long boundaryPoints) {
        assert area > 0;
        assert boundaryPoints > 0;
        return (long) ((area + 1) - boundaryPoints / 2d);
    }


}
