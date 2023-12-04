package advent_of_code.advent_of_code_2023.day4;

import types.FileParsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Scratchcards {
    public static void main(String[] args) {


        var path = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2023/day4/input.txt";

        var cards = FileParsers.toListOfLines(path)
                               .stream()
                               .map(Scratchcards::toCard)
                               .toList();


        var sol_part1 = cards.stream()
                             .map(Card::value)
                             .reduce(Long::sum)
                             .get();

        System.out.println(sol_part1);

        Map<Long, Card> map = cards.stream().collect(Collectors.toMap(c -> (long) c.number, c -> c));

        System.out.println(pileCards(map).size() + map.size());

    }

    private static List<Card> pileCards(Map<Long, Card> originals) {
        return originals.entrySet().stream().flatMap(e -> getWonCards(originals, e.getValue()).stream()).toList();
    }

    private static List<Card> getWonCards(Map<Long, Card> originals, Card card) {

        if (card.value == 0) return List.of();
        List<Card> wonCards = LongStream.range(card.number + 1, card.number + 1 + card.value)
                                        .mapToObj(originals::get).toList();
        var result = new ArrayList<>(wonCards);
        for (Card wonCard : wonCards) result.addAll(getWonCards(originals, wonCard));
        return result;
    }

    private static Card toCard(String line) {
        var card_numbers = line.split(":");
        var id = Integer.parseInt(card_numbers[0].split(" +")[1]);
        var numbers_wining_numbers = card_numbers[1].split("\\|");
        var str_wining_numbers = numbers_wining_numbers[0].trim().split(" +");
        var str_numbers = numbers_wining_numbers[1].trim().split(" +");
        var numbers = Arrays.stream(str_numbers)
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .toList();
        var wining_numbers = Arrays.stream(str_wining_numbers)
                                   .mapToInt(Integer::parseInt)
                                   .boxed()
                                   .toList();

        long size =
                wining_numbers
                        .stream()
                        .filter(numbers::contains)
                        .count();
        //part 1
        //return new Card(id,
        //                ((int) Math.pow(2, size - 1))
        //);

        return new Card(id,
                        size);
    }


    record Card(int number, long value) {
    }
}
