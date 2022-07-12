package com.ve.lib.common.network.base

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.network.constant.HttpConstant
import com.ve.lib.common.network.interceptor.CacheInterceptor
import com.ve.lib.common.network.interceptor.CookieInterceptor
import com.ve.lib.common.network.interceptor.HeaderInterceptor
import com.ve.lib.common.network.interceptor.ResponseInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/25
 */
object ApiServiceFactory {

    fun <T> getService(serviceClass: Class<T>, baseUrl: String): T {
        return attachRetrofit(baseUrl).create(serviceClass)
    }

    fun attachRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(attachOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .baseUrl(baseUrl)
            .build()
    }


    /**
     * 获取 OkHttpClient 实例对象
     * 子类可重写，自定义 OkHttpClient
     */
    private fun attachOkHttpClient(): OkHttpClient {
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(BaseApplication.mContext.cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)

        val cookieJar = PersistentCookieJar(
            SetCookieCache(), SharedPrefsCookiePersistor(
                BaseApplication.mContext
            )
        )

        return OkHttpClient()
            .newBuilder()
            .cache(cache)  //添加缓存
            .cookieJar(cookieJar)
            //网页请求日志输出
            //  addInterceptor(httpLoggingInterceptor)
            //请求拦截器
            .addInterceptor(HeaderInterceptor())
            //响应拦截器
            .addInterceptor(ResponseInterceptor())
            //缓存拦截器
            .addInterceptor(CacheInterceptor())
            //响应报文保存、添加 cookie
            .addInterceptor(CookieInterceptor())
            //设置请求超时时间
            .callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // 错误重连
            // cookieJar(CookieManager())
            .build()
    }

}