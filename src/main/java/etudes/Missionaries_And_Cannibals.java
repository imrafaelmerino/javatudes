package etudes;

import search.Action;
import search.BreathSearch;
import search.DepthSearch;
import java.util.List;
import java.util.function.Function;

/**
 * https://en.wikipedia.org/wiki/Missionaries_and_cannibals_problem
 * In the missionaries and cannibals problem, three missionaries
 * and three cannibals must cross a river using a boat which can
 * carry at most two people, under the constraint that, for both
 * banks, if there are missionaries present on the bank, they cannot
 * be outnumbered by cannibals (if they were, the cannibals would
 * eat the missionaries). The boat cannot cross the river by itself
 * with no people on board. And, in some variations, one of the
 * cannibals has only one arm and cannot row.[1]
 * <p>
 * In the jealous husbands problem, the missionaries and cannibals
 * become three married couples, with the constraint that no woman
 * can be in the presence of another man unless her husband is also
 * present. Under this constraint, there cannot be both women and men
 * present on a bank with women outnumbering men, since if there were,
 * these women would be without their husbands. Therefore, upon changing
 * men to missionaries and women to cannibals, any solution to the jealous
 * husbands problem will also become a solution to the missionaries
 * and cannibals problem.[1]
 */
public class Missionaries_And_Cannibals {

    record State(int m_here, int c_here, int m_across, int c_across, boolean boat_here) {
        State cross(int delta_m, int delta_c) {
            return new State(m_here + delta_m,
                             c_here + delta_c,
                             m_across - delta_m,
                             c_across - delta_c,
                             !boat_here
            );
        }
    }

    static State ALL_HERE = new State(3, 3, 0, 0, true);

    static State ALL_ACROSS = new State(0, 0, 3, 3, false);

    static Function<State, List<Action<State>>> succ = s -> {

        if (s.m_here < 0 || s.c_here < 0 || s.c_across < 0 || s.m_across < 0) return List.of();

        if (s.m_here + s.m_across > 3 || s.c_here + s.c_across > 3) return List.of();

        if (s.c_here > s.m_here) return List.of();

        return s.boat_here ?
                List.of(new Action<>("CROSS ACROSS CANNIBAL & MISSIONARY", s.cross(-1, -1)),
                        new Action<>("CROSS ACROSS MISSIONARY", s.cross(0, -1)),
                        new Action<>("CROSS ACROSS CANNIBAL", s.cross(-1, 0)),
                        new Action<>("CROSS ACROSS TWO CANNIBALS", s.cross(-2, 0)),
                        new Action<>("CROSS ACROSS TWO MISSIONARIES", s.cross(0, -2))
                       ) :
                List.of(new Action<>("CROSS HERE CANNIBAL & MISSIONARY", s.cross(1, 1)),
                        new Action<>("CROSS HERE MISSIONARY", s.cross(0, 1)),
                        new Action<>("CROSS HERE CANNIBAL", s.cross(1, 0)),
                        new Action<>("CROSS HERE TWO CANNIBALS", s.cross(2, 0)),
                        new Action<>("CROSS HERE TWO MISSIONARIES", s.cross(0, 2))
                       );
    };

    public Object solve() {

        return BreathSearch.fromActions(succ)
                           .findFirst(ALL_HERE, it -> ALL_ACROSS.equals(it));

    }

    public static void main(String[] args) {
        System.out.println(new Missionaries_And_Cannibals().solve());

       var n = BreathSearch.fromActions(succ).
                stream(ALL_HERE,(sp,p)->!sp.containsState(p)).
                filter(it->it.size() == 10 && it.lastStateEq(ALL_ACROSS)).
                count();

        System.out.println(n);

        var y = DepthSearch.fromActions(succ)
                                   .stream(ALL_HERE,(sp,p)->!sp.containsState(p))
                                   .filter(it->it.size()== 10 && it.lastStateEq(ALL_ACROSS))
                                   .count();

        System.out.println(y);

    }

}



