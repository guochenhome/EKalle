package com.e_young.plugin.httplibr;

import android.content.Context;

import com.e_young.plugin.httplibr.core.HeadConsts;
import com.e_young.plugin.httplibr.http.JsonConverter;
import com.e_young.plugin.httplibr.util.OSUtil;
import com.e_young.plugin.httplibr.util.SystemUtil;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.KalleConfig;
import com.yanzhenjie.kalle.OkHttpConnectFactory;
import com.yanzhenjie.kalle.connect.BroadcastNetwork;
import com.yanzhenjie.kalle.connect.http.LoggerInterceptor;

import java.util.concurrent.TimeUnit;


/**
 * @author guochen
 * <p>
 * 具体详解请参考-->
 * 用于初始化网络请求及配置
 * 后期会根据不同需求添加拦截器
 * @see <p>https://www.yanzhenjie.com/Kalle/config/</p>
 */
public class HttpConfig {


    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public HttpConfig(Context context, Builder builder) {
        Kalle.setConfig(getKallConfig(context, builder.loggerTag, builder.Debug, builder.DEVICEID, builder.lister, builder.Token));
    }

    /**
     * 初始化 kalleconfig 提供者
     *
     * @return params.put(" appVersion ", AppUtil.getAppVersionName ( ManagerApplication.getApplication ()));
     * params.put("Content-Type", "application/json; charset=UTF-8");
     * params.put("os", "android");
     * params.put("osLang", "zh");
     * params.put("osVersion", android.os.Build.VERSION.RELEASE);
     * params.put("phoneModel", Build.MODEL);
     * params.put("token", StringUtil.isNotBlank(HeadItem.getInstance().getToken()) ? HeadItem.getInstance().getToken() : SpHelper.getToken());
     * Map<String, String> deviceInfo = getDeviceInfo();
     * String brand = deviceInfo.get("BRAND");
     * String model = deviceInfo.get("MODEL");
     * params.put("osType", model.toLowerCase().contains("mi".toLowerCase()) ? model : (StringUtil.isNotBlank(brand) ? brand : "android"));
     * <p>
     * if (android.os.Build.MODEL.toLowerCase().contains("mi".toLowerCase())) {//小米
     * params.put("deviceId", MyPreferences.getregid());
     * } else {
     * params.put("deviceId", JPushInterface.getUdid(context));
     * }
     */
    private KalleConfig getKallConfig(Context context, String loggerTag
            , boolean Debug, String deviceid
            , JsonConverter.OnJsonConverterLister lister
            , String token) {

        KalleConfig config = null;

        try {
            config = KalleConfig.newBuilder()
                    .addInterceptor(new LoggerInterceptor(loggerTag, Debug))
                    .connectionTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .network(new BroadcastNetwork(context))
                    .connectFactory(OkHttpConnectFactory.newBuilder().build())
                    .converter(new JsonConverter(context, lister))
                    .addHeader(HeadConsts.APPVER, OSUtil.getAppVersionName(context))
                    .addHeader(HeadConsts.CONTENT_TYPE, HeadConsts.CONTENT_TYPE_VEL)
                    .addHeader(HeadConsts.OS, HeadConsts.OS_VEL)
                    .addHeader(HeadConsts.OSLANG, HeadConsts.OS_LANG_VEL)
                    .addHeader(HeadConsts.OSVERSION, SystemUtil.getSystemVersion())
                    .addHeader(HeadConsts.PHONEMODEL, SystemUtil.getSystemModel())
                    .addHeader(HeadConsts.OSTYPE, OSUtil.getOsType(context))
                    .addHeader(HeadConsts.DEVICEID, deviceid)
                    .addParam(HeadConsts.TOKEY, token)
                    .build();
        } catch (Exception error) {
            config = KalleConfig.newBuilder().build();
        }
        return config;
    }


    /**
     * 网络配置的 构建方法
     */
    public static class Builder {
        /**
         * 上下文环境
         * 如果是全局配置，要求使用appliction 全局上下文环境
         */
        Context context;
        /**
         * 日志默认 tag，如果没有设置tag，则默认一下关键字
         */
        private String loggerTag = "plugin_log";
        /**
         * debug 开关，若为设置，则默认 关闭
         */
        private boolean Debug = false;

        /**
         * 极光识别的 udid
         *
         * @param context
         */
        private String DEVICEID = "";
        /**
         * 拦截器 jsonconverter
         *
         * @param context
         */
        private JsonConverter.OnJsonConverterLister lister;
        /**
         * token
         */
        private String Token;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLoggerTag(String tag) {
            this.loggerTag = tag;
            return this;
        }

        public Builder isDebug(boolean debug) {
            this.Debug = debug;
            return this;
        }

        public Builder setDeviceid(String id) {
            this.DEVICEID = id;
            return this;
        }

        public Builder setJsonConverter(JsonConverter.OnJsonConverterLister lister) {
            this.lister = lister;
            return this;
        }

        public Builder setToken(String token) {
            this.Token = token;
            return this;
        }


        public HttpConfig builder() {
            return new HttpConfig(this.context, this);
        }


    }
}
