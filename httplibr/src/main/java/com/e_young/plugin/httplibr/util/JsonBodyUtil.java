package com.e_young.plugin.httplibr.util;

import com.google.gson.Gson;
import com.yanzhenjie.kalle.JsonBody;

import java.util.Map;

public class JsonBodyUtil {

    public static JsonBody BuilderString(Map<String, String> map) {

        String json = "";

        if (map != null) {
            try {
                Gson gson = new Gson();
                json = gson.toJson(map);
            } catch (Error error) {
                json = "";
            }
        }
        return new JsonBody(json);
    }

    public static JsonBody BuilderInt(Map<String, Integer> map) {
        String json = "";

        if (map != null) {
            try {
                Gson gson = new Gson();
                json = gson.toJson(map);
            } catch (Error error) {
                json = "";
            }
        }
        return new JsonBody(json);
    }

}
