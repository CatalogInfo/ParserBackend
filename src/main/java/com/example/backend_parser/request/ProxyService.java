package com.example.backend_parser.request;


import com.example.backend_parser.models.ProxyWithApiToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class ProxyService {
    public static String requestWithProxy(ProxyWithApiToken proxyWithApiToken, String authorizationToken, String src, String dst, String amount) {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "false");


        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(proxyWithApiToken.getUsername(), proxyWithApiToken.getPassword().toCharArray());
            }
        });

        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                InetSocketAddress proxyAddress = new InetSocketAddress(proxyWithApiToken.getHost(), proxyWithApiToken.getPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
                return List.of(proxy);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                System.out.println("Proxy connection failed");
            }
        };

        ProxySelector.setDefault(proxySelector);


        return RequestMaker.inchQuoteRequest(authorizationToken, src, dst, amount);
    }
}
