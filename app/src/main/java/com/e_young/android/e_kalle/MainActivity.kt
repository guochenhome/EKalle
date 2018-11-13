package com.e_young.android.e_kalle

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.e_young.plugin.httplibr.util.JsonBodyUtil
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.simple.SimpleCallback
import com.yanzhenjie.kalle.simple.SimpleResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val URL_ROOT = "http://192.168.1.107:8181"


    var URL_CONTENT = "$URL_ROOT/rest/ysh/app"

    /**
     *
     */
    val LOGIN = "$URL_CONTENT/user/authAppUser"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.setOnClickListener {
            login()
        }
    }


    fun login() {

        var param = mutableMapOf(
                "id" to "1"
        )


        Kalle.post(LOGIN)
                .body(JsonBodyUtil.BuilderString(param))
                .tag(this)
                .perform(object : SimpleCallback<BannerBean>() {
                    override fun onResponse(response: SimpleResponse<BannerBean, String>?) {

                        Log.d("", response.toString())

                    }


                })


    }


}
