package etudes;

import search.Action;
import search.BreathSearch;
import types.ListFun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A farmer went to a market and purchased a wolf, a goat, and a cabbage. On his way home, the farmer came to the bank
 * of a river and rented a boat. But crossing the river by boat, the farmer could carry only himself and a single one
 * of his purchases: the wolf, the goat, or the cabbage.
 * <p>
 * If left unattended together, the wolf would eat the goat, or the goat would eat the cabbage.
 * <p>
 * The farmer's challenge was to carry himself and his purchases to the far bank of the river, leaving each purchase
 * intact.
 */
class Wolf_Goat_And_Cabbage {
    static List<String> ALL = List.of("WOLF", "GOAT", "CABBAGE", "FARMER");

    record State(List<String> here, List<String> across) {

        boolean farmerHere() {
            return here.contains("FARMER");
        }

        boolean farmerAcross() {
            return !farmerHere();
        }

        State crossAcross(List<String> stuff) {
            var here = new ArrayList<>(this.here);
            here.removeAll(stuff);
            var across = new ArrayList<>(this.across);
            across.addAll(stuff);
            return new State(here, across);
        }

        State cross(List<String> stuff){
            return farmerHere() ? crossAcross(stuff) : crossHere(stuff);
        }

        State crossHere(List<String> stuff) {
            var swap = new State(across, here).crossAcross(stuff);
            return new State(swap.across, swap.here);
        }

        boolean containsHere(String... stuff){
            return Arrays.stream(stuff).allMatch(here::contains);
        }

        boolean containsAcross(String... stuff){
            return Arrays.stream(stuff).allMatch(across::contains);
        }

        boolean isGoal(){
            return across.containsAll(ALL);
        }
    }

    public static void main(String[] args) {

        var ALL_HERE = new State(ALL, List.of());

        Function<List<String>, String> action = s -> "Boat carry " + String.join(" and ", s);

        Function<List<String>, List<List<String>>> combinations = bank ->
                ListFun.add(
                        ListFun.allPairs(bank)
                               .stream()
                               .filter(pair -> pair.contains("FARMER")) //only pairs with a farmer
                               .collect(Collectors.toList()),
                        List.of("FARMER") //farmer  can cross alone
                           );

        Function<State, List<Action<State>>> succ = s -> {
            if (s.farmerHere() && (s.containsAcross("WOLF","GOAT")
                    || s.containsAcross("CABBAGE","GOAT"))) return List.of();

            if (s.farmerAcross() && (s.containsHere("WOLF","GOAT")
                    || s.containsHere("CABBAGE","GOAT"))) return List.of();

            return
                    combinations
                            .apply(s.farmerHere() ? s.here : s.across)
                            .stream()
                            .map(com -> new Action<>(action.apply(com),
                                                     s.cross(com)))
                            .collect(Collectors.toList());


        };

        BreathSearch.fromActions(succ)
                             .stream(ALL_HERE,(path,state)-> !path.containsState(state))
                             .filter(path -> path.lastStateSuchThat(State::isGoal))
                             .forEach(System.out::println);


    }
}



