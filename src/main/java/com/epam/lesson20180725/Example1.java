package com.epam.lesson20180725;

import lombok.Value;

import java.util.concurrent.*;

public class Example1 {

    @Value
    static class Box {

        String destination;
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        int numBoxes = 10;
        BlockingQueue<Box> desk = new ArrayBlockingQueue<>(numBoxes);

        Runnable operator = () -> {
            try {
                for (int i = 0; i < numBoxes; ++i) {
                    System.out.println("Prepare the box number - " + i);
                    TimeUnit.SECONDS.sleep(1);
                    Box box = new Box(String.valueOf(i));

                    System.out.println("Trying put " + i + " on the desk");
                    desk.put(box);
                    System.out.println("Successfully put " + i + " on the desk!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable courier = () -> {
            try {

                for (int i = 0; i < numBoxes; ++i) {
                    System.err.println("Trying to take box form the desk");
                    Box box = desk.take();
                    System.err.println("Took the " + box + ", going to deliver");
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        service.submit(operator);
        service.submit(courier);
        service.shutdown();
    }
}
