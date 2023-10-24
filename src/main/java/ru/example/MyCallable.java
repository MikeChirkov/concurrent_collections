package ru.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import static ru.example.Main.LENGTH_OF_WORDS;

public class MyCallable implements Callable<NameValue> {
    private final BlockingQueue<String> symbols;
    private final char letter;
    private int maxCount;
    private String maxString;

    public MyCallable(BlockingQueue<String> symbols, char letter) {
        this.symbols = symbols;
        this.letter = letter;
    }

    private static int getCountByLetter(String text, char let) {
        return (int) text.chars().filter(c -> c == let).count();
    }

    @Override
    public NameValue call() {
        try {
            for (int i = 0; i < LENGTH_OF_WORDS; i++) {
                String tmp = symbols.take();
                int count = getCountByLetter(tmp, letter);
                if (count > maxCount) {
                    maxCount = count;
                    maxString = tmp;
                }
            }
        } catch (InterruptedException ignored) {
        }

        return new NameValue(maxString, maxCount);
    }

}
