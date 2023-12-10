package types;

public record Cell<T>(Pos pos,T value) {
    @Override
    public String toString() {
        return "(%s, %s, %s)".formatted(pos.x(),pos.y(),value);
    }
}
