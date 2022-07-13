package com.ve.lib.common.network.interceptor

import com.ve.lib.common.vutils.SpUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @Author  weiyi
 * @Date 2022/7/13
 * @Description  current project lockit-android
 * 从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
 */
class SaveCookieInterceptor(
    private val spCookieKey:String="cookie"
) : Interceptor {
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url.toString()
        val domain = request.url.host

        /**
         * 保存cookie
         */
        if (response.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in response.headers("Set-Cookie")) {
                cookies.add(header)
            }
            SpUtil.setStringSet(spCookieKey, cookies)
        }

        return response
    }
}