package com.ve.module.locker.model.http.api

import com.ve.lib.common.http.base.BaseApiService
import com.ve.module.locker.api.LockerService
import okhttp3.OkHttpClient

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
class LockerApiService:BaseApiService<LockerService>() {

    override fun baseUrl(): String {
        return LockerService.BASE_URL
    }

    override fun attachApiService(): Class<LockerService> {
        return LockerService::class.java
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        val tokenInterceptor= TokenInterceptor()
        builder.addInterceptor(tokenInterceptor)
    }

}