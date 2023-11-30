package types.decorators;

class AbstractMemo {

    protected long hits;

    protected long misses;


    @Override
    public String toString() {
        return "AbstractMemo{" +
                "hits=" + hits +
                ", misses=" + misses +
                '}';
    }

    public void printStats(){
        System.out.println(this);
    }

}
