package com.e_young.android.e_kalle

import android.app.Application
import com.e_young.plugin.httplibr.HttpConfig
import com.e_young.plugin.httplibr.http.JsonConverter
import com.e_young.plugin.httplibr.http.RedirectInterceptor
import com.e_young.plugin.httplibr.util.OSUtil

class App : Application(), JsonConverter.OnJsonConverterLister {
    override fun projectIn() {
        //项目入驻
    }

    override fun authentication() {
        //认证
    }

    override fun authenticationFail() {
        //认证失败，从新认证
    }

    override fun notSingin() {
        // 没有实名认证
    }

    override fun outLogin() {
        // 未登录，登录失败
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * 初始化网络
         */
        HttpConfig.newBuilder(this)
                .isDebug(BuildConfig.DEBUG)
                .setLoggerTag("PluginLogg")
                .setDeviceid(OSUtil.getDeviceId(this))
                .setTokenLister { "111111111111111" }
                .builder()
    }
}