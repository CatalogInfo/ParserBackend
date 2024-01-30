package com.example.backend_parser.logs;

public class LogFactory {
    public static void makeALog(String message) {
        String time = TimeUtils.getTime();
        System.out.println("-------NormalLog::" + time + "  ::  " + message);
    }
}
