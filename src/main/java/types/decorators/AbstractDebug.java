package types.decorators;

class AbstractDebug {

    public Stats stats = new Stats();

    protected void updateStats(long start) {
        stats.calls += 1;
        var elapsed = System.nanoTime() - start;
        if (elapsed < stats.minTime) stats.minTime = elapsed;
        if (elapsed > stats.maxTime) stats.maxTime = elapsed;
        stats.accTime += elapsed;
        stats.avgTime = stats.accTime / stats.calls;
    }

    public String getTimeStats() {
        return "avg: %s | acc: %s | min: %s | max: %s | times: %s".formatted(stats.getAvgTime(),
                                                                             stats.getAccTime(),
                                                                             stats.getMinTime(),
                                                                             stats.getMaxTime(),
                                                                             stats.calls);
    }


}
