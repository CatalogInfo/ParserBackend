package com.example.backend_parser.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
    public static JSONObject getJSONObject(String data) {
        return new JSONObject(data);
    }

    public static JSONObject getJSONObject(Object data) {
        return new JSONObject(String.valueOf(data));
    }

    public static JSONObject getJSONObject(JSONObject object, String key) {
        return new JSONObject(String.valueOf(object.get(key)));
    }

    public static JSONArray getJSONArray(JSONObject object, String key) {
        return new JSONArray(String.valueOf(object.get(key)));
    }

    public static JSONArray getJSONArray(String data) {
        return new JSONArray(data);
    }

    public static String getValue(JSONObject obj, String key) {
        return String.valueOf(obj.get(key));
    }

}
