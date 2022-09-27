package com.ve.lib.common.lifecycle

import androidx.lifecycle.*
import com.ve.lib.application.utils.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @Author  weiyi
 * @Date 2022/9/17
 * @Description  current project lockit-android
 */
class EventBusLifecycle private constructor() : DefaultLifecycleObserver {

    companion object {
        val instant by lazy { EventBusLifecycle() }
    }

    /**
     * 必须要类中有@Subscribe才可以使用，要不然会报错
     */
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (!EventBus.getDefault().isRegistered(owner))//加上判断,注销订阅者
        {
            EventBus.getDefault().register(owner)
        }
        LogUtil.msg("1 event bus "+owner::class.java.name)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (EventBus.getDefault().isRegistered(owner))//加上判断,注销订阅者
        {
            EventBus.getDefault().unregister(owner)
        }
        LogUtil.msg("2 event bus "+owner::class.java.name)
    }
}