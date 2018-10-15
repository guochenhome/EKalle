package com.e_young.plugin.httplibr.http;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.simple.Converter;
import com.yanzhenjie.kalle.simple.SimpleResponse;

import java.lang.reflect.Type;

/**
 * 数据过滤器，起到过滤数据的作用
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
            HttpEntity httpEntity;

            try {
                httpEntity = JSON.parseObject(serverJson, HttpEntity.class);
            } catch (Exception e) {
                httpEntity = new HttpEntity();
                httpEntity.setmMessage("服务器数据格式错误");
                httpEntity.setmData("");
                httpEntity.setmStatus("0");//设置 -1 为数据格式错误
                httpEntity.setSucceed(false);
            }

            if (httpEntity != null && httpEntity.isSucceed() && httpEntity.getmData() != null) {

                //判断服务器返回状态
                if(httpEntity.getmStatus()==null||!"1".equals(httpEntity.getmStatus())){
                    Toast.makeText(context,"数据请求失败",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        succeedData = new Gson().fromJson(httpEntity.getmData(), succeed);
                    } catch (Exception error) {
                        failedData = (F) "服务器数据格式错误";
                        Toast.makeText(context, "服务器数据格式错误", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                failedData = (F) httpEntity.getmMessage();
                Toast.makeText(context,"服务器数据格式错误",Toast.LENGTH_SHORT).show();
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
