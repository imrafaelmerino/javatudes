package types;

import java.util.Arrays;
import java.util.stream.Stream;

public final class StreamFun {


    public static <T> Stream<T> concat(Stream<T> stream, T... t){
        return Stream.concat(stream,Arrays.stream(t));
    }
}
