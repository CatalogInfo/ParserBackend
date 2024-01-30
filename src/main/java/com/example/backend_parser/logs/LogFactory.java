package com.example.backend_parser.logs;

public class LogFactory {
    public static void makeALog(String message) {
        String time = TimeUtils.getTime();
        System.out.println("-------NormalLog::" + time + "  ::  " + message + " -> " + getClassName());
    }

    private static String getClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement caller = stackTrace[2];
        return caller.getFileName();
    }
}
