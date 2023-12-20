package advent_of_code._2023.day20;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class Conjunction implements Node {
    final String name;
    final Map<Node, Boolean> memory;

    Conjunction(String name) {
        this.name = name;
        memory = new HashMap<>();
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Conjunction con = (Conjunction) object;
        return Objects.equals(name, con.name);
    }

    @Override
    public String toString() {
        return "Conjunction{" +
               "name='" + name + '\'' +
               ", memory=" + memory +
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