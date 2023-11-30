package search;


import java.util.Objects;

public record Action<State>(String name, State state) {

    public Action {
        Objects.requireNonNull(name);
        Objects.requireNonNull(state);
    }
    public Action(final State state) {
        this("", state);
    }

    @Override
    public String toString() {
        return name.isEmpty() ?
                state.toString() :
                String.format("(%s, %s)", name, state);
    }
}
