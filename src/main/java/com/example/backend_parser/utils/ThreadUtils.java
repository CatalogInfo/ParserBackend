package com.example.backend_parser.utils;

import java.util.List;

public class ThreadUtils {
    public static void waitTillThreadsExecuted(List<Thread> threads) {
        for(int i = 0; i < threads.size(); i ++) {
            join(threads.get(i));
            sleepOnTime(100);
        }
    }

    public static boolean areThreadsAlive(List<Thread> threads) {
        for(Thread thread : threads) {
            if(thread.isAlive()) {
                return true;
            }
        }
        return false;
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
