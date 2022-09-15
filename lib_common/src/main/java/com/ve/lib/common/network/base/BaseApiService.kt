package com.ve.lib.common.network.base

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.network.constant.HttpConstant
import com.ve.lib.common.network.interceptor.CacheInterceptor
import com.ve.lib.common.network.interceptor.HeaderInterceptor
import com.ve.lib.common.network.interceptor.ResponseInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author admin
 * @date 2018/11/21
 * @desc RetrofitFactory
 * 创建API接口实例需要实现 两个方法：baseUrl()  attachApiService()
 * 调用时需要只需要.service
 */
abstract class BaseApiService<T> {

    private var apiService: T

    private var baseUrl = ""

    private var retrofit: Retrofit? = null

    protected abstract fun baseUrl(): String

    protected abstract fun attachApiService(): Class<T>

    init {
        baseUrl = this.baseUrl()
        if (baseUrl.isEmpty()) {
            throw RuntimeException("base url can not be empty!")
        }
        apiService = getRetrofit()!!.create(this.attachApiService())
    }

    fun getApiService(): T {
        return apiService
    }

    /**
     * 获取 Retrofit 实例对象
     */
    private fun getRetrofit(): Retrofit? {
        synchronized(BaseApiService::class.java) {
            if (retrofit == null) {
                retrofit = attachRetrofit(baseUrl)
            }
        }
        return retrofit
    }

    /**
     * 获取 retrofit 实例对象
     * 子类可重写，自定义 retrofit
     */
    private fun attachRetrofit(baseUrl: String): Retrofit {
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

        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(BaseApplication.mContext))

        val builder = OkHttpClient()
            .newBuilder()
            .cache(cache)  //添加缓存
            .cookieJar(cookieJar)
            //请求拦截器
            .addInterceptor(HeaderInterceptor())
            //响应拦截器
            .addInterceptor(ResponseInterceptor())
            //缓存拦截器
            .addInterceptor(CacheInterceptor())
            //设置请求超时时间
            .callTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // 错误重连
        // cookieJar(CookieManager())
        handleBuilder(builder)
        return builder.build()
    }

    /**
     * 修改设置
     * 添加一些自定义拦截器 cookie token
     */
    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    open fun <T> create(cls: Class<T>?): T {
        return retrofit!!.create<T>(cls)
    }
}