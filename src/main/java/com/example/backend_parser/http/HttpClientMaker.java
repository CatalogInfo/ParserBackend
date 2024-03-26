package com.example.backend_parser.http;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpClientMaker {
    static HttpClient client = HttpClient.newHttpClient();

    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }

    public static String get(String url, Map<String, String> headers) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));

        setHeaders(requestBuilder, headers);

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }


    public static String get(String url, String bearer) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + bearer)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }

    public static String post(String url, String data) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }

    public static String post(String url, String data, String bearer) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + bearer)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        handleStatusCode(response);

        return response.body();
    }

    public static String post(String url, String data, Map<String, String> headers) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json; charset=UTF-8");

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
