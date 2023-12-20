package advent_of_code._2023.day20;


import java.util.Objects;

final class FlipFlop implements Node {
    private final String name;
    public  boolean status;


    public FlipFlop(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        FlipFlop ff = (FlipFlop) object;
        return Objects.equals(name, ff.name);
    }

    @Override
    public String toString() {
        return "FlipFlop{" +
               "name='" + name + '\'' +
               ", status=" + status +
               '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String name() {
        return name;
    }



}