package com.ve.lib.common.network.interceptor

import com.ve.lib.common.utils.log.LogUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author chenxz
 * @date 2018/9/26
 * @desc HeaderInterceptor: 设置请求头
 */
class HeaderInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
        // .header("token", token)
        // .method(request.method(), request.body())


        val domain = request.url.host
        val url = request.url.toString()
        LogUtil.msg(request.body)
        LogUtil.msg(domain)
        LogUtil.msg(url)
        return chain.proceed(builder.build())
    }

}