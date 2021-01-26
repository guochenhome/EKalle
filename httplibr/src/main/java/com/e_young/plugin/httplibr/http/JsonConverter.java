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

                /*
                 * 主逻辑处理
                 */
                if (status == null || status.isEmpty()) {
                    lister.abNormal("0", "服务器数据错误", "服务器数据错误");
                } else if ("1".equals(status)) {
                    try {
                        succeedData = new Gson().fromJson(serverJson, succeed);
                        lister.normal();
                    } catch (Exception e) {
                        lister.abNormal("-1", "服务器数据格式错误", "服务器数据格式错误");
                        failedData = (F) "服务器数据格式错误";
                    }
                } else {
                    String data = "";
                    try {
                        data = jsonObject.get("data").getAsString();
                    } catch (Exception e) {
                        data = "DATA-发成错误";
                    }
                    lister.abNormal(status, message, data);
                    failedData = (F) message;
                }

            } catch (Exception e) {
                /*
                 *  辅助逻辑 处理未被规定格式的 先用于拉取域名的方法
                 * 但是可以应对任何的 json 格式的json数据
                 */
                try {
                    succeedData = new Gson().fromJson(serverJson, succeed);
                    lister.normal();
                }catch (Exception ce){
                    lister.abNormal("-2", "Gson服务错误", "Gson服务错误");
                    failedData = (F) "Gson服务错误";
                }
            }
        } else if (code >= 400 && code < 500) {
            lister.abNormal("-3", "发生未知错误", "发生未知错误");
            failedData = (F) "发生未知错误";
        } else if (code >= 500) {
            lister.abNormal("-4", "服务器发生未知错误", "服务器发生未知错误");
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

//        //登录过期
//        void outLogin();
//
//        //拦截签约 该用户有未完成的签约，需要进行签约
//        // retroactive 是否是补签   true 补签  false 非补签
//        void notSingin(boolean retroactive);
//
//        //未实名认证,请进行实名认证
//        void authentication();
//
//        //实名认证未通过,请重新认证
//        void authenticationFail();
//
//        //"用户没有入驻的项目,请入驻后登录~
//        void projectNoIn();
//
//        //个体信息未完善
//        void individualInfo();
//
//        //跳转个体完善页面
//        void individualDtl();
//
//        //风控
//        void riskcontrol(String message, String data);
//
//        //开通钱包
//        void openWallet();

        //非正确返回  abnormal
        void abNormal(String status, String message, String data);

        //正常返回
        void normal();

    }


}
