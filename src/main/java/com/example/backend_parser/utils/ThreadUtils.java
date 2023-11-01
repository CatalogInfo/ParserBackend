package com.example.backend_parser.utils;

import java.util.List;

public class ThreadUtils {
    public static void waitTillThreadsExecuted(List<Thread> threads) {
        for(int i = 0; i < threads.size(); i ++) {
            join(threads.get(i));
        }
    }

    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleepOnTime(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
