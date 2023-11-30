package types.decorators;

class AbstractDebug {

    long calls = 0;
    long maxTime = -1;
    long minTime = Long.MAX_VALUE;
    long avgTime = 0;
    long accTime = 0;

    protected void updateStats(long start) {
        calls += 1;
        var elapsed = System.nanoTime() - start;
        if (elapsed < minTime) minTime = elapsed;
        if (elapsed > maxTime) maxTime = elapsed;
        accTime += elapsed;
        avgTime = accTime / calls;
    }

    @Override
    public String toString() {
        return "Debug{" +
                "calls=" + calls +
                ", maxTime=" + maxTime +
                ", minTime=" + minTime +
                ", avgTime=" + avgTime +
                ", accTime=" + accTime +
                '}';
    }

    public void printStats(){
        System.out.println(this);
    }

}
