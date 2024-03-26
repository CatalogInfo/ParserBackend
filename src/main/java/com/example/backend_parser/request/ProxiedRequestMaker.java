package com.example.backend_parser.request;

import com.example.backend_parser.http.HttpClientMaker;
import com.example.backend_parser.models.ProxyWithApiToken;
import com.example.backend_parser.proxy.ProxyService;

import java.io.IOException;

public class ProxiedRequestMaker {
    public static String makeRequestWithProxy(String url, ProxyWithApiToken proxyWithApiToken) {
        ProxyService.applyProxy(proxyWithApiToken);
        try {
            return HttpClientMaker.get(url, proxyWithApiToken.getApiToken(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
