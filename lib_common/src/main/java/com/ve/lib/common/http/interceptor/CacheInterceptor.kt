package com.ve.lib.common.http.interceptor

import com.ve.lib.application.BaseApplication
import com.ve.lib.common.http.util.NetWorkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author weiyi
 * @date 2022/4/10
 * @desc CacheInterceptor: 设置缓存
 *  无网络时从本地获取
 */
class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        //无网络时强制使用缓存
        if (!NetWorkUtil.isConnected(BaseApplication.mContext)) {
            //网络不可连接
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }

        val response = chain.proceed(request)
        if (NetWorkUtil.isConnected(BaseApplication.mContext)) {
            val maxAge = 0
            // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build()
        } else {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("nyn")
                .build()
        }
        return response
    }
}