package advent_of_code;

public interface Puzzle {

    Object solveFirst() throws Exception;

    Object solveSecond() throws Exception;

    String name();

    int day();

    String getInputPath();

    String outputUnits();


}
