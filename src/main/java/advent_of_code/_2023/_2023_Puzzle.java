package advent_of_code._2023;

import advent_of_code.InputDir;
import advent_of_code.Puzzle;

public interface _2023_Puzzle extends Puzzle {


    default String getInputPath() {

        return InputDir.getPath("2023", day());
    }

    default String getTestInputPath() {

        return InputDir.getTestPath("2023", day());
    }
}
