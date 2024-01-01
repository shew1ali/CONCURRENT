package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final BlockingQueue<String> textsQueueOne = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> textsQueueTwo = new ArrayBlockingQueue<>(100);
    public static final BlockingQueue<String> textsQueueThree = new ArrayBlockingQueue<>(100);
    public static final int length = 100000;
    public static final int amountOfWords = 10000;


    public static void main(String[] args) {
        String[] words = new String[amountOfWords];

        Thread queue = new Thread(() -> {
            for (int i = 0; i < words.length; i++) {
                words[i] = generateText("abc", length);
                try {
                    textsQueueOne.put(words[i]);
                    textsQueueTwo.put(words[i]);
                    textsQueueThree.put(words[i]);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        queue.start();
        Thread queue1 = new Thread(() -> calculation('a', textsQueueOne));
        queue1.start();

        Thread queue2 = new Thread(() -> calculation('b', textsQueueTwo));
        queue2.start();

        Thread queue3 = new Thread(() -> calculation('c', textsQueueThree));
        queue3.start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void calculation(char letter, BlockingQueue<String> textsQueue) {
        int count = 0;
        int maxCount = 0;
        for (int i = 0; i < amountOfWords; i++) {
            try {
                String word = textsQueue.take();
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) == letter) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    maxCount = count;
                }
                count = 0;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("The maximum amount " + letter + " " + maxCount);
    }
}
