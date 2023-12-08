package types.decorators;


import java.util.function.Supplier;

public class Timing<T> extends AbstractDebug implements Supplier<T> {

    final Supplier<T> program;

    final int times;

    public Timing(Supplier<T> program,
                  int times
                 ) {
        this.program = program;
        this.times = times;
    }

    @Override
    public T get() {

        T output = null;
        for (int i = 0; i < times; i++) {
            var start = System.nanoTime();
            output = program.get();
            updateStats(start);
        }
        return output;

    }

    /**
     * Formats a given time duration in nanoseconds into a human-readable string.
     *
     * @param time The time in nanoseconds.
     * @return A formatted string representing the time duration.
     */
    public static String formatTime(long time) {
        if (time < 0) throw new IllegalArgumentException("time < 0");
        if (time >= 1000_000_000) return "%.3f sg".formatted(time / 1000_000_000d);
        if (time >= 1000_000) return "%.3f ms".formatted(time / 1000_000d);
        if (time >= 1000) return "%.3f µs".formatted(time / 1000d);
        return "%d ns".formatted(time);
    }


}
