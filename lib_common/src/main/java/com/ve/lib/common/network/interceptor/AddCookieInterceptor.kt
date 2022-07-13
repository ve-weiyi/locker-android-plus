package com.ve.lib.common.network.interceptor

import com.ve.lib.common.vutils.SpUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @Author  weiyi
 * @Date 2022/7/13
 * @Description  current project locker-android
 * 从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
 */
class AddCookieInterceptor(
    private val spCookieKey:String="cookie"
) : Interceptor {
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        /**
         * 添加cookie
         */
        val stringSet = SpUtil.getStringSet(spCookieKey)
        for (cookie in stringSet) {
            builder.addHeader("Cookie", cookie)
        }

        return chain.proceed(builder.build())
    }
}