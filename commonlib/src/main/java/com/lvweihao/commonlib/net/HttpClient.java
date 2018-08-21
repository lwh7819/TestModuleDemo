package com.lvweihao.commonlib.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp和Retrofit对象类
 * Created by lv.weihao on 2018/1/15.
 */
public abstract class HttpClient {

    public static final int DEFAULT_CONNECT_TIMEOUT = 10;
    public static final int DEFAULT_READ_TIMEOUT = 20;
    public static final int DEFAULT_WRITE_TIMEOUT = 60;

    protected OkHttpClient.Builder builder;
    protected Retrofit retrofit;

    protected HttpClient() {
        //创建OkhttpClient
        builder = new OkHttpClient.Builder()
                //设置超时
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)
                //支持Https，明文Http与比较新的Https
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS));

        //添加Header
        builder.addInterceptor(new HttpHeaderInterceptor());

        //添加调试工具
        builder.networkInterceptors().add(new StethoInterceptor());

        //添加拦截
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        //创建Retrofit实例
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();

        initApi();
    }

    /**
     * 创建消息头
     */
    private class HttpHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
//                    .header("Accept", "application/json")
//                    .header("Content-type", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 获取API URL
     *
     * @return
     */
    protected abstract String getBaseUrl();

    /**
     * 创建Retrofit对象
     */
    protected abstract void initApi();
}
