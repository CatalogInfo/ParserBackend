package com.example.backend_parser.http;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HttpClientMaker {
    static HttpClient client = HttpClient.newHttpClient();

    public static String get(String url, String bearer, Map<String, String> headers) throws IOException, InterruptedException {
        if(headers == null) {
            headers = new HashMap<>();
        }
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));
        if(bearer != null) {
            headers.put("Authorization", "Bearer " + bearer);
        }

        setHeaders(requestBuilder, headers);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }

    public static CompletableFuture<HttpResponse<String>> getAsync(String url, String bearer, Map<String, String> headers) throws IOException, InterruptedException {
        if(headers == null) {
            headers = new HashMap<>();
        }
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));
        if(bearer != null) {
            headers.put("Authorization", "Bearer " + bearer);
        }

        setHeaders(requestBuilder, headers);

        HttpRequest request = requestBuilder.build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static String post(String url, String data, String bearer, Map<String, String> headers) throws IOException, InterruptedException {
        if(headers == null) {
            headers = new HashMap<>();
        }
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json; charset=UTF-8");
        if(bearer != null) {
            headers.put("Authorization", "Bearer " + bearer);
        }
        setHeaders(requestBuilder, headers);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response);

        return response.body();
    }

    private static void handleStatusCode(HttpResponse<?> response) {
        if(response == null) {
            return;
        }

        int statusCode = response.statusCode();

        switch(statusCode) {
            case 429:
                System.exit(1);
        }
    }

    private static void setHeaders(HttpRequest.Builder requestBuilder, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue());
        }
    }

}
