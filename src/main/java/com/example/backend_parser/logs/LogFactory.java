package com.example.backend_parser.logs;

public class LogFactory {
    public static void makeALog(String message) {
        String time = TimeUtils.getTime();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
        System.out.println("-------NormalLog::" + time + "  ::  " + message + " -> " + caller.getFileName());
    }


}
