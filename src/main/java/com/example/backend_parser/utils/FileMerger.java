package com.example.backend_parser.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileMerger {
    public static void merge() {
        String file1Path = "proxies.txt";
        String file2Path = "apiKeys.txt";
        String outputPath = "proxiesWithAPI.txt";

        mergeFiles(file1Path, file2Path, outputPath);
    }

    public static void mergeFiles(String file1Path, String file2Path, String outputPath) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
             BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));
             FileWriter writer = new FileWriter(outputPath)) {

            String line1, line2;
            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                writer.write(line1.trim() + ":" + line2 + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}