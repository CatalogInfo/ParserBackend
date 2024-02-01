package com.example.backend_parser.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    public static JSONObject getJSONObject(String data) {
        try {
            new JSONObject(data);
        } catch (JSONException e) {
            return new JSONObject();
        }
        return new JSONObject(data);
    }

    public static JSONObject getJSONObject(Object data) {
        try {
            new JSONObject(String.valueOf(data));
        } catch (JSONException e) {
            return new JSONObject();
        }
        return new JSONObject(String.valueOf(data));
    }

    public static JSONObject getJSONObject(JSONObject object, String key) {
        if (!object.has(key)) {
            return new JSONObject();
        }
        return new JSONObject(String.valueOf(object.get(key)));
    }

    public static JSONArray getJSONArray(JSONObject object, String key) {
        if (!object.has(key)) {
            return new JSONArray();
        }
        return new JSONArray(String.valueOf(object.get(key)));
    }

    public static JSONArray getJSONArray(String data) {
        try {
            new JSONArray(data);
        } catch (JSONException e) {
            return new JSONArray();
        }
        return new JSONArray(data);
    }

    public static String getValue(JSONObject obj, String key) {
        return String.valueOf(obj.get(key));
    }

}
