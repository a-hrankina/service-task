package com.ahrankina.task2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestService {
    private static final Map<String, AtomicInteger> map = new ConcurrentHashMap<>();
    private static final String[] names = {
            "Ann", "Olivia", "Jack", "Harry", "Archie",
            "Ann2", "Olivia2", "Jack2", "Harry2", "Archie2",
            "Ann3", "Olivia3", "Jack3", "Harry3", "Archie3",
    };

    private static void work() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 15; i++) {
            final String name = names[i];
            executorService.execute(() -> {
                for (int j = 0; j < 300_000_000; j++) {
                    AtomicInteger value = map.get(name);
                    if (value == null) {
                        value = new AtomicInteger(1);
                    } else {
                        value.incrementAndGet();
                    }

                    map.putIfAbsent(name, value);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println(map);
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        work();
        System.out.println(System.currentTimeMillis() - startTime);
    }

}
