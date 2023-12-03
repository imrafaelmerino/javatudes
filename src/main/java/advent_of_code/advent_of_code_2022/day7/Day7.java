package advent_of_code.advent_of_code_2022.day7;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Day7 {

    record File(String name, long size) {}

    record Dir(String name, Dir parent, List<File> files, List<Dir> dirs) {

        Dir cd(String name) {
            //always exists, listed and added with list command
            return dirs.stream().filter(it -> it.name.equals(name)).findFirst().get();
        }

        void addFile(File file) {
            files.add(file);
        }

        void addDir(Dir dir) {
            dirs.add(dir);
        }


        @Override
        public String toString() {
            return name;
        }

        public long size() {
            return files.stream().map(file -> file.size).reduce(0L, Long::sum)
                    + dirs.stream().map(Dir::size).reduce(0L, Long::sum);

        }
    }


    public static void main(String[] args) throws IOException {

        String input = "/Users/rmerino/Projects/javatudes/src/main/java/advent_of_code/advent_of_code_2022/day7/input.txt";

        var lines = Files.readAllLines(new java.io.File(input).toPath());

        var outermost = new Dir("/", null, new ArrayList<>(), new ArrayList<>());

        Dir current = null;
        boolean lsOutput = false;

        for (String line : lines) {
            if (line.equals("$ cd /")) {
                lsOutput = false;
                current = outermost;
            } else if (line.equals("$ cd ..")) {
                lsOutput = false;
                current = current.parent;
            } else if (line.startsWith("$ cd")) {
                lsOutput = false;
                String name = line.split("\s")[2].trim();
                current = current.cd(name);
            } else if (line.equals("$ ls")) {
                lsOutput = true;
            } else if (lsOutput) {
                if (line.startsWith("dir")) {
                    String name = line.split("\s")[1].trim();
                    var dir = new Dir(name, current, new ArrayList<>(), new ArrayList<>());
                    current.addDir(dir);
                } else {
                    var tokens = line.split("\s");
                    String name = tokens[1].trim();
                    int size = Integer.parseInt(tokens[0].trim());
                    File file = new File(name, size);
                    current.addFile(file);
                }
            }
        }

        HashMap<String, Long> sizes = new HashMap<>();
        sizes(outermost, sizes);

        System.out.println(sizes);

        System.out.println(sizes.entrySet().stream()
                                .map(Map.Entry::getValue)
                                .filter(it -> it <= 100000)
                                .reduce(0L, Long::sum)
                          );

        long total = sizes.get("/");
        long free = 70000000 - total;
        long needed = 30000000 - free;

        System.out.println(sizes.entrySet().stream()
                                .map(Map.Entry::getValue)
                                .filter(it -> it >= needed)
                                .min(Long::compare)
                                .get());


    }


    public static void sizes(Dir dir,
                             Map<String, Long> result
                            ) {
        //warning same dir name but different parent can ruin your solution, so include the parent in the key
        String key = dir.parent != null ?
                dir.parent + "/" + dir.name :
                dir.name;
        result.put(key,
                   dir.size()
                  );
        for (Dir d : dir.dirs) sizes(d, result);

    }
}

