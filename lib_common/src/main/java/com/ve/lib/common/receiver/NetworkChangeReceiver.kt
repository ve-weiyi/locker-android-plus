package com.ve.lib.common.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ve.lib.common.event.NetworkChangeEvent
import com.ve.lib.common.config.AppConfig
import com.ve.lib.common.http.util.NetWorkUtil
import com.ve.lib.common.utils.PreferenceUtil
import org.greenrobot.eventbus.EventBus

/**
 * Created by chenxz on 2018/8/1.
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by PreferenceUtil(AppConfig.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}