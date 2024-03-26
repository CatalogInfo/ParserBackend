package com.example.backend_parser.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncRequestWithDelay {

    public static List<HttpResponse<String>> pollingGetRequest(List<String> urls, int delay, String bearer, Map<String, String> headers) {

        Map<String, CompletableFuture<HttpResponse<String>>> futuresMap = new HashMap<>();

        for (String url : urls) {
            CompletableFuture<HttpResponse<String>> future = null;
            try {
                future = HttpClientMaker.getAsync(url, bearer, headers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            futuresMap.put(url, future);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Collect responses and maintain the order
        List<HttpResponse<String>> responses = new ArrayList<>();
        for (String url : urls) {
            CompletableFuture<HttpResponse<String>> future = futuresMap.get(url);
            try {
                HttpResponse<String> response = future.get();
                responses.add(response);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return responses;
    }

}