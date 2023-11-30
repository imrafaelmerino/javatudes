package types.decorators;

import java.util.function.Function;

public final class Debug<A, B> extends AbstractDebug implements Function<A, B> {

    public Debug(final Function<A, B> fn) {
        this.fn = fn;
    }

    private final Function<A, B> fn;

    @Override
    public B apply(A a) {
        var start = System.nanoTime();
        try {
            return fn.apply(a);
        } finally {
            updateStats(start);
        }
    }


}
