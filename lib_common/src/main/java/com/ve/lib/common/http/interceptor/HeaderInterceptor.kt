package com.ve.lib.common.http.interceptor

import com.ve.lib.common.http.constant.HttpConstant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc HeaderInterceptor: 设置请求头
 */
class HeaderInterceptor : Interceptor {

    /**
     * token
     */
    private var token: String by com.ve.lib.common.utils.PreferenceUtil(HttpConstant.TOKEN_KEY, "")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
        // .header("token", token)
        // .method(request.method(), request.body())

        val domain = request.url.host
        val url = request.url.toString()

        if (domain.isNotEmpty()) {
            val spDomain: String by com.ve.lib.common.utils.PreferenceUtil(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(HttpConstant.COOKIE_NAME, cookie)
            }
        }

        return chain.proceed(builder.build())
    }

}