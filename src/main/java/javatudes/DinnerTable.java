package javatudes;


import search.csp.BinaryConstraint;
import search.csp.Constraint;
import search.csp.FindFirst;
import types.ListFun;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * <pre>
 *         1
 *       ----
 * 2  /      \  6
 *
 * 3  \     /   5
 *      ----
 *       4
 * </pre>
 * You just bought a 6-sided table (because it looks like a benzene ring) and want to hold a dinner party.
 * You invite your 4 best friends: McCain, Obama, Biden and Palin.
 * Luckily a moose wanders by and also accepts your invitation.
 * Counting yourself, you have 6 guests for seats labeled 1-6.
 * Your guests have seven seating demands:
 * ● Palin wants to sit next to McCain
 * ● Biden wants to sit next to Obama
 * ● Neither McCain nor Palin will sit next to Obama or Biden
 * ● Neither Obama nor Biden will sit next to McCain or Palin
 * ● The moose is afraid to sit next to Palin
 * ● No two people can sit in the same seat, and no one can sit in 2 seats.
 * ● McCain insists on sitting in seat 1 4
 */
public class DinnerTable {

    public static final String OBAMA = "OBAMA";
    public static final String MCCAIN = "MCCAIN";
    public static final String BIDEN = "BIDEN";
    public static final String PALIN = "PALIN";
    public static final String MOOSE = "MOOSE";
    public static final String ME = "ME";

    public static final List<String> ALL = List.of(OBAMA, MCCAIN, BIDEN, PALIN, MOOSE, ME);
    public static final List<String> ALL_BUT_PALIN = ALL.stream().filter(it -> !it.equals(PALIN)).toList();


    public static final Map<String, List<String>> DOMAIN =
            Map.of("1", ALL,
                   "2", ALL_BUT_PALIN,
                   "3", ALL_BUT_PALIN,
                   "4", ALL,
                   "5", ALL_BUT_PALIN,
                   "6", ALL_BUT_PALIN
                  );

    public static final Map<String, List<String>> NEXT_SEAT =
            Map.of("1", List.of("2", "6"),
                   "2", List.of("1", "3"),
                   "3", List.of("2", "4"),
                   "4", List.of("3", "5"),
                   "5", List.of("4", "6"),
                   "6", List.of("1", "5")
                  );
    public static final BiPredicate<String, String> NEXT_TO_EACH_OTHER =
            (seat1, seat2) -> NEXT_SEAT.get(seat1).contains(seat2);


    public static void main(String[] args) {

        var variables = DOMAIN.keySet();
        List<Constraint<String, String>> constraints =
                ListFun.appendAll(Constraint.allDifferent(variables),
                                  BinaryConstraint.all(PALIN, MCCAIN, NEXT_TO_EACH_OTHER,
                                                    BIDEN, OBAMA, NEXT_TO_EACH_OTHER,
                                                    MCCAIN, OBAMA, NEXT_TO_EACH_OTHER.negate(),
                                                    PALIN, OBAMA, NEXT_TO_EACH_OTHER.negate(),
                                                    PALIN, BIDEN, NEXT_TO_EACH_OTHER.negate(),
                                                    MOOSE, PALIN, NEXT_TO_EACH_OTHER.negate()
                                                   )
                                 );

        var sol = new FindFirst<>(variables, DOMAIN, constraints).findFirst();

        System.out.println(sol);


    }


}
