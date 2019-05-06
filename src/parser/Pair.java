package parser;

class Pair {
    int first;
    int second;

    public Pair(int f, int s) {
        first = f;
        second = s;
    }

    public int getKey() {
        return first;
    }

    public int getValue() {
        return second;
    }
}
