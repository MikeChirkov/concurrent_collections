package ru.example;

public class NameValue {
    private final String name;
    private final int value;

    public NameValue(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
