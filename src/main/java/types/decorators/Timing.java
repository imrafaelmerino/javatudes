package types.decorators;


import java.util.function.Supplier;

public class Timing extends AbstractDebug implements Supplier<Void> {

    final Supplier<?> program;

    final int times;

    public Timing(Supplier<?> program,
                  int times
                 ) {
        this.program = program;
        this.times = times;
    }

    @Override
    public Void get() {

        for (int i = 0; i < times; i++) {
            var start = System.nanoTime();
            program.get();
            updateStats(start);
        }

        printStats();

        return null;


    }
}
