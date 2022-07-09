package com.ve.module.sunny.logic.http.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceCreator {

    /**
     * 回应超时
     * 连接超时
     * 读取超时 默认为ms
     */
    private const val CALL_TIMEOUT = 10L
    private const val CONNECT_TIMEOUT = 20L
    private const val IO_TIMEOUT = 20L

    private lateinit var apiCaiyun: ApiCaiyun


    fun getApiCaiyun(): ApiCaiyun {
        val httpLoggingInterceptor = HttpLoggingInterceptor { Log.d("httpLog", it) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //从响应头里拿到cookie并存起来，后面的每次请求再添加到请求头里
        /**OkHttpClient  底层封装 也可以使用HttpURLConnection*/
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
            //设置Cookie持久化
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()

        /**Retrofit  上层封装 关联okHttp并加上rxJava和Gson的配置和baseUrl*/
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiCaiyun.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())//Gson解析方式，json自动映射为对象，重写对json的预处理
            .build()

        /**ApiService*/
        apiCaiyun = retrofit.create(ApiCaiyun::class.java)

        return apiCaiyun
    }
}
