package types;

import java.util.HashMap;
import java.util.Map;

public final class MapFun {

    public static <K, V> Map<K, V> copyAndPut(Map<K, V> map, K k, V v) {
          var copy =  new HashMap<>(map);
          copy.put(k,v);
          return copy;
    }

    public static <K, V> Map<K, V> put(Map<K, V> result, K k, V v) {
        result.put(k,v);
        return result;
    }
}
