package com.ve.lib.common.network.interceptor

import com.ve.lib.common.utils.system.LogUtil.d
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.charset.UnsupportedCharsetException

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc 日志打印
 */
class ResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime()
        val responseBody = response.body
        if (response.code != 200) {
            return response
        }
        val contentLength = responseBody!!.contentLength()
        if (!bodyEncoded(response.headers)) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer()
            var charset = UTF_8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = try {
                    contentType.charset(UTF_8)
                } catch (e: UnsupportedCharsetException) {
                    return response
                }
            }
            if (contentLength != 0L) {
                val result = buffer.clone().readString(charset!!)
                d(TAG, " response.url: [" + response.request.url + "]")
                d(TAG, " response.body: [$result]")
                d(TAG, " 响应时间: " + (t2 - t1) / 1e6 + "ms")
            }
        }
        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {
        private const val TAG = "ResponseInterceptor"
        private val UTF_8 = StandardCharsets.UTF_8
    }
}