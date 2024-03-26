package com.example.backend_parser.utils;

import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.proxy.ProxyModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadProxiesUtils {
    public static List<ProxyWithApiToken> readProxies(String path) {
        List<ProxyWithApiToken> proxies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();

            while (line != null) {
                String[] parts = line.split(":");

                if (parts.length == 6) {
                    proxies.add(new ProxyWithApiToken(new ProxyModel(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]), parts[4], parts[5]));
                } else {
                    System.err.println("Incorrect line: " + line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return proxies;
    }
}
