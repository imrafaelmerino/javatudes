package advent_of_code._2023.day20;

public final class Unknown implements Node {

    public static final Unknown INSTANCE = new Unknown();

    private Unknown() {
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }



}