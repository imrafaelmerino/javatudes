package advent_of_code.advent_of_code_2022.day24;


import fun.tuple.Pair;
import search.BreathSearch;
import types.*;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day24_1 {


    public static final String UP = "^";
    public static final String DOWN = "v";
    public static final String LEFT = "<";
    public static final String EMPTY = ".";
    public static final String WALL = "#";

    public static void main(String[] args) {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day24/input.txt";
        var grid = PersistentGrid.fromFile(input);


        var startPos = grid.getBottomBorder()
                           .stream()
                           .filter(it -> it.value().equals(EMPTY))
                           .findFirst()
                           .get()
                           .pos();

        System.out.println("start pos: "+startPos);

        var endPos = grid.getTopBorder()
                         .stream()
                         .filter(it -> it.value().equals(EMPTY))
                         .findFirst()
                         .get()
                         .pos();

        var xrange = new Range(grid.xmin(),grid.xmax());
        var yrange = new Range(grid.ymin(),grid.ymax());

        Function<Pos,List<Pos>> neighbors = pos ->
                grid.getVHNeighbors(pos)
                    .stream()
                    .filter(it->!grid.getVal(it).equals(WALL))
                    .collect(Collectors.toList());

        System.out.println("goal pos: "+endPos);


        Function<Pair<Pos, Grid<List<String>>>,
                List<Pair<Pos, Grid<List<String>>>>> getSuccesors = state -> {

                    var nextGrid = nextGrid(state.second(),xrange,yrange);

                    var nextElfPos=
                           neighbors.apply(state.first())
                                    .stream()
                                    .filter(pos->nextGrid.getVal(pos)==null)
                                    .collect(Collectors.toList());

                    if(nextGrid.getVal(state.first()) == null)
                        nextElfPos.add(state.first());

                    var xs =
                            nextElfPos.stream()
                                      .map(pos -> Pair.of(pos, nextGrid))
                                      .collect(Collectors.toList());


                    return xs;
                };

       var bb = BreathSearch.fromStates(getSuccesors);

       var result = bb.findFirst(Pair.of(startPos,
                                          grid
                                              .remove((p,v)->v.equals(EMPTY) || v.equals(WALL))
                                              .mapValues((p,x)->ListFun.mutableOf(x))),
                                  m->m.first().equals(endPos)
                                );

        var result1 = bb.findFirst(Pair.of(endPos,
                                           result.last().state().second()
                                           ),
                                  m->m.first().equals(startPos)
                                 );

        var result2 = bb.findFirst(Pair.of(startPos,
                                           result1.last().state().second()
                                          ),
                                   m->m.first().equals(endPos)
                                  );

       System.out.println(result.size()-1+result1.size()-1+result2.size()-1);


    }


    static Map<Grid<List<String>>, Grid<List<String>>> cache = new HashMap<>();
    public static Grid<List<String>> nextGrid(Grid<List<String>> grid, Range xrange, Range yrange) {
        var xs = cache.get(grid);
        if(xs!=null) return xs;
        Grid<List<String>> ys = PersistentGrid.empty();
        for (var entry : grid)
            for (String arrow : entry.value())
                ys = moveArrow(entry.pos(), arrow, ys,xrange,yrange);
        cache.put(grid,ys);
        return ys;
    }

    public static Grid<List<String>> moveArrow(Pos source, String arrow, Grid<List<String>> ys,Range xrange,Range yrange) {
        Pos target = switch (arrow) {
            case UP ->
                (source.y() == yrange.min() + 1) ?
                        new Pos(source.x(), yrange.max() - 1) :
                        new Pos(source.x(), source.y() - 1);
            case DOWN ->
                (source.y() == yrange.max() - 1) ?
                        new Pos(source.x(), yrange.min() + 1) :
                        new Pos(source.x(), source.y() + 1);

            case LEFT ->  source.x() == xrange.min() + 1 ?
                        new Pos(xrange.max() - 1, source.y()) :
                        new Pos(source.x() - 1, source.y());

            default -> source.x() == xrange.max() - 1 ?
                        new Pos(xrange.min() + 1, source.y()) :
                        new Pos(source.x() + 1, source.y());

        };

        if(ys.getVal(target)==null) return ys.put(target,ListFun.mutableOf(arrow));
        else {
            List<String> val = ys.getVal(target);
            val.add(arrow);
            return  ys.put(target, val);
        }

    }

}
