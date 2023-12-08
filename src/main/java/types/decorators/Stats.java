package types.decorators;

public class Stats {
    public long calls;
    public long maxTime=-1;
    public long minTime=Long.MAX_VALUE;
    public long avgTime;
    public long accTime;

    public long getCalls() {
        return calls;
    }

    public String getMaxTime() {
        return Timing.formatTime(maxTime);
    }

    public String getMinTime() {
        return Timing.formatTime(minTime);
    }

    public String getAvgTime() {
        return Timing.formatTime(avgTime);
    }

    public String getAccTime() {
        return Timing.formatTime(accTime);
    }


}
