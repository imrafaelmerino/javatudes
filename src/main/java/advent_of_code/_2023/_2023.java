package advent_of_code._2023;

import advent_of_code.Calendar;
import advent_of_code.Puzzle;
import advent_of_code._2023.day1.Trebuchet;

import java.util.List;

public class _2023 implements Calendar {
    @Override
    public List<Puzzle> getPuzzles() {
        return List.of(new Trebuchet());
    }

    @Override
    public String year() {
        return "2023";
    }


    public static void main(String[] args) throws Exception {
        Calendar.mainProgram(new _2023(), args);
    }
}
