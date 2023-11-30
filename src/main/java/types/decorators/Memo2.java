package types.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Memo2<A, B,C> extends AbstractMemo implements BiFunction<A, B,C> {

    private record Inputs<A,B>(A first,B second){}
    private final Map<Inputs<A,B>,C> cache = new HashMap<>();

    private final BiFunction<A, B,C> fn;

    public Memo2(BiFunction<A, B,C> fn) {
        this.fn = fn;
    }


    @Override
    public C apply(A a,B b) {
        var inputs = new Inputs<>(a,b);
        if (cache.containsKey(inputs)) {
            hits += 1;
            return cache.get(inputs);
        }
        misses += 1;
        C result = fn.apply(a,b);
        cache.put(inputs, result);
        return result;

    }


}
