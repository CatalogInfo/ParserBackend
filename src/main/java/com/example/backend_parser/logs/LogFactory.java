package com.example.backend_parser.logs;

import java.io.*;

public class LogFactory {
    public static void makeALog(String message) {
        String time = TimeUtils.getTime();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
        System.out.println("-------NormalLog::" + time + "  ::  " + message + " -> " + caller.getFileName());
    }

    public static void makeAnExceptionLog(String message) {
        String time = TimeUtils.getTime();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
//        System.out.println("-------ExceptionLog::" + time + "  ::  " + message + " -> " + caller.getFileName());
//        writeToFile("-------ExceptionLog::" + time + "  ::  " + message + " -> " + caller.getFileName(), "logs.txt");

    }

    private static void writeToFile(String message, String filePath) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            String line1;
            while ((line1 = reader1.readLine()) != null) {
                writer.write(line1 + "\n" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
