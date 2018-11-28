package com.e_young.plugin.httplibr.http;

import android.util.Log;

import com.e_young.plugin.httplibr.core.HeadConsts;
import com.yanzhenjie.kalle.BodyRequest;
import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.UrlRequest;
import com.yanzhenjie.kalle.connect.Interceptor;
import com.yanzhenjie.kalle.connect.http.Chain;
import com.yanzhenjie.kalle.util.IOUtils;

import java.io.IOException;

public class RedirectInterceptor implements Interceptor {


    public RedirectInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.isRedirect()) {
            Url oldUrl = request.url();
            Url url = oldUrl.location(response.headers().getLocation());
            Headers headers = request.headers();
            headers.set(HeadConsts.TOKEY,"11111");

            RequestMethod method = request.method();
            Request newRequest;
            if (method.allowBody()) {
                newRequest = BodyRequest.newBuilder(url.builder(), request.method())
                        .setHeaders(headers)
                        .setParams(request.copyParams())
                        .body(request.body())
                        .build();
            } else {
                newRequest = UrlRequest.newBuilder(url.builder(), request.method())
                        .setHeaders(headers)
                        .build();
            }
            IOUtils.closeQuietly(response);
            return chain.proceed(newRequest);
        }
        return response;
    }

    public interface OnRedirectInterceLister {
        String OnGetToken();
    }
}
