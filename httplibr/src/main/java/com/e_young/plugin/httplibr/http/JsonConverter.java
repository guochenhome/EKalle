package com.e_young.plugin.httplibr.http;

import android.content.Context;
import android.util.Log;
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

    private OnJsonConverterLister lister;

    public JsonConverter(Context context, OnJsonConverterLister lister) {
        this.context = context;
        this.lister = lister;
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

                //判断授权,如果授权失败直接忽略以下的操作
                /*
                NO_AUTHENTICATION("-998", "未实名认证,请进行实名认证"),

                NO_AUTHENTICATIONFAIL("-997", "实名认证未通过,请重新认证"),

                NO_SIGNING("-900", "该用户有未完成的签约，需要进行签约"),

                NO_LOGIN("-999", "未登录"),

                SUCCESS("1", "操作成功"),

                ERROR("0", "操作失败"),

                ERROR_SYSTEM("0", "服务器忙,请稍后重试~"),

                ERROR_PARAM_DEFICIENCY("0", "操作失败，参数缺失");
                 */
                if (status != null && "-999".equals(status)) {
                    lister.outLogin();
                } else if (status != null && "-900".equals(status)) {
                    lister.notSingin();
                } else if (status != null && "-998".equals(status)) {
                    lister.authentication();
                } else if (status != null && "-997".equals(status)) {
                    lister.authenticationFail();
                } else if (status != null && "1".equals(status)) {
                    try {
                        succeedData = new Gson().fromJson(serverJson, succeed);
                    } catch (Exception e) {
                        Log.e("erre", e.toString());
                        failedData = (F) "服务器数据格式错误";
                    }
                } else {
                    failedData = (F) message;
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


    public interface OnJsonConverterLister {

        //登录过期
        void outLogin();

        //拦截签约 该用户有未完成的签约，需要进行签约
        void notSingin();

        //未实名认证,请进行实名认证
        void authentication();

        //实名认证未通过,请重新认证
        void authenticationFail();
    }


}
