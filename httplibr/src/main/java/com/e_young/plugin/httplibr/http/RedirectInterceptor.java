package com.e_young.plugin.httplibr.http;

import com.e_young.plugin.httplibr.core.HeadConsts;
import com.yanzhenjie.kalle.BodyRequest;
import com.yanzhenjie.kalle.Headers;
import com.yanzhenjie.kalle.Request;
import com.yanzhenjie.kalle.RequestMethod;
import com.yanzhenjie.kalle.Response;
import com.yanzhenjie.kalle.UrlRequest;
import com.yanzhenjie.kalle.connect.Interceptor;
import com.yanzhenjie.kalle.connect.http.Chain;

import java.io.IOException;

/**
 *  暂时作为token拦截器，，在每次请求  重定向 添加token
 */
public class RedirectInterceptor implements Interceptor {

    private OnRedirectInterceLister lister;

    public RedirectInterceptor(OnRedirectInterceLister lister) {
        this.lister = lister;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Headers headers = request.headers();
        headers.set(HeadConsts.TOKEY, lister.OnGetToken());

        RequestMethod method = request.method();
        Request newRequest;
        if (method.allowBody()) {
            newRequest = BodyRequest.newBuilder(request.url(), request.method())
                    .setHeaders(headers)
                    .setParams(request.copyParams())
                    .body(request.body())
                    .build();
        } else {
            newRequest = UrlRequest.newBuilder(request.url(), request.method())
                    .setHeaders(headers)
                    .build();
        }
        return chain.proceed(newRequest);
    }

    public interface OnRedirectInterceLister {
        String OnGetToken();
    }
}
