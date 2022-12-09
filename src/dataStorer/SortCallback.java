package dataStorer;

public interface SortCallback<T> {
    public boolean call(T leftValue, T rightValue);
}