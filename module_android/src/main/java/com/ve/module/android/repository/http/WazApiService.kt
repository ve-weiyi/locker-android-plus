package com.ve.module.android.repository.http

import com.ve.lib.common.network.base.BaseApiService
import com.ve.lib.common.network.interceptor.AddCookieInterceptor
import com.ve.lib.common.network.interceptor.SaveCookieInterceptor
import okhttp3.OkHttpClient

object WazApiService : BaseApiService<ApiService>() {

    val service by lazy {
        getApiService()
//        ApiServiceFactory.getService(ApiService::class.java, ApiService.BASE_URL)
    }

    override fun baseUrl(): String {
        return ApiService.BASE_URL
    }

    override fun attachApiService(): Class<ApiService> {
        return ApiService::class.java
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder
            //响应报文保存、添加 cookie
            .addInterceptor(SaveCookieInterceptor("waz cookie"))
            //响应报文保存、添加 cookie
            .addInterceptor(AddCookieInterceptor("waz cookie"))
    }

}