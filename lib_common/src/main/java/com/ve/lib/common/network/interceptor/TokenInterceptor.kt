package com.ve.lib.common.network.interceptor

import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.system.SpUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc
 * 请求头添加token
 */
class TokenInterceptor(
    private var tokenName: String = "Authorization",
    /**
     * token
     */
    private var spTokenKey: String = "token"
) : Interceptor {

    /**
     * token
     */
    private var token: String = " "

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        token = SpUtil.getValue(spTokenKey, "Authorization is null")

        LogUtil.d("$tokenName :$token")
        builder.header(tokenName, token)

        return chain.proceed(builder.build())
    }

}