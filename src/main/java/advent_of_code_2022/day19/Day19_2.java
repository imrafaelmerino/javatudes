package advent_of_code_2022.day19;

import types.FileParsers;
import types.MathFun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day19_2 {


    record ObsidianCost(int oreCost,
                        int clayCost
    ) {}

    record GeodeCost(int oreCost,
                     int obsidianCost
    ) {}

    record Blueprint(int n,
                     int oreRobotCost,
                     int clayRobotCost,
                     ObsidianCost obsidianRobotCost,
                     GeodeCost geodeRobotCost
    ) {}

    record State(int nOreRobots,
                 int nClayRobots,
                 int nObsidianRobots,
                 int nGeodeRobots,
                 int ore,
                 int clay,
                 int obsidian,
                 int geode,
                 int remaining
    ) {}


    public static void main(String[] args) {


        var input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code_2022/day19/input.txt";

        var blueprints = FileParsers.toListOfLines(input)
                                    .stream()
                                    .map(line -> {
                                        String[] sent = line.split("\\.");
                                        var n = Integer.parseInt(sent[0].trim().split("\s")[1].replace(":", ""));
                                        var oreRobotCost = Integer.parseInt(sent[0].trim().split("\s")[6]);
                                        var clayRobotCost = Integer.parseInt((sent[1].trim().split("\s")[4]));
                                        var obsidianOre = Integer.parseInt((sent[2].trim().split("\s")[4]));
                                        var obsidianClary = Integer.parseInt((sent[2].trim().split("\s")[7]));
                                        var geodeOre = Integer.parseInt((sent[3].trim().split("\s")[4]));
                                        var geodeObsidian = Integer.parseInt((sent[3].trim().split("\s")[7]));
                                        return new Blueprint(n,
                                                             oreRobotCost,
                                                             clayRobotCost,
                                                             new ObsidianCost(obsidianOre,
                                                                              obsidianClary
                                                             ),
                                                             new GeodeCost(geodeOre,
                                                                           geodeObsidian
                                                             )
                                        );
                                    })
                                    .toList();

        System.out.println(blueprints);

        var firstThree = blueprints.subList(0, 3);


        var quality = firstThree.stream()
                                .map(bp -> simulate(bp,
                                                    new State(1, 0, 0, 0, 0, 0, 0, 0, 32),
                                                    getMaxOreRobotsNeeded(bp)
                                                   ))
                                .reduce(1, (a, b) -> a * b);

        System.out.println(quality);


    }


    //having more bots is useless, collecting more than this number per minute doesn't make any difference,
    // the same for other robots but geocode, but we can get this figure directly from the blueprint

    public static int getMaxOreRobotsNeeded(Blueprint b) {
        return MathFun.max(b.oreRobotCost,
                           b.clayRobotCost,
                           b.obsidianRobotCost.oreCost,
                           b.geodeRobotCost.oreCost
                          );
    }

    public static int getMaxOreToSpentTilEnd(Blueprint b, int remaining) {
        return MathFun.max(b.oreRobotCost,
                           b.clayRobotCost,
                           b.obsidianRobotCost.oreCost,
                           b.geodeRobotCost.oreCost
                          ) * (remaining - 1);
    }

    public static int getMaxClayToSpentTilEnd(Blueprint b, int remaining) {
        return (remaining - 1) * b.clayRobotCost;
    }

    public static int getMaxObsidianToSpentTilEnd(Blueprint b, int remaining) {
        return (remaining - 1) * b.geodeRobotCost.obsidianCost;
    }

    public static int simulate(Blueprint blueprint, State start, int maxOreNeeded) {
        State last;
        State max = start;
        var stack = new ArrayList<State>();
        stack.add(start);
        var seen = new HashSet<>();
        while (!stack.isEmpty()) {
            last = stack.remove(0);
            if (!seen.contains(last)) {
                seen.add(last);
                if (last.geode >= max.geode && last.geode > 0) max = last;

                //System.out.println(last);
                if (last.remaining > 0) {
                    List<State> succ = succ(blueprint, last, maxOreNeeded);
                    // succ.forEach(System.out::println);
                    stack.addAll(0, succ);
                    // System.out.println("\n");
                }
            }
        }
        //succ: State[nOreRobots=1, nClayRobots=4, nObsidianRobots=2, nGeodeRobots=1, ore=3, clay=21, obsidian=5, geode=1, remaining=19]

        System.out.println(max);

        return max.geode;
    }


    public static List<State> succ(Blueprint bp, State state, int maxOreNeeded) {
        //System.out.println(state);

        //So basically, we can prune the search if
        // geodes collected + geode robots * time remaining + T(time remaining) <= best total geodes found so far.

        var states = new ArrayList<State>();


        if (state.obsidian >= bp.geodeRobotCost.obsidianCost
                && state.ore >= bp.geodeRobotCost.oreCost)

            states.add(new State(state.nOreRobots,
                                 state.nClayRobots,
                                 state.nObsidianRobots,
                                 state.nGeodeRobots + 1,
                                 state.ore - bp.geodeRobotCost.oreCost + state.nOreRobots,
                                 state.clay >= getMaxClayToSpentTilEnd(bp, state.remaining) ? state.clay : state.clay + state.nClayRobots,
                                 state.obsidian - bp.geodeRobotCost.obsidianCost + state.nObsidianRobots,
                                 state.geode + state.nGeodeRobots,
                                 state.remaining - 1
                       )
                      );

        else {

            if (state.clay >= bp.obsidianRobotCost.clayCost
                    && state.ore >= bp.obsidianRobotCost.oreCost
                    && state.nObsidianRobots < bp.geodeRobotCost.obsidianCost
            ) {
                states.add(new State(state.nOreRobots,
                                     state.nClayRobots,
                                     state.nObsidianRobots + 1,
                                     state.nGeodeRobots,
                                     state.ore - bp.obsidianRobotCost.oreCost + state.nOreRobots,
                                     state.clay - bp.obsidianRobotCost.clayCost + state.nClayRobots,
                                     state.obsidian >= getMaxObsidianToSpentTilEnd(bp, state.remaining) ? state.obsidian : state.obsidian + state.nObsidianRobots,
                                     state.geode + state.nGeodeRobots,
                                     state.remaining - 1
                ));
            }

            if (state.ore >= bp.clayRobotCost
                    && state.nClayRobots < bp.obsidianRobotCost.clayCost
            )
                states.add(new State(state.nOreRobots,
                                     state.nClayRobots + 1,
                                     state.nObsidianRobots,
                                     state.nGeodeRobots,
                                     state.ore - bp.clayRobotCost + state.nOreRobots,
                                     state.clay >= getMaxClayToSpentTilEnd(bp, state.remaining) ? state.clay : state.clay + state.nClayRobots,
                                     state.obsidian >= getMaxObsidianToSpentTilEnd(bp, state.remaining) ? state.obsidian : state.obsidian + state.nObsidianRobots,
                                     state.geode + state.nGeodeRobots,
                                     state.remaining - 1
                ));

            if (state.ore >= bp.oreRobotCost
                    && state.nOreRobots < maxOreNeeded
            )
                states.add(new State(state.nOreRobots + 1,
                                     state.nClayRobots,
                                     state.nObsidianRobots,
                                     state.nGeodeRobots,
                                     state.ore - bp.oreRobotCost + state.nOreRobots,
                                     state.clay >= getMaxClayToSpentTilEnd(bp, state.remaining) ? state.clay : state.clay + state.nClayRobots,
                                     state.obsidian >= getMaxObsidianToSpentTilEnd(bp, state.remaining) ? state.obsidian : state.obsidian + state.nObsidianRobots,
                                     state.geode + state.nGeodeRobots,
                                     state.remaining - 1
                ));


            states.add(new State(state.nOreRobots,
                                 state.nClayRobots,
                                 state.nObsidianRobots,
                                 state.nGeodeRobots,
                                 state.ore >= getMaxOreToSpentTilEnd(bp, state.remaining) ? state.ore : state.ore + state.nOreRobots,
                                 state.clay >= getMaxClayToSpentTilEnd(bp, state.remaining) ? state.clay : state.clay + state.nClayRobots,
                                 state.obsidian >= getMaxObsidianToSpentTilEnd(bp, state.remaining) ? state.obsidian : state.obsidian + state.nObsidianRobots,
                                 state.geode + state.nGeodeRobots,
                                 state.remaining - 1
            ));
        }

        return states;


    }
}
