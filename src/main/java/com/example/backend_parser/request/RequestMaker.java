package com.example.backend_parser.request;

import com.example.backend_parser.utils.RestartUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class RequestMaker {
    public static String makeAuthRequest(String url) {
        String authResponse = postRequest("http://localhost:8080/api/v1/auth/login", "{\"username\":\"root\",\"password\":\"password\"}");
        JSONObject obj = new JSONObject(authResponse);
        return getRequestWithAuth(url, obj.getString("token"));
    }

    public static String getRequest(String url) {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        InputStream inputStream = null;
        try {
            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .setConnectionTimeToLive(1000, TimeUnit.MILLISECONDS)
                    .evictExpiredConnections()
                    .disableAutomaticRetries()
                    .build();            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            isRequestWorked(httpResponse.getStatusLine().getStatusCode(), url);
            
            HttpEntity entity = httpResponse.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            RestartUtils.restartApp();
            e.printStackTrace();
        }
        return RequestUtils.readFromConnection(inputStream);
    }

    public static String getRequestWithAuth(String url, String authToken) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .setConnectionTimeToLive(1000, TimeUnit.MILLISECONDS)
                    .evictExpiredConnections()
                    .disableAutomaticRetries()
                    .build();            HttpGet httpGet = new HttpGet(url);
            // Adding Authorization header
            httpGet.addHeader("Authorization", "Bearer " + authToken);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            isRequestWorked(httpResponse.getStatusLine().getStatusCode(), url);

            HttpEntity entity = httpResponse.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = RequestUtils.readFromConnection(inputStream);
        return response;
    }


    public static String inchQuoteRequest(String authorizationToken, String src, String dst, String amount, String walletAddress) {
        String link = "https://api.1inch.dev/fusion/quoter/v1.0/1/quote/receive?fromTokenAddress=" + src + "&toTokenAddress=" + dst + "&amount=" + amount + "&walletAddress=" + walletAddress + "&enableEstimate=true&isLedgerLive=true";

        InputStream inputStream = null;
        try {
            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .setConnectionTimeToLive(1000, TimeUnit.MILLISECONDS)
                    .evictExpiredConnections()
                    .disableAutomaticRetries()
                    .build();

            HttpGet httpGet = new HttpGet(link);

            // Adding Authorization header
            httpGet.addHeader("Authorization", "Bearer " + authorizationToken);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            try {
                isRequestWorked(httpResponse.getStatusLine().getStatusCode(), link);
            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpEntity entity = httpResponse.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {

            e.printStackTrace();
        }

        String response = RequestUtils.readFromConnection(inputStream);
        return response;
    }


    public static String postRequest(String url, String body) {
        InputStream inputStream = null;
        try {
            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .setConnectionTimeToLive(1000, TimeUnit.MILLISECONDS)
                    .evictExpiredConnections()
                    .disableAutomaticRetries()
                    .build();            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity body_json = new StringEntity(body);
            httppost.setEntity(body_json);

            HttpResponse httpResponse = httpClient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();

            inputStream = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RequestUtils.readFromConnection(inputStream);
    }

    private static void isRequestWorked(int responseCode, String url) throws Exception {

        if(responseCode == 429){
            System.out.println("LIMIT REACHED " + url);
            System.exit(1);
            throw new Exception("SOSI HUI");
        }
    }
}
