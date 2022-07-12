package com.ve.module.locker.respository.http.api

import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.common.config.LockerConstant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc HeaderInterceptor: 设置请求头
 */
class TokenInterceptor() : Interceptor {

    private var mTokenName: String = "Authorization"

    /**
     * token
     */
    private var mToken: String = " "

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        mToken = SpUtil.getValue(LockerConstant.TOKEN_KEY,"Authorization is null")

        LogUtil.d("mTokenName $mTokenName")
        LogUtil.d("mToken $mToken")

        builder.header(mTokenName, mToken)

        return chain.proceed(builder.build())
    }

}