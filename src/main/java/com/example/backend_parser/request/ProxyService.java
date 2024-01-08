package com.example.backend_parser.request;

import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class ProxyService {
    public static String requestWithProxy(String user, String password, String host, int port) {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "false");

        String targetUrl = "https://api4.binance.com/api/v3/exchangeInfo";

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password.toCharArray());
            }
        });

        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                InetSocketAddress proxyAddress = new InetSocketAddress(host, port);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
                return List.of(proxy);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                System.out.println("Proxy connection failed");
            }
        };

        ProxySelector.setDefault(proxySelector);

        String line = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(targetUrl).openStream()))) {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
