package advent_of_code._2023.day19;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;
import types.ListFun;
import types.LongRange;
import types.MapFun;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aplenty implements _2023_Puzzle {
    //{x=787,m=2655,a=1222,s=2876}
    //px{a<2006:qkq,m>2090:A,rfg}
    static final Pattern WORKFLOW_REGEX =
            Pattern.compile("(?<label>\\w+)\\{(?<rules>([axms][<>=]\\d+:\\w+,)+)(?<output>\\w+)}");
    static final Pattern RULE_REGEX =
            Pattern.compile("((?<category>[axms])(?<symbol>[<>=])(?<number>\\d+):(?<output>\\w+))");

    static final Pattern RATING_REGEX =
            Pattern.compile("(?<category>[axsm])=(?<rating>\\d+)");
    static final Pattern RATINGS_REGEX =
            Pattern.compile("\\{(?<ratings>((?<category>[axsm])=(?<rating>\\d+),?)+)}");

    private static List<Rule> getAllRulesByCat(Map<String, Workflow> workflowMap, Category a) {
        return workflowMap.entrySet().stream()
                          .flatMap(it -> it.getValue().rules.stream()).filter(it -> it.category == a).toList();
    }

    @Override
    public Object solveFirst() throws Exception {
        String input = FileParsers.read(getInputPath());
        var groups = input.split("\n\n");
        Map<String, Workflow> workflowMap = initWorkloadMap(groups);

        var ratingsStr = groups[1];

        var partRatings = Arrays.stream(ratingsStr.split("\n"))
                                .map(this::toListOfPartRatings)
                                .toList();

        var result = applyWorkflows(workflowMap, partRatings, "in", new ArrayList<>());

        return result.stream().flatMap(part -> part.categoryRatings.values().stream()).reduce(0L, Long::sum);
    }

    private Map<String, Workflow> initWorkloadMap(String[] groups) {
        Map<String, Workflow> workflowsMap = new HashMap<>();
        var workflowsStr = groups[0];
        for (var workflow : workflowsStr.split("\n")) {
            Matcher wm = WORKFLOW_REGEX.matcher(workflow.trim());
            if (wm.matches()) {
                var output = wm.group("output").trim();
                var label = wm.group("label").trim();
                var rules = wm.group("rules").trim();
                var workFlowRules = new ArrayList<Rule>();
                for (var rule : rules.split(",")) {
                    Matcher rm = RULE_REGEX.matcher(rule.trim());

                    if (rm.matches()) {
                        var cat = Category.valueOf(rm.group("category").trim());
                        var symbol = mapSymbol(rm.group("symbol").trim());
                        var number = Long.parseLong(rm.group("number").trim());
                        var ruleOutput = rm.group("output").trim();
                        workFlowRules.add(new Rule(symbol,
                                                   number,
                                                   cat,
                                                   toPredicate(symbol, number), ruleOutput));
                    } else throw new RuntimeException(rule);
                }
                workflowsMap.put(label, new Workflow(label, workFlowRules, output));
            } else throw new RuntimeException(workflowsStr);
        }

        return workflowsMap;
    }


    private List<PartRating> applyWorkflows(Map<String, Workflow> workflowMap,
                                            List<PartRating> remaining,
                                            String label,
                                            List<PartRating> result
                                           ) {
        if (remaining.isEmpty()) return result;
        PartRating head = remaining.getFirst();
        if (applyWorkflow(workflowMap, head, label) == Result.A) result.add(head);
        return applyWorkflows(workflowMap, ListFun.tail(remaining), label, result);
    }

    private Result applyWorkflow(Map<String, Workflow> workflowMap, PartRating part, String label) {
        Workflow workflow = workflowMap.get(label);
        for (var rule : workflow.rules) {
            var cat = rule.category;
            var catRating = part.categoryRatings.get(cat);
            if (rule.condition().test(catRating)) {
                if (rule.output.equals("A")) return Result.A;
                if (rule.output.equals("R")) return Result.R;
                return applyWorkflow(workflowMap, part, rule.output);
            }
        }
        if (workflow.defaultOutput.equals("A")) return Result.A;
        if (workflow.defaultOutput.equals("R")) return Result.R;
        return applyWorkflow(workflowMap, part, workflow.defaultOutput);

    }

    private Symbol mapSymbol(String symbol) {
        if (symbol.equals("<")) return Symbol.LOWER;
        if (symbol.equals(">")) return Symbol.GREATER;
        if (symbol.equals("=")) return Symbol.EQUAL;
        throw new IllegalArgumentException(symbol);
    }

    private PartRating toListOfPartRatings(String ratings) {
        var map = new HashMap<Category, Long>();
        var ms = RATINGS_REGEX.matcher(ratings);
        if (ms.matches()) {
            for (var rating : ms.group("ratings").split(",")) {
                var mr = RATING_REGEX.matcher(rating);
                if (mr.matches()) {
                    var cat = Category.valueOf(mr.group("category"));
                    var number = Long.parseLong(mr.group("rating"));
                    map.put(cat, number);
                }
            }
        }
        return new PartRating(map);
    }

    Predicate<Long> toPredicate(Symbol simbol, long number) {
        return switch (simbol) {
            case LOWER -> n -> n < number;
            case GREATER -> n -> n > number;
            case EQUAL -> n -> n == number;
        };
    }

    @Override
    public Object solveSecond() throws Exception {
        String input = FileParsers.read(getInputPath());
        var groups = input.split("\n\n");
        Map<String, Workflow> workflowMap = initWorkloadMap(groups);
        Map<Category, LongRange> catRanges = new HashMap<>();
        LongRange range = new LongRange(1, 4000);
        catRanges.put(Category.a, range);
        catRanges.put(Category.s, range);
        catRanges.put(Category.m, range);
        catRanges.put(Category.x, range);

        return sumAllCombinations(workflowMap, catRanges, "in");
    }

    long combinations(Map<Category, LongRange> catRanges) {
        return catRanges.values()
                        .stream()
                        .map(LongRange::length)
                        .reduce(1L,
                                (a, b) -> a * b
                               );
    }

    public long sumAllCombinations(Map<String, Workflow> workflowMap,
                                   Map<Category, LongRange> ranges,
                                   String label
                                  ) {

        if (label.equals("A")) return combinations(ranges);
        if (label.equals("R")) return 0;
        Workflow workflow = workflowMap.get(label);
        return sumAllCombinations(workflowMap, workflow.defaultOutput, ranges, workflow.rules);
    }

    public long sumAllCombinations(Map<String, Workflow> workflowMap,
                                   String defaultOutput,
                                   Map<Category, LongRange> ranges,
                                   List<Rule> rules
                                  ) {

        if (rules.isEmpty()) return sumAllCombinations(workflowMap, ranges, defaultOutput);
        var rule = rules.getFirst();
        var inputRange = ranges.get(rule.category);
        var ruleRange = rule.symbol.equals(Symbol.LOWER) ?
                new LongRange(1, rule.number - 1) :
                new LongRange(rule.number + 1, Long.MAX_VALUE);

        var inter = ruleRange.intersection(inputRange);
        if (inter == null) return sumAllCombinations(workflowMap, defaultOutput, ranges, ListFun.tail(rules));

        var difference = inputRange.difference(inter);
        return sumAllCombinations(workflowMap,
                                  MapFun.copyAndPut(ranges, rule.category, inter),
                                  rule.output) +
               difference
                       .stream()
                       .map(xs -> sumAllCombinations(workflowMap,
                                                     defaultOutput,
                                                     MapFun.copyAndPut(ranges, rule.category, xs),
                                                     ListFun.tail(rules))
                           )
                       .reduce(0L, Long::sum);


    }


    @Override
    public String name() {
        return "Aplenty";
    }

    @Override
    public int day() {
        return 19;
    }

    @Override
    public String outputUnitsPart1() {
        return "total rating of all the parts";
    }

    @Override
    public String outputUnitsPart2() {
        return "distinct combinations of ratings";
    }

    enum Result {A, R}

    enum Symbol {LOWER, GREATER, EQUAL}

    enum Category {x, m, s, a}

    record Workflow(String label, List<Rule> rules, String defaultOutput) {
    }

    record Rule(Symbol symbol, long number, Category category, Predicate<Long> condition, String output) {

    }

    record PartRating(Map<Category, Long> categoryRatings) {
    }
}
