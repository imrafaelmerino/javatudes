package advent_of_code._2023.day4;

import types.FileParsers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Scratchcards {

    static final Pattern LINE =
            Pattern.compile("Card\\s+(?<id>\\d+): (?<winning>[\\s\\d]+) \\| (?<numbers>[\\s\\d]+)");

    public static void main(String[] args) {


        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day4/input.txt";

        var cards = FileParsers.toListOfLines(path)
                               .stream()
                               .map(Scratchcards::toCard)
                               .toList();


        var sol_part1 = cards.stream()
                             .map(card -> ((long) Math.pow(2, card.winningIHave.size() - 1)))
                             .reduce(Long::sum)
                             .get();

        System.out.println(sol_part1);

        var cardsByNumber = cards.stream()
                                 .collect(Collectors.toMap(e -> e.number, e -> e));

        var counters = cards.stream().collect(Collectors.toMap(c -> c, c -> 1L));

        System.out.println(getPileSize(counters, cardsByNumber));

    }

    private static Long getPileSize(Map<Card, Long> counters, Map<Long, Card> cards) {

        counters.forEach((card, n) -> updateCardCounter(counters,
                                                        cards,
                                                        card));
        return counters.values()
                       .stream()
                       .reduce(Long::sum)
                       .get();
    }

    private static void updateCardCounter(Map<Card, Long> counters, Map<Long, Card> cards, Card card) {

        if (!card.winningIHave.isEmpty()) {
            for (var wonCard : LongStream.range(card.number + 1,
                                                card.number + 1 + card.winningIHave.size())
                                         .mapToObj(cards::get)
                                         .toList()) {
                counters.compute(wonCard, (c, n) -> n + 1);
                updateCardCounter(counters, cards, wonCard);
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


    record Card(long number, List<Integer> winningIHave) {
    }
}
