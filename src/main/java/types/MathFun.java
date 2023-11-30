package types;

public class MathFun {


    public static int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    public static int max(int a, int b, int c, int d) {
        return Math.max(Math.max(Math.max(a, b), c), d);
    }

    public static int max(int a, int b, int c, int d, int e) {
        return Math.max(Math.max(Math.max(Math.max(a, b), c), d),e);
    }

    public static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }



    public static int min(int a, int b, int c, int d) {

        return Math.min(Math.min(Math.min(a, b), c), d);
    }


    public static int min(int a, int b, int c, int d, int e) {

        return Math.min(Math.min(Math.min(Math.min(a, b), c), d),e);
    }


}
