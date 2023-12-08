package advent_of_code._2023.day7;

import advent_of_code.Puzzle;
import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class CamelCard implements _2023_Puzzle {


    private static int breakTie(String cards1, String cards2, List<Character> cards) {
        for (int i = 0; i < cards1.length(); i++) {
            var char1 = cards1.charAt(i);
            var char2 = cards2.charAt(i);
            if (char1 != char2)
                return cards.indexOf(char1) < cards.indexOf(char2) ? 1 : -1;
        }

        return 0;
    }


    //same comparator for both parts but the strength of the cards is different!
    // in the second part the J has the lowest strength to break the ties
    static Comparator<Hand> getHandComparator(List<Character> CARDS) {
        return (hand1, hand2) -> {
            var n = Integer.compare(hand1.rank,
                                    hand2.rank);
            return n == 0 ? breakTie(hand1.cards,
                                     hand2.cards,
                                     CARDS
                                    )
                    : n;
        };
    }


    private static Hand getBestHand(Hand hand,
                                    List<Character> alternativeCards,
                                    Comparator<Hand> comparator
                                   ) {

        return alternativeCards.stream()
                               .map(String::valueOf)
                               .map(type -> hand.cards.replace("J", type))
                               .map(handReplaced -> new Hand(hand.cards,
                                                             hand.bid(),
                                                             getRank(handReplaced)
                                    )
                                   )
                               .max(comparator)
                               .get();

    }

    private static int getRank(String cards) {
        var set = Arrays.stream(cards.split(""))
                        .collect(Collectors.toSet());

        var occurrences = Arrays.stream(cards.split(""))
                                .collect(Collectors.groupingBy(k -> k,
                                                               Collectors.counting()));
        if (set.size() == 5) return 1; //higher kind
        if (set.size() == 4) return 2; //one pair
        if (set.size() == 3 && occurrences.values().stream().noneMatch(n -> n == 3)) return 3; //two pair
        if (set.size() == 3) return 4; //three of a kind
        if (set.size() == 2 && occurrences.values().stream().anyMatch(n -> n == 3)) return 5; //full house
        if (set.size() == 2) return 6; //four of a kind
        if (set.size() == 1) return 7; //five of a kind
        throw new IllegalArgumentException();
    }

    @Override
    public Object solveFirst()  {
        var cards =
                Arrays.stream("A,K,Q,J,T,9,8,7,6,5,4,3,2".split(","))
                      .map(it -> it.charAt(0))
                      .toList();

        var sortedByRanK = FileParsers.toListOfLines(getInputPath())
                                      .stream()
                                      .map(line -> line.split("\\s"))
                                      .map(tokens -> {
                                          var hand = tokens[0];
                                          var bid = Integer.parseInt(tokens[1]);
                                          var rank = getRank(hand);
                                          return new Hand(hand, bid, rank);
                                      })
                                      .sorted(getHandComparator(cards))
                                      .toList();

        return IntStream.range(0, sortedByRanK.size())
                        .mapToObj(n -> (n + 1) * sortedByRanK.get(n).bid)
                        .reduce(0L,
                                Long::sum);
    }

    @Override
    public Object solveSecond()  {
        var cards =
                Arrays.stream("A,K,Q,T,9,8,7,6,5,4,3,2,J".split(","))
                      .map(it -> it.charAt(0))
                      .toList();

        var comparator = getHandComparator(cards);

        var sortedByRanK = FileParsers.toListOfLines(getInputPath())
                                      .stream()
                                      .map(line -> line.split("\\s"))
                                      .map(tokens -> {
                                          var hand = new Hand(tokens[0], Integer.parseInt(tokens[1]), getRank(tokens[0]));
                                          return hand.cards.contains("J") ?
                                                  getBestHand(hand,
                                                              cards,
                                                              comparator
                                                             ) :
                                                  hand;

                                      })
                                      .sorted(comparator)
                                      .toList();
        return IntStream.range(0, sortedByRanK.size())
                        .mapToObj(n -> (n + 1) * sortedByRanK.get(n).bid)
                        .reduce(0L,
                                Long::sum);
    }

    @Override
    public String name() {
        return "Camel Cards";
    }

    @Override
    public int day() {
        return 7;
    }


    @Override
    public String outputUnitsPart1() {
        return "total winnings";
    }

    @Override
    public String outputUnitsPart2() {
        return outputUnitsPart1();
    }

    record Hand(String cards, long bid, int rank) {
    }
}
