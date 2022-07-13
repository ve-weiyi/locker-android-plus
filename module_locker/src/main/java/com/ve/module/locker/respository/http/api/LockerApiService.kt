package com.ve.module.locker.respository.http.api

import com.ve.lib.common.network.base.BaseApiService
import com.ve.lib.common.network.interceptor.AddCookieInterceptor
import com.ve.lib.common.network.interceptor.SaveCookieInterceptor
import com.ve.lib.common.network.interceptor.TokenInterceptor
import com.ve.module.locker.api.LockerService
import com.ve.module.locker.common.config.LockerSpKey
import okhttp3.OkHttpClient

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object LockerApiService : BaseApiService<LockerService>() {


    override fun baseUrl(): String {
        return LockerService.BASE_URL
    }

    override fun attachApiService(): Class<LockerService> {
        return LockerService::class.java
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder
            //响应报文保存、添加 cookie
            .addInterceptor(SaveCookieInterceptor(LockerSpKey.COOKIE_KEY))
            //响应报文保存、添加 cookie
            .addInterceptor(AddCookieInterceptor(LockerSpKey.COOKIE_KEY))
            //添加 token
            .addInterceptor(TokenInterceptor(spTokenKey = LockerSpKey.TOKEN_KEY))
    }

}