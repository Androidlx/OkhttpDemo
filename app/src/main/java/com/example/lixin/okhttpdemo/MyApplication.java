package com.example.lixin.okhttpdemo;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hua on 2017/9/7.
 */

public class MyApplication extends Application {
    //OkHttpClient实例是唯一的, 所有的请求都会通过这个OkHttpClient,所以所有的请求都可能被拦截器拦截,
    // 我们可以在这个必经之路,做一些通用的操作,比如打印日志.
    private static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //建议一个app只有一个OkHttpClient实例
        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .addInterceptor(new MyLogInterceptor());
    }
    public static OkHttpClient okHttpClient(){
        return okHttpClient;
    }
    //拦截器,可以修改header,可以通过拦截器打印日志
    public class MyLogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request().newBuilder()
                    .header("shenfen", "chinesse")
                    .build();
            HttpUrl url = request.url();
            String httpUrl = url.url().toString();
            Log.e("TAG", "============" + httpUrl);
            Response response = chain.proceed(request);
            int code = response.code();
            Log.e("TAG", "============response.code() == " + code);
            return response;
        }
    }
}
