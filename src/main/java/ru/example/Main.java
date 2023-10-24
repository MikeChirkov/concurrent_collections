package ru.example;

import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static final int LENGTH_OF_WORDS = 10_000;
    private static final BlockingQueue<String> symbolsA = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> symbolsB = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> symbolsC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

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

        ExecutorService threadPool = Executors.newFixedThreadPool(25);

        Callable<NameValue> myCallableA = new MyCallable(symbolsA, 'a');
        Callable<NameValue> myCallableB = new MyCallable(symbolsB, 'b');
        Callable<NameValue> myCallableC = new MyCallable(symbolsC, 'c');
        Future<NameValue> newFutureA = threadPool.submit(myCallableA);
        Future<NameValue> newFutureB = threadPool.submit(myCallableB);
        Future<NameValue> newFutureC = threadPool.submit(myCallableC);

        fillThread.join();
        threadPool.shutdown();

        System.out.println(newFutureA.get().getName() + " " + newFutureA.get().getValue());
        System.out.println(newFutureB.get().getName() + " " + newFutureB.get().getValue());
        System.out.println(newFutureC.get().getName() + " " + newFutureC.get().getValue());
    }

    private static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}