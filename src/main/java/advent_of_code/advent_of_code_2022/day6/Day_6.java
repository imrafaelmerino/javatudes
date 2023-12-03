package advent_of_code.advent_of_code_2022.day6;

import types.FileParsers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Day_6 {


    public static void main(String[] args) throws IOException {

        String file = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day6/input.txt";

        int  WINDOW_SIZE = 14;

        var characters = FileParsers.toListOfLines(file).get(0);

        //var characters = "bvwbjplbgvbhsrlpgdmjqwftvncz";

        String window = "";
        int counter  = 0;

        for(var c : characters.toCharArray()){
            counter +=1;
            if(window.length() == WINDOW_SIZE) window = window.substring(1) + c;
            else window += c;
            if(window.length() == WINDOW_SIZE && allDifferent(window)){
                System.out.println(counter);
                return;
            }
        }

    }

    private static boolean allDifferent(String window) {
        return Arrays.stream(window.split(""))
                     .collect(Collectors.toSet())
                     .size() == window.length();
    }
}
