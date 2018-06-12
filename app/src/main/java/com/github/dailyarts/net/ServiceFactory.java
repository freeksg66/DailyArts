package com.github.dailyarts.net;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by legao005426 on 2018/5/17.
 * 对Retrofit 2.x的简单封装
 */
public class ServiceFactory {

    private static final GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create());

    private static final long CONNECTION_TIMEOUT = 30L;
    private static final long HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024; //10MiB

    /**
     * createUri service with default settings
     */
    public static <S> S createService(String endpoint, Class<S> serviceClass) {
        return getRetrofitBuilder(endpoint, 0).build().create(serviceClass);
    }

    public static <S> S createService(String endpoint, Class<S> serviceClass, int timeout) {
        return getRetrofitBuilder(endpoint, timeout).build().create(serviceClass);
    }

    @NonNull
    private static Retrofit.Builder getRetrofitBuilder(String endpoint, int timeout) {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(getOkHttpClient());
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();

        okClientBuilder.addInterceptor(getHttpLoggingInterceptor());

        setTimeOut(100, okClientBuilder);

        return okClientBuilder.build();
    }

    private static void setTimeOut(int timeout, OkHttpClient.Builder okClientBuilder) {
        if (timeout == 0) {
            okClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        } else {
            okClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS);
        }
    }

    @NonNull
    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }

}
