package advent_of_code;

import java.nio.file.Path;

public final class InputDir {

    public static final Path PATH = Path.of(InputDir.class.getProtectionDomain()
                                                          .getCodeSource()
                                                          .getLocation()
                                                          .getPath())
                                        .getParent()
                                        .toAbsolutePath();

    public static String getPath(String year, int day) {
        return "%s/src/main/java/advent_of_code/_%s/day%s/input.txt".formatted(InputDir.PATH,
                                                                               year,
                                                                               day);
    }

    public static String getTestPath(String year, int day) {
        return "%s/src/main/java/advent_of_code/_%s/day%s/input_test.txt".formatted(InputDir.PATH,
                                                                                    year,
                                                                                    day);
    }
}
