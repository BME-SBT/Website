package hu.schdesign.solarboat.model;

public class DataPair<T, U> {
    private T name;
    private U value;

    public DataPair(T id, U data) {
        this.name = id;
        this.value = data;
    }

    public DataPair() {
    }

    public T getName() {
        return name;
    }

    public U getValue() {
        return value;
    }
}
