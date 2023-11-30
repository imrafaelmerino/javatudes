package types;

import java.util.Set;

public class SetFun {

    public static <T> Set<T> remove(Set<T> list, T... obj){
        for (T t : obj) list.remove(t);
        return list;
    }
}
