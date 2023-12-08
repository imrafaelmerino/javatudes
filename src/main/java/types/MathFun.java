package types;

import java.math.BigInteger;
import java.util.Collection;

public class MathFun {


    public static int max(int... values) {
        if (values.length == 0) throw new IllegalArgumentException("No values provided");
        var maxValue = values[0];
        for (int i = 1; i < values.length; i++) maxValue = Math.max(maxValue, values[i]);
        return maxValue;
    }

    public static int min(int... values) {
        if (values.length == 0) throw new IllegalArgumentException("No values provided");
        var minValue = values[0];
        for (int i = 1; i < values.length; i++) minValue = Math.min(minValue, values[i]);
        return minValue;
    }




    public static BigInteger lcm(long... values) {
        if (values.length == 0) throw new IllegalArgumentException("No values provided");
        var value = BigInteger.valueOf(values[0]);
        for (int i = 1; i < values.length; i++) value = lcm(value, BigInteger.valueOf(values[i]));
        return value;
    }

    public static BigInteger lcm(Collection<Long> values) {
        if (values.isEmpty()) throw new IllegalArgumentException("No values provided");
        return values.stream().map(BigInteger::valueOf).reduce(BigInteger.ONE, MathFun::lcm);
    }

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        var gcd = number1.gcd(number2);
        return number1.multiply(number2).abs().divide(gcd);
    }

    public static BigInteger lcm(long number1, long number2) {
        var bd1 = BigInteger.valueOf(number1);
        var bd2 = BigInteger.valueOf(number2);
        var gcd = bd1.gcd(bd2);
        return bd1.multiply(bd2).abs().divide(gcd);
    }

}
