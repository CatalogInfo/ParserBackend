package com.example.backend_parser.request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class RequestMaker {
    public static String getRequest(String url) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpclient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RequestUtils.readFromConnection(inputStream);
    }

    public static String postRequest(String url, String body) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity body_json = new StringEntity(body);
            httppost.setEntity(body_json);

            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();

            inputStream = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RequestUtils.readFromConnection(inputStream);
    }
}
