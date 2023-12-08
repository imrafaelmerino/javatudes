package advent_of_code._2023.day4;

import advent_of_code.Puzzle;
import types.FileParsers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ScratchCards implements Puzzle {

    static final Pattern LINE =
            Pattern.compile("Card\\s+(?<id>\\d+): (?<winning>[\\s\\d]+) \\| (?<numbers>[\\s\\d]+)");


    private static void updateCardCounter(Map<Card, Long> counters,
                                          Map<Long, Card> cards,
                                          Card card
                                         ) {

        if (!card.winningIHave.isEmpty()) {
            for (var id = card.number + 1; id < card.number + 1 + card.winningIHave.size(); id++) {
                var next = cards.get(id);
                counters.compute(next,
                                 (c, n) -> n + 1
                                );
                updateCardCounter(counters, cards, next);
            }
        }
    }

    private static Card toCard(String line) {
        Matcher matcher = LINE.matcher(line);
        if (!matcher.matches()) throw new RuntimeException(line);
        var id = Integer.parseInt(matcher.group("id"));
        var numbers = toListOfInt(matcher.group("numbers"));
        var winning = toListOfInt(matcher.group("winning"));
        return new Card(id,
                        winning.stream().filter(numbers::contains).toList());
    }

    private static List<Integer> toListOfInt(String numbers) {
        return Arrays.stream(numbers.trim().split("\\s+"))
                     .map(Integer::parseInt).toList();
    }

    @Override
    public Object solveFirst() throws Exception {

        var cards = FileParsers.toListOfLines(getInputPath())
                               .stream()
                               .map(ScratchCards::toCard)
                               .toList();


        return cards.stream()
                    .map(card -> ((long) Math.pow(2, card.winningIHave.size() - 1)))
                    .reduce(0L,
                            Long::sum);

    }

    @Override
    public Object solveSecond() throws Exception {
        var cards = FileParsers.toListOfLines(getInputPath())
                               .stream()
                               .map(ScratchCards::toCard)
                               .toList();

        var cardsByNumber = cards.stream()
                                 .collect(Collectors.toMap(e -> e.number,
                                                           e -> e
                                                          )
                                         );

        var counters = cards.stream()
                            .collect(Collectors.toMap(c -> c,
                                                      c -> 1L
                                                     )
                                    );

        counters.forEach((card, n) -> updateCardCounter(counters,
                                                        cardsByNumber,
                                                        card));
        return counters.values()
                       .stream()
                       .reduce(0L,
                               Long::sum);

    }

    @Override
    public String name() {
        return "ScratchCards";
    }

    @Override
    public int day() {
        return 4;
    }

    @Override
    public String getInputPath() {
        return "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/_2023/day4/input.txt";
    }

    @Override
    public String outputUnitsPart1() {
        return "points";
    }

    @Override
    public String outputUnitsPart2() {
        return "scratchcards";
    }


    record Card(long number, List<Integer> winningIHave) {
    }
}
