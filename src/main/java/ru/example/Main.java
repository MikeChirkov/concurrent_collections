package ru.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int LENGTH_OF_WORDS = 10_000;
    private static final BlockingQueue<String> symbolsA = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> symbolsB = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> symbolsC = new ArrayBlockingQueue<>(100);
    private static String maxA = "";
    private static String maxB = "";
    private static String maxC = "";
    private static int maxCountA = 0;
    private static int maxCountB = 0;
    private static int maxCountC = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread fillThread = new Thread(() -> {
            for (int i = 0; i < LENGTH_OF_WORDS; i++) {
                String text = generateText("abc", 100_000);
                try {
                    symbolsA.put(text);
                    symbolsB.put(text);
                    symbolsC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        fillThread.start();

        Thread aCalculateThread = new Thread(() -> {
            try {
                for (int i = 0; i < LENGTH_OF_WORDS; i++) {
                    String tmp = symbolsA.take();
                    int count = getCountByLetter(tmp, 'a');
                    if (count > maxCountA) {
                        maxCountA = count;
                        maxA = tmp;
                    }
                }

            } catch (InterruptedException e) {
            }
        });
        aCalculateThread.start();

        Thread bCalculateThread = new Thread(() -> {
            try {
                for (int i = 0; i < LENGTH_OF_WORDS; i++) {
                    String tmp = symbolsB.take();
                    int count = getCountByLetter(tmp, 'b');
                    if (count > maxCountB) {
                        maxCountB = count;
                        maxB = tmp;
                    }
                }
            } catch (InterruptedException e) {
            }
        });
        bCalculateThread.start();

        Thread cCalculateThread = new Thread(() -> {
            try {
                for (int i = 0; i < LENGTH_OF_WORDS; i++) {
                    String tmp = symbolsC.take();
                    int count = getCountByLetter(tmp, 'a');
                    if (count > maxCountC) {
                        maxCountC = count;
                        maxC = tmp;
                    }
                }
            } catch (InterruptedException e) {
            }
        });
        cCalculateThread.start();

        fillThread.join();
        aCalculateThread.join();
        bCalculateThread.join();
        cCalculateThread.join();

        System.out.println(getCountByLetter(maxA, 'a'));
        System.out.println(getCountByLetter(maxB, 'b'));
        System.out.println(getCountByLetter(maxC, 'c'));
    }

    private static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static int getCountByLetter(String text, char let) {
        return (int) text.chars().filter(c -> c == let).count();
    }

}