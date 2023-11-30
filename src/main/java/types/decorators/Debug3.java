package types.decorators;


import java.util.function.Function;

public final class Debug3<A, B, C, D> extends AbstractDebug implements Function<A, Function<B, Function<C, D>>> {

    private final Function<A, Function<B, Function<C, D>>> fn;

    public Debug3(Function<A, Function<B, Function<C, D>>> fn) {
        this.fn = fn;
    }

    @Override
    public Function<B, Function<C, D>> apply(A a) {
        return b -> c -> {
            var start = System.nanoTime();
            try {
                return fn.apply(a).apply(b).apply(c);
            } finally {
                updateStats(start);
            }
        };
    }


}
