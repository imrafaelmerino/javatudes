package search;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharFun {


    public static boolean contains(char[] chars, char x){
        for (char aChar : chars) {
            if (aChar == x) return true;
        }
        return false;
    }

    public static int repeatedTil(char[] xs, int start,char x){
        assert xs[start] == x;
        int end = start;
        for (int i = start+1; i < xs.length; i++) {
            System.out.println("end is "+end);
            if (xs[i] == x) end = end + 1;
            else break;
        }
        return end;
    }

    public static boolean containsFrom(char[] chars, char x,int from){
        for (int i = 0; i < chars.length; i++) {
            if (i >= from && chars[i] == x) return true;
        }


        return false;
    }

    public static char[] copy(char[] chars){
        return Arrays.copyOf(chars,chars.length);
    }

    public static char[] copyAndReplace(char[] chars,int index, char replacement){
        var xs = copy(chars);
        xs[index] = replacement;
        return xs;
    }




}
