package com.e_young.android.e_kalle

import android.app.Application
import com.e_young.plugin.httplibr.HttpConfig
import com.e_young.plugin.httplibr.util.OSUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * 初始化网络
         */
        HttpConfig.newBuilder(this)
                .isDebug(BuildConfig.DEBUG)
                .setLoggerTag("PluginLogg")
                .setDeviceid(OSUtil.getDeviceId(this))
                .builder()
    }
}