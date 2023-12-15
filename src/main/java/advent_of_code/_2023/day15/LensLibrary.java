package advent_of_code._2023.day15;

import advent_of_code._2023._2023_Puzzle;
import types.FileParsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LensLibrary implements _2023_Puzzle {

    public static final Pattern LENS_REGEX = Pattern.compile("(?<label>\\w+)(?<op>[-=])(?<fl>\\d?)");

    private static int hash(String step) {
        var result = 0;
        for (char c : step.toCharArray()) result = ((result + c) * 17) % 256;
        return result;
    }

    @Override
    public Object solveFirst() throws Exception {
        var input = FileParsers.read(getInputPath());
        var steps = Arrays.stream(input.split(",")).toList();
        return steps.stream()
                    .map(LensLibrary::hash)
                    .reduce(0, Integer::sum);
    }

    public static void main(String[] args) throws Exception {
        new LensLibrary().solveSecond();
    }

    @Override
    public Object solveSecond() throws Exception {
        List<List<Lens>> boxes = new ArrayList<>();
        for (int i = 0; i <= 255; i++) boxes.add(new ArrayList<>());

        var input = FileParsers.read(getInputPath());
        var steps = Arrays.stream(input.split(",")).toList();
        for (String step : steps) {
            Matcher matcher = LENS_REGEX.matcher(step.trim());
            if (matcher.matches()) {
                var label = matcher.group("label");
                var nbox = hash(label);
                var op = matcher.group("op");
                System.out.println(label);
                System.out.println(op);
                List<Lens> box = boxes.get(nbox);
                if (op.equals("=")) {
                    var fl = Integer.parseInt(matcher.group("fl"));
                    var lens = new Lens(label, fl);
                    if (box.contains(lens)) box.set(box.indexOf(lens), lens);
                    else box.add(lens);
                } else box.removeIf(it -> it.name.equals(label));
            } else throw new RuntimeException();

        }

        var fp = 0;
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = 0; j < boxes.get(i).size(); j++) {
                var l = boxes.get(i).get(j);
                fp = fp + ((1 + i)*(j+1)*(l.fl));
            }

        }
        return fp;
    }

    @Override
    public String name() {
        return "Lens Library";
    }

    @Override
    public int day() {
        return 15;
    }

    @Override
    public String outputUnitsPart1() {
        return "sum of hashes";
    }

    @Override
    public String outputUnitsPart2() {
        return "focusing power";
    }

    record Lens(String name, int fl) {
        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Lens lens = (Lens) object;
            return Objects.equals(name, lens.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
