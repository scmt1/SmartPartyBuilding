package com.yami.shop.security.api.util;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class OKHttpClientFactory {
    private static final OkHttpClient OK_HTTP_CLIENT;
    private static OkHttpClient OK_HTTP_CLIENT_WITH_PROXY;

    public OKHttpClientFactory() {
    }

    public static OkHttpClient getClient() {
        return OK_HTTP_CLIENT;
    }

    static {
        OK_HTTP_CLIENT = (new OkHttpClient.Builder()).connectTimeout(3L, TimeUnit.SECONDS).writeTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS).addInterceptor((chain) -> {
            long start = System.nanoTime();

            Response var3;
            try {
                var3 = chain.proceed(chain.request());
            } finally {
            }

            return var3;
        }).build();
    }
}
