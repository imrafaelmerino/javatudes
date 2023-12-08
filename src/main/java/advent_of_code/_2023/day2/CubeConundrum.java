package advent_of_code._2023.day2;


import advent_of_code.Puzzle;
import advent_of_code._2023._2023_Puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CubeConundrum implements _2023_Puzzle {
    static Map<String, Integer> BAG =
            Map.of("blue", 14,
                   "green", 13,
                   "red", 12);


    private static Map<String, Integer> getBagWithFewest(Game game) {
        return Map.of("green", maxNumberCubesPerGame(game, "green"),
                      "blue", maxNumberCubesPerGame(game, "blue"),
                      "red", maxNumberCubesPerGame(game, "red"));
    }

    private static int maxNumberCubesPerGame(Game game, String color) {
        return game.rounds()
                   .stream()
                   .flatMap(round -> round.cubes().stream())
                   .filter(cube -> cube.color().equals(color))
                   .map(Cube::count)
                   .max(Integer::compareTo)
                   .get();
    }

    private static ArrayList<Game> parse(List<String> lines) {
        var games = new ArrayList<Game>();
        for (String line : lines) {
            var id = Integer.parseInt(line.split(":")[0].split(" ")[1]);
            var rounds = new ArrayList<Round>();
            var roundsPerLine = line.split(":")[1].split(";");
            for (String str_round : roundsPerLine) {
                var round = new Round(getListOfCubesPerRound(str_round));
                rounds.add(round);
            }
            games.add(new Game(id, rounds));

        }
        return games;
    }

    private static boolean isValid(Game game) {
        return game.rounds
                .stream()
                .flatMap(round -> round.cubes().stream())
                .noneMatch(cube -> BAG.get(cube.color) < cube.count);

    }

    private static List<Cube> getListOfCubesPerRound(String round) {
        var cubes = new ArrayList<Cube>();
        if (round.contains(",")) {
            var pairs = round.trim().split(",");
            for (String pair : pairs) {
                var nc = pair.trim().split(" ");
                cubes.add(new Cube(nc[1], Integer.parseInt(nc[0])));
            }
        } else {
            var nc = round.trim().split(" ");
            cubes.add(new Cube(nc[1], Integer.parseInt(nc[0])));
        }
        return cubes;
    }

    @Override
    public Object solveFirst() throws Exception {
        var lines = Files.readAllLines(Path.of(getInputPath()));
        var games = parse(lines);
        return
                games.stream()
                     .filter(CubeConundrum::isValid)
                     .map(Game::id)
                     .reduce(0,Integer::sum);

    }

    @Override
    public Object solveSecond() throws Exception {
        var lines = Files.readAllLines(Path.of(getInputPath()));
        var games = parse(lines);
        return games.stream()
                    .map(CubeConundrum::getBagWithFewest)
                    .map(bag -> bag.get("green") * bag.get("blue") * bag.get("red"))
                    .reduce(0,Integer::sum);


    }

    @Override
    public String name() {
        return "Cube Conundrum";
    }

    @Override
    public int day() {
        return 2;
    }

    @Override
    public String outputUnitsPart1() {
        return "sum of IDs";
    }

    @Override
    public String outputUnitsPart2() {
        return "cubes";
    }


    public record Game(int id, List<Round> rounds) {
    }

    public record Round(List<Cube> cubes) {
    }

    public record Cube(String color, int count) {
    }
}
