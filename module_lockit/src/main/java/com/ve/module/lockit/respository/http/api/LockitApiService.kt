package com.ve.module.lockit.respository.http.api

import com.ve.lib.common.network.base.BaseApiService
import com.ve.lib.common.network.interceptor.AddCookieInterceptor
import com.ve.lib.common.network.interceptor.SaveCookieInterceptor
import com.ve.lib.common.network.interceptor.TokenInterceptor
import com.ve.module.lockit.common.config.LockitSpKey
import okhttp3.OkHttpClient

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object LockitApiService : BaseApiService<LockitService>() {


    override fun baseUrl(): String {
        return LockitService.BASE_URL
    }

    override fun attachApiService(): Class<LockitService> {
        return LockitService::class.java
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder
            //响应报文保存、添加 cookie
            .addInterceptor(SaveCookieInterceptor(LockitSpKey.COOKIE_KEY))
            //响应报文保存、添加 cookie
            .addInterceptor(AddCookieInterceptor(LockitSpKey.COOKIE_KEY))
            //添加 token
            .addInterceptor(TokenInterceptor(spTokenKey = LockitSpKey.TOKEN_KEY))
    }

}