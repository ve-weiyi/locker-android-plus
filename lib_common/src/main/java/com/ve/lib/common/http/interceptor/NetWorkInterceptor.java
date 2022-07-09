package com.ve.lib.common.http.interceptor;


import com.ve.lib.common.http.util.NetWorkUtil;
import com.ve.lib.common.vutils.AppContextUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * @author weiyi
 * @date 2022/4/10
 * @desc CookieInterceptor: 保存 Cookie
 */
public class NetWorkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //无网络时强制使用缓存
        if (!NetWorkUtil.isConnected(AppContextUtils.mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (NetWorkUtil.isConnected(AppContextUtils.mContext)) {
            //有网络时，设置超时为0
            int maxStale = 0;
            response.newBuilder()
                    .header("Cache-Control", "public,max-age=" + maxStale)
                    .build();
        } else {
            //无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cache, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
