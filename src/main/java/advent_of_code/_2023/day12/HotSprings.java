package advent_of_code._2023.day12;

import advent_of_code._2023._2023_Puzzle;
import org.paukov.combinatorics3.Generator;
import search.CharFun;
import types.FileParsers;
import types.ListFun;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class HotSprings implements _2023_Puzzle {

    Function<Integer, Pattern> regex = n -> Pattern.compile("([#?]{%d}[.?])|([#?]{%d}$)".formatted(n, n));


    @Override
    public Object solveFirst() throws Exception {
        var arrangements = FileParsers.toListOfLines(getInputPath());
        return arrangements.stream().map(arrangement -> {
            var springs = arrangement.split(" ")[0];
            var groups = Arrays.stream(arrangement.split(" ")[1].trim().split(",")).map(Integer::parseInt).toList();
            return countGroupingWays(springs, groups);
        }).reduce(0L, Long::sum);
    }

    private long countGroupingWays(String springs, List<Integer> groups) {
        var unknownIndexes = new ArrayList<Integer>();
        for (int i = 0; i < springs.length(); i++) if (springs.charAt(i) == '?') unknownIndexes.add(i);
        var permutations = Generator.permutation(List.of('#', '.')).withRepetitions(unknownIndexes.size()).stream().toList();
        var count = 0L;
        for (var per : permutations) {
            var xs = springs.toCharArray();
            for (int i = 0; i < unknownIndexes.size(); i++) xs[unknownIndexes.get(i)] = per.get(i);
            var perGroups = getGroups(xs, 0, 0, new ArrayList<>());
            if (groups.equals(perGroups)) count = count + 1;
        }

        return count;
    }

    private List<Integer> getGroups(char[] xs, int index, int counter, List<Integer> result) {
        if (index == xs.length) return counter > 0 ?
                ListFun.append(result, counter) :
                result;
        var head = xs[index];
        if (head == '#') return getGroups(xs, index + 1, counter + 1, result);
        return getGroups(xs, index + 1, 0, counter > 0 ?
                ListFun.append(result, counter) :
                result);
    }

    /**
     * ???.### 1,1,3 - 1 arrangement .??..??...?##. 1,1,3 - 4 arrangements ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
     * ????.#...#... 4,1,1 - 1 arrangement ????.######..#####. 1,6,5 - 4 arrangements ?###???????? 3,2,1 - 10
     * arrangements
     */
    @Override
    public Object solveSecond() throws Exception {
        var arrangements = FileParsers.toListOfLines(getInputPath());
//        var arrangements = Arrays.stream("""
//                                                 ???.### 1,1,3
//                                                 .??..??...?##. 1,1,3
//                                                 ?#?#?#?#?#?#?#? 1,3,1,6
//                                                 ????.#...#... 4,1,1
//                                                 ????.######..#####. 1,6,5
//                                                 ?###???????? 3,2,1""".split("\n")).toList();
        return unfold(arrangements)
                .stream()
                .map(line -> {
                    var springs = line.split(" ")[0];
                    var blocks = Arrays.stream(line.split(" ")[1].trim().split(",")).map(Integer::parseInt).toList();
                    var blockRegex = blocks.stream().map(regex).toList();
                    return c(springs.toCharArray(), blocks, blockRegex, 0, 0,new HashMap<>());
                })
                .reduce(0L, Long::sum);

    }


    record Key(int index,int blocksCounter){}
    private long c(char[] s,
                   List<Integer> blocks,
                   List<Pattern> blocksRegex,
                   int currentIndex,
                   int blocksCounter,
                   Map<Key,Long> cache
                  ) {

        if (currentIndex > s.length-1)
            return blocksCounter == blocks.size() ? 1 : 0;

        if (blocksCounter == blocks.size())
            return CharFun.containsFrom(s, '#', currentIndex) ? 0 : 1;

        Key key = new Key(currentIndex, blocksCounter);
        if(cache.containsKey(key))return cache.get(key);

        var result = getResult(s, blocks, blocksRegex, currentIndex, blocksCounter,cache);
        cache.put(key,result);

        return result;


    }

    private long getResult(char[] s, List<Integer> blocks, List<Pattern> blocksRegex, int currentIndex, int blocksCounter,
                           Map<Key,Long> cache) {
        var result = 0L;
        char currentChar = s[currentIndex];
        if (currentChar == '.') result = result + c(s, blocks, blocksRegex, currentIndex + 1, blocksCounter,cache);
        else if (currentChar == '#') {
            int nextBlockSize = blocks.get(blocksCounter);
            if (s.length - 1 >= currentIndex + nextBlockSize - 1) {
                var nextBlockPattern = blocksRegex.get(blocksCounter);
                var input = (s.length - 1 == currentIndex + nextBlockSize - 1) ?
                        new String(s, currentIndex, nextBlockSize):
                        new String(s, currentIndex, nextBlockSize+1);
                var matcher = nextBlockPattern.matcher(input);
                boolean matches = matcher.matches();
                if (matches)
                    result = result + c(s, blocks, blocksRegex, currentIndex + nextBlockSize+1, blocksCounter + 1,cache);
            }
        } else {
            result = result
                     + c(CharFun.copyAndReplace(s,
                                                currentIndex,
                                                '.'),
                         blocks, blocksRegex, currentIndex + 1, blocksCounter,cache)
                     +
                     c(CharFun.copyAndReplace(s,
                                              currentIndex,
                                              '#'),
                       blocks,
                       blocksRegex,
                       currentIndex,
                       blocksCounter,cache);
        }
        return result;
    }

    private List<String> unfold(List<String> arrangements) {
        return arrangements.stream().map(line -> {
            var springs = line.split(" ")[0].trim();
            var group = line.split(" ")[1];
            return "%s?%s?%s?%s?%s %s,%s,%s,%s,%s".formatted(springs, springs, springs, springs, springs,
                                                             group, group, group, group, group);
        }).toList();
    }

    @Override
    public String name() {
        return "Hot Springs";
    }

    @Override
    public int day() {
        return 12;
    }

    @Override
    public String outputUnitsPart1() {
        return "possible arrangements";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }


}
