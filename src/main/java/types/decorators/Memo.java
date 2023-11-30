package types.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memo<A, B>  extends AbstractMemo implements Function<A, B>  {

    private final Map<A, B> cache = new HashMap<>();

    private final Function<A, B> fn;

    public Memo(Function<A, B> fn) {
        this.fn = fn;
    }


    @Override
    public B apply(A a) {
        if (cache.containsKey(a)) {
            hits += 1;
            return cache.get(a);
        }
        misses += 1;
        B result = fn.apply(a);
        cache.put(a, result);
        return result;

    }


}
