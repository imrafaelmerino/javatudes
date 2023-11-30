package types.decorators;

import java.util.function.BiFunction;

public final class Debug2<A,B,C> extends AbstractDebug implements BiFunction<A, B,C> {

    public Debug2(BiFunction<A, B, C> fn) {
        this.fn = fn;
    }

    private final BiFunction<A, B,C> fn;

    @Override
    public C apply(A a, B b) {

        var start = System.nanoTime();
        try {
            return fn.apply(a,b);
        } finally {
            updateStats(start);
        }
    }


}
