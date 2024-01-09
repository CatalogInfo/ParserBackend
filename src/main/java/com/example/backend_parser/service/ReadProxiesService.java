package com.example.backend_parser.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadProxiesService {
    public static void readProxies() {
        String filePath = "path.txt";
        List<String[]> allParts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(":");

                if (parts.length == 5) {
                    allParts.add(parts);
                } else {
                    System.err.println("Incorrect line: " + line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
