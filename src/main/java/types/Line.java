package types;

import java.math.BigDecimal;

public class Line {

    public final Coor p1;
    public final Coor p2;

    public Line(Coor p1, Coor p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    //to avoid overlflows I had to work with bigdecimal
    public Coor intersection(Line line) {
        Coor p3 = line.p1;
        Coor p4 = line.p2;

        var x1 = BigDecimal.valueOf(p1.x());
        var y1 = BigDecimal.valueOf(p1.y());
        var x2 = BigDecimal.valueOf(p2.x());
        var y2 = BigDecimal.valueOf(p2.y());
        var x3 = BigDecimal.valueOf(p3.x());
        var y3 = BigDecimal.valueOf(p3.y());
        var x4 = BigDecimal.valueOf(p4.x());
        var y4 = BigDecimal.valueOf(p4.y());

        var den = (x1.subtract(x2)).multiply(y3.subtract(y4))
                                   .subtract((y1.subtract(y2)).multiply(x3.subtract(x4)));

        if (den.compareTo(BigDecimal.ZERO) == 0) return null; // Denominator is zero, no intersection

        var a = x1.multiply(y2).subtract(y1.multiply(x2));
        var b = x3.multiply(y4).subtract(y3.multiply(x4));

        var resultX = (a.multiply(x3.subtract(x4))
                        .subtract((x1.subtract(x2))
                                          .multiply(b))).divide(den);

        var resultY = (a.multiply(y3.subtract(y4)).subtract((y1.subtract(y2)).multiply(b))).divide(den);

        return new Coor(resultX.doubleValue(), resultY.doubleValue());
    }

    public record Coor(double x, double y) {
    }


}
