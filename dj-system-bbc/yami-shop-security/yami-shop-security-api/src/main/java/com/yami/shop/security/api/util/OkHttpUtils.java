package com.yami.shop.security.api.util;

import com.yami.shop.common.exception.YamiShopBindException;
import okhttp3.*;
import okio.BufferedSink;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    private static final String FAIL_MSG = "call api %s failed. headers=%s, code=%s, message=%s";
    private static final String TIMEOUT_MSG = "call api %s timeout. headers=%s";
    private static final String GW_ERROR_MSG = "call api %s gateway error. headers=%s";
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final RequestBody EMPTY_BODY = new RequestBody() {
        @Nullable
        public MediaType contentType() {
            return OkHttpUtils.APPLICATION_JSON;
        }

        public void writeTo(BufferedSink bufferedSink) throws IOException {
        }
    };

    public OkHttpUtils() {
    }

    public static String syncHttps(String url, String method, Map<String, Object> headers, String body, String contentType) {
        Call call = okHttpCall(url, method, headers, body, contentType);
        try {
            Response response = call.execute();
            Throwable var7 = null;
            try {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String var9 = responseBody.string();
                        return var9;
                    }
                }

                throw new YamiShopBindException(String.format("call api %s failed. headers=%s, code=%s, message=%s", url, headers, response.code(), response.message()), response.code());
            } catch (Throwable var21) {
                var7 = var21;
                throw var21;
            } finally {
                if (response != null) {
                    if (var7 != null) {
                        try {
                            response.close();
                        } catch (Throwable var20) {
                            var7.addSuppressed(var20);
                        }
                    } else {
                        response.close();
                    }
                }

            }
        } catch (YamiShopBindException var23) {
            throw var23;
        } catch (SocketTimeoutException var24) {
            throw new YamiShopBindException(String.format("call api %s timeout. headers=%s", url, headers), 504);
        } catch (Throwable var25) {
            throw new YamiShopBindException(String.format("call api %s gateway error. headers=%s", url, headers), 502);
        }
    }


    public static String okHttpFrom(String url, Map<String, String> headers, Map<String, String> params) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder1 = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            params.forEach((k, v) -> {
                builder1.add(k, v);
            });
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(builder1.build());
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> {
                builder.addHeader(k, v);
            });
        }
        try {
            Response response = client.newCall(builder.build()).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static Call okHttpCall(String url, String method, Map<String, Object> headers, String body, String contentType) {
        Request.Builder builder = (new Request.Builder()).url(url);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((k, v) -> {
                builder.header(k, v.toString());
            });
        }

        if (body != null && body.length() > 0) {
            builder.method(method, RequestBody.create(getMediaType(contentType), body));
        } else if ("POST".equalsIgnoreCase(method)) {
            builder.post(getRequestBody(contentType));
        }

        return OKHttpClientFactory.getClient().newBuilder().connectTimeout(3L, TimeUnit.SECONDS).readTimeout(15L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).build().newCall(builder.build());
    }

    private static MediaType getMediaType(String contentType) {
        return contentType != null && !"".equals(contentType) && !"application/json; charset=utf-8".equalsIgnoreCase(contentType) ? MediaType.parse(contentType) : APPLICATION_JSON;
    }

    private static RequestBody getRequestBody(final String contentType) {
        return contentType != null && !"".equals(contentType) ? new RequestBody() {
            @Nullable
            public MediaType contentType() {
                return MediaType.parse(contentType);
            }

            public void writeTo(BufferedSink bufferedSink) throws IOException {
            }
        } : EMPTY_BODY;
    }
}
