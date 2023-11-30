package types.decorators;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Log2<A, B,C> extends AbstractDebug implements BiFunction<A, B,C> {

    public Log2(final BiFunction<A, B,C> fn) {
        this.fn = Objects.requireNonNull(fn);
    }

    BiFunction<A, B,String> logInput = (a,b) -> String.format("(%s, %s)",a,b);

    Function<C, String> logOutput = Objects::toString;

    private final BiFunction<A, B,C> fn;

    @Override
    public C apply(A a,B b) {
        System.out.println(logInput.apply(a,b));
        C c = fn.apply(a,b);
        System.out.println(logOutput.apply(c));
        return c;

    }

    public Log2<A, B,C> setLogInput(BiFunction<A, B, String> logInput) {
        this.logInput = Objects.requireNonNull(logInput);
        return this;
    }

    public Log2<A, B,C> setLogOutput(Function<C, String> logOutput) {
        this.logOutput = Objects.requireNonNull(logOutput);
        return this;
    }
}
