package advent_of_code._2023.day20;

public interface Node {
    static Node parse(String name) {
        if (name.equals("broadcaster")) return Broadcaster.INSTANCE;
        if (name.startsWith("%")) return new FlipFlop(name.substring(1));
        if (name.startsWith("&")) return new Conjunction(name.substring(1));
        throw new RuntimeException("unkown: " + name);
    }

    String name();
}