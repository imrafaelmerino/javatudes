package advent_of_code._2023.day9;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;
import types.ListFun;
import types.StrFun;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MirageMaintenance implements _2023_Puzzle {
    private static int getNextSeqNumber(List<Integer> sequence) {
        return nextSequences(ListFun.append(new ArrayList<>(),
                                            sequence)).stream()
                                                      .map(List::getLast)
                                                      .reduce(0, Integer::sum);
    }

    private static List<List<Integer>> nextSequences(List<List<Integer>> sequences) {
        var lastSeq = sequences.getLast();
        var next = ListFun.sliding(lastSeq, 2)
                          .map(pair -> pair.get(1) - pair.get(0))
                          .collect(Collectors.toList());
        if (next.stream().allMatch(n -> n == 0)) return sequences;
        return nextSequences(ListFun.append(sequences, next));

    }

    /**
     * current - x = previous -> x = current - previous
     *
     * @param sequence
     * @return
     */
    private static int getNextSeqNumberPartII(List<Integer> sequence) {
        var sequences = nextSequencesPartII(ListFun.append(new ArrayList<>(),
                                                           sequence)
                                           );
        var next = 0;
        for (var seq : sequences.reversed()) next = seq.getFirst() - next;
        return next;
    }

    private static List<List<Integer>> nextSequencesPartII(List<List<Integer>> sequences) {
        var lastSeq = sequences.getLast();
        var next = ListFun.sliding(lastSeq, 2)
                          .map(pair -> pair.get(1) - pair.get(0))
                          .collect(Collectors.toList());
        if (next.stream().allMatch(n -> n == 0)) return sequences;
        return nextSequencesPartII(ListFun.append(sequences, next));

    }

    @Override
    public Object solveFirst() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());

        return lines.stream()
                    .map(StrFun::parseSpacedInts)
                    .map(MirageMaintenance::getNextSeqNumber)
                    .reduce(0, Integer::sum);
    }

    @Override
    public Object solveSecond() throws Exception {
        var lines = FileParsers.toListOfLines(getInputPath());

        return lines.stream()
                    .map(StrFun::parseSpacedInts)
                    .map(MirageMaintenance::getNextSeqNumberPartII)
                    .reduce(0, Integer::sum);
    }

    @Override
    public String name() {
        return "Mirage Maintenance";
    }

    @Override
    public int day() {
        return 9;
    }


    @Override
    public String outputUnitsPart1() {
        return "sum of the next sequences values";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }
}
