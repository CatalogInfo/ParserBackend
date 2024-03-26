package com.example.backend_parser.parser;

import com.example.backend_parser.api.dtos.OptionsDto;
import com.example.backend_parser.api.table_entities.BanToken;
import com.example.backend_parser.http.HttpClientMaker;
import com.google.gson.Gson;
import io.jsonwebtoken.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiCommunicator {
    public ApiCommunicator() {
//        login();
    }

    public String login() {
        String authResponse = "";
        try {
            authResponse = HttpClientMaker.post("http://localhost:8080/api/v1/auth/login", "{\"username\":\"root\",\"password\":\"password\"}", null , null);
        } catch (IOException | java.io.IOException | InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject(authResponse);
        return obj.getString("token");
    }

    public List<BanToken> getBanList(String exchangeName) {
        List<BanToken> tokens = new ArrayList<>();

        String response = null;
        try {
            response = HttpClientMaker.get("http://localhost:8080/api/v1/exchange/banList?exchange=" + exchangeName, login(), null);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JSONArray jsonArray = new JSONArray(response);
        for(int i = 0; i < jsonArray.length(); i ++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            BanToken banToken = new BanToken(obj.getLong("id"), obj.getString("exchange"), obj.getString("token"));
            tokens.add(banToken);
        }

        return tokens;
    }

    public void sendDataToClient() {
        try {
            HttpClientMaker.get("http://localhost:8080/api/v1/exchange/socket/exchanges", login(), null);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpClientMaker.get("http://localhost:8080/api/v1/general/parsingTime", login(), null);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public OptionsDto getOptions() {
        Gson gson = new Gson();
        try {
            return gson.fromJson(HttpClientMaker.get("http://localhost:8080/api/v1/general/options", login(), null), OptionsDto.class);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void banToken(String token, String exchange) {
        try {
            HttpClientMaker.get("http://localhost:8080/api/v1/exchange/ban?token=" + token + "&exchange=" + exchange, login(), null);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void unbanToken(String token, String exchange) {
        try {
            HttpClientMaker.get("http://localhost:8080/api/v1/unban?token=" + token + "&exchange=" + exchange, login(), null);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
