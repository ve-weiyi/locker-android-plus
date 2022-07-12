package com.ve.module.android.repository.http

import com.ve.lib.common.http.base.BaseApiService
import okhttp3.OkHttpClient

object WazRetrofitClient : BaseApiService<ApiService>() {

    val service by lazy {
        getApiService()
//        ApiServiceFactory.getService(ApiService::class.java,
//            ApiService.BASE_URL)
    }

    override fun baseUrl(): String {
        return ApiService.BASE_URL
    }

    override fun attachApiService(): Class<ApiService> {
        return ApiService::class.java
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {

    }

}