package com.e_young.plugin.httplibr.http;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.simple.Converter;
import com.yanzhenjie.kalle.simple.SimpleResponse;

import java.lang.reflect.Type;

/**
 * 数据过滤器，起到过滤数据的作用
 *
 * @author guochen
 * @see 2018.9.21
 */
public class JsonConverter implements Converter {
    private Context context;

    public JsonConverter(Context context) {
        this.context = context;
    }

    @Override
    public <S, F> SimpleResponse<S, F> convert(Type succeed, Type failed, Response response, boolean fromCache) throws Exception {
        S succeedData = null;
        F failedData = null;

        int code = response.code();
        String serverJson = response.body().string();

        if (code >= 200 && code < 300) {

            try {
                JsonObject jsonObject = new JsonParser().parse(serverJson).getAsJsonObject();
                String status = jsonObject.get("status").getAsString();
                String message = jsonObject.get("message").getAsString();

                if (status != null && "1".equals(status)) {
                    try {
                        succeedData = new Gson().fromJson(serverJson, succeed);
                    } catch (Exception e) {
                        failedData = (F) "服务器数据格式错误";
                    }
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                failedData = (F) "Gson服务错误";
            }
        } else if (code >= 400 && code < 500) {
            failedData = (F) "发生未知错误";
        } else if (code >= 500) {
            failedData = (F) "服务器发生未知错误";
        }
        return SimpleResponse.<S, F>newBuilder()
                .code(response.code())
                .headers(response.headers())
                .fromCache(fromCache)
                .succeed(succeedData)
                .failed(failedData)
                .build();
    }


}
