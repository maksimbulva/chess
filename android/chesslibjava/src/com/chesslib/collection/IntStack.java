package com.chesslib.collection;

public class IntStack {
    private static final int DEFAULT_CAPACITY = 32;

    private int[] values = new int[DEFAULT_CAPACITY];
    private int size = 0;

    public void push(final int value) {
        if (size == values.length) {
            int[] newValues = new int[size * 2];
            System.arraycopy(values, 0, newValues, 0, size);
            values = newValues;
        }
        values[size] = value;
        ++size;
    }

    public int pop() {
        --size;
        return values[size];
    }
}
