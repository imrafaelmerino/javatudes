package advent_of_code._2023;

import advent_of_code.Calendar;
import advent_of_code.Puzzle;
import advent_of_code._2023.day1.Trebuchet;
import advent_of_code._2023.day10.PipeMaze;
import advent_of_code._2023.day11.CosmicExpansion;
import advent_of_code._2023.day12.HotSprings;
import advent_of_code._2023.day13.PointOfIncidence;
import advent_of_code._2023.day14.ParabolicReflectorDish;
import advent_of_code._2023.day15.LensLibrary;
import advent_of_code._2023.day16.TheFloorWillBeLava;
import advent_of_code._2023.day17.ClumsyCrucible;
import advent_of_code._2023.day18.LavaductLagoon;
import advent_of_code._2023.day19.Aplenty;
import advent_of_code._2023.day2.CubeConundrum;
import advent_of_code._2023.day20.PulsePropagation;
import advent_of_code._2023.day3.GearRatios;
import advent_of_code._2023.day4.ScratchCards;
import advent_of_code._2023.day5.Seeds;
import advent_of_code._2023.day6.WaitForIt;
import advent_of_code._2023.day7.CamelCard;
import advent_of_code._2023.day8.HauntedWasteland;
import advent_of_code._2023.day9.MirageMaintenance;

import java.util.List;

public class _2023 implements Calendar {
    public static void main(String[] args) throws Exception {
        Calendar.mainProgram(new _2023(), args);
    }

    @Override
    public List<Puzzle> getPuzzles() {
        return List.of(new Trebuchet(),
                       new CubeConundrum(),
                       new GearRatios(),
                       new ScratchCards(),
                       new Seeds(),
                       new WaitForIt(),
                       new CamelCard(),
                       new HauntedWasteland(),
                       new MirageMaintenance(),
                       new PipeMaze(),
                       new CosmicExpansion(),
                       new HotSprings(),
                       new PointOfIncidence(),
                       new ParabolicReflectorDish(),
                       new LensLibrary(),
                       new TheFloorWillBeLava(),
                       new ClumsyCrucible(),
                       new LavaductLagoon(),
                       new Aplenty(),
                       new PulsePropagation()
                      );
    }

    @Override
    public String year() {
        return "2023";
    }
}
