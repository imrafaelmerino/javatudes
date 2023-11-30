package types.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memo3<A, B, C, D>  extends AbstractMemo implements Function<A, Function<B, Function<C, D>>> {

    @Override
    public Function<B, Function<C, D>> apply(A a) {
        return b -> c -> {
            var inputs = new Inputs<>(a, b, c);
            if (cache.containsKey(inputs)) {
                hits += 1;
                return cache.get(inputs);
            }
            misses += 1;
            D result = fn.apply(a)
                         .apply(b)
                         .apply(c);
            cache.put(inputs,
                      result
                     );
            return result;
        };
    }

    private record Inputs<A, B, C>(A first, B second, C third) {}

    private final Map<Inputs<A, B, C>, D> cache = new HashMap<>();

    private final Function<A, Function<B, Function<C, D>>> fn;

    public Memo3(Function<A, Function<B, Function<C, D>>> fn) {
        this.fn = fn;
    }


}
