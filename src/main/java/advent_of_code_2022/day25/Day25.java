package advent_of_code_2022.day25;

import fun.tuple.Pair;
import types.FileParsers;

import java.util.Map;

public class Day25 {

    static Map<String,Integer> map = Map.of("2",2,"1",1,"0",0,"-",-1,"=",-2);
    public static void main(String[] args) {

        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day25/input.txt";

        var t = FileParsers.toListOfLines(input)
                   .stream()
                   .map(i->toDec(i.trim()))
                   .reduce(0L, Long::sum);
        System.out.println(t);
        System.out.println(toSnafu(t));



    }

    static long toDec(String n){
        var acc = 0L;
        for (int i = 0; i < n.length(); i++) {
            acc+=Math.pow(5.0,i)*map.get(n.substring(n.length()-1-i,
                                                     n.length()-i)
                                        );
        }
        return acc;
    }

    static String toSnafu(long n){
        var mod = n;
        var div = n;
        var acc = "";
        do{
            mod = div % 5;
            div = div / 5;
            var p = toSnafuDigit(mod);
            acc=p.first()+acc;
            div+=p.second(); //carry
        }
        while (div >= 5);
        var c = toSnafuDigit(div).second();
        return (c==0 ? "" : c)+toSnafuDigit(div).first()+acc;
    }

    static Pair<String,Integer> toSnafuDigit(long n){
        if(n==0 || n==1 || n ==2) return Pair.of(n+"",0);
        if (n == 3) return Pair.of("=",1);
        return  Pair.of("-",1);
    }



}
