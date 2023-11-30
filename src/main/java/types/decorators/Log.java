package types.decorators;

import java.util.Objects;
import java.util.function.Function;

public class Log<A, B> extends AbstractDebug implements Function<A, B> {

    public Log(final Function<A, B> fn) {
        this.fn = Objects.requireNonNull(fn);
    }

    Function<A, String> logInput = Object::toString;

    Function<B, String> logOutput = Object::toString;

    public Log<A, B> setLogInput(Function<A, String> logInput) {
        this.logInput = Objects.requireNonNull(logInput);
        return this;
    }

    public Log<A, B> setLogOutput(Function<B, String> logOutput) {
        this.logOutput = Objects.requireNonNull(logOutput);
        return this;
    }

    private final Function<A, B> fn;

    @Override
    public B apply(A a) {
        System.out.println(logInput.apply(a));
        B b = fn.apply(a);
        System.out.println(logOutput.apply(b));
        return b;
    }
}
