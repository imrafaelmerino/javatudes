package advent_of_code._2023.day20;

public class Broadcaster implements Node {

    public static final Broadcaster INSTANCE = new Broadcaster();

    private Broadcaster(){}
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }



}