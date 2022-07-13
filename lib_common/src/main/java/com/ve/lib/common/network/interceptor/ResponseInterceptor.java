package com.ve.lib.common.network.interceptor;


import com.ve.lib.common.vutils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc 日志打印
 */
public class ResponseInterceptor implements Interceptor {
    private static final String TAG = "ResponseInterceptor";

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        ResponseBody responseBody = response.body();
        if (response.code() != 200) {
            return response;
        }
        long contentLength = responseBody.contentLength();

        if (!bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();

            Charset charset = UTF_8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF_8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                LogUtil.d(TAG, " response.url: [" + response.request().url() + "]");
                LogUtil.d(TAG, " response.body: [" + result + "]");
                LogUtil.d(TAG, " 响应时间: " + (t2 - t1) / 1e6d + "ms");
            }
        }

        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}
