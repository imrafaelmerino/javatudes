package types.decorators;

import java.util.Objects;
import java.util.function.Function;

public class Log3<A, B, C, D> extends AbstractDebug implements Function<A, Function<B, Function<C, D>>> {

    public Log3(Function<A, Function<B, Function<C, D>>> fn) {
        this.fn = Objects.requireNonNull(fn);
    }

    Function<A, Function<B, Function<C, String>>> logInput = a -> b -> c -> String.format("(%s, %s, %s)", a, b, c);

    Function<D, String> logOutput = Objects::toString;

    private final Function<A, Function<B, Function<C, D>>> fn;


    @Override
    public Function<B, Function<C, D>> apply(A a) {
        return b -> c -> {
            System.out.println(logInput.apply(a).apply(b).apply(c));
            D d = fn.apply(a).apply(b).apply(c);
            System.out.println(logOutput.apply(d));
            return d;
        };
    }

    public Log3<A, B, C, D> setLogInput(Function<A, Function<B, Function<C, String>>> logInput) {
        this.logInput = Objects.requireNonNull(logInput);
        return this;
    }

    public Log3<A, B, C, D> setLogOutput(Function<D, String> logOutput) {
        this.logOutput = Objects.requireNonNull(logOutput);
        return this;
    }
}
