package etudes;

import search.csp.Constraint;
import search.csp.FindFirst;
import types.ListFun;

import java.util.List;
import java.util.Map;

/**
 * The MIT Time Travel Society (MITTTS) has invited seven famous historical figures to each give a
 * lecture at the annual MITTTS convention, and you've been asked to create a schedule for them.
 * Unfortunately, there are only four time slots available, and you discover that there are some
 * restrictions on how you can schedule the lectures and keep all the convention attendees happy. For
 * instance, physics students will be disappointed if you schedule Niels Bohr and Isaac Newton to
 * speak during the same time slot, because those students were hoping to attend both of those
 * lectures.
 * After talking to some students who are planning to attend this year's convention, you determine
 * that they fall into certain groups, each of which wants to be able to see some subset of the
 * timetraveling speakers. (Fortunately, each student identifies with at most one of the groups.) You write
 * down everything you know:
 * The list of guest lecturers consists of Alan Turing, Ada Lovelace, Niels Bohr, Marie Curie,
 * Socrates, Pythagoras, and Isaac Newton.
 * 1) Turing has to get home early to help win World War II, so he can only be assigned to the
 * 1pm slot.
 * 2) The Course VIII students want to see the physicists: Bohr, Curie, and Newton.
 * 3) The Course XVIII students want to see the mathematicians: Lovelace, Pythagoras, and
 * Newton.
 * 4) The members of the Ancient Greece Club wants to see the ancient Greeks: Socrates and
 * Pythagoras.
 * 5) The visiting Wellesley students want to see the female speakers: Lovelace and Curie.
 * 6) The CME students want to see the British speakers: Turing, Lovelace, and Newton.
 * 7) Finally, you decide that you will be happy if and only if you get to see both Curie and
 * Pythagoras. (Yes, even if you belong to one or more of the groups above.)
 **/
public class TaskScheduling {

    public static final String TURING = "TURING";
    public static final String LOVELACE = "LOVELACE";
    public static final String BOHR = "BOHR";
    public static final String CURIE = "CURIE";
    public static final String SOCRATES = "SOCRATES";
    public static final String PYTHAGORAS = "PYTHAGORAS";
    public static final String NEWTON = "NEWTON";

    public static final List<Integer> FIRST_SLOT = List.of(1);
    public static final List<Integer> ALL_SLOTS = List.of(1, 2, 3, 4);

    public static final Map<String, List<Integer>> DOMAIN =
            Map.of(TURING, FIRST_SLOT,
                   LOVELACE, ALL_SLOTS,
                   BOHR, ALL_SLOTS,
                   CURIE, ALL_SLOTS,
                   SOCRATES, ALL_SLOTS,
                   PYTHAGORAS, ALL_SLOTS,
                   NEWTON, ALL_SLOTS
                  );

    public static final List<String> PHYSICISTS = List.of(BOHR, CURIE, NEWTON);
    public static final List<String> MATHEMATICIANS = List.of(LOVELACE, PYTHAGORAS, NEWTON);
    public static final List<String> ANCIENT_GREEKS = List.of(SOCRATES, PYTHAGORAS);
    public static final List<String> FEMALE = List.of(LOVELACE, CURIE);
    public static final List<String> ENGLISH = List.of(TURING, LOVELACE, NEWTON);
    public static final List<String> MY_FAV_SPEAKERS = List.of(CURIE, PYTHAGORAS);

    public static void main(String[] args) {

        List<Constraint<String, Integer>> constraints = ListFun.appendAll(Constraint.allDifferent(PHYSICISTS),
                                                                          Constraint.allDifferent(MATHEMATICIANS),
                                                                          Constraint.allDifferent(ANCIENT_GREEKS),
                                                                          Constraint.allDifferent(FEMALE),
                                                                          Constraint.allDifferent(ENGLISH),
                                                                          Constraint.allDifferent(MY_FAV_SPEAKERS)
                                                                         );

        var sol = new FindFirst<>(DOMAIN.keySet(),
                                  DOMAIN,
                                  constraints).findFirst();

        System.out.println(sol);

    }


}
