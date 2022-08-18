package com.ve.lib.common.utils.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ve.lib.common.ext.getMethodName
import com.ve.lib.common.utils.log.LogUtil

/**
 * application 生命周期
 */
open class ApplicationObserver: LifecycleObserver {

    //在前台
    var onForeground=true

    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        LogUtil.msg("application "+ getMethodName(3))
    }

    /**
     * 应用程序出现到前台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        LogUtil.msg("application "+ getMethodName(3))
    }

    /**
     * 应用程序出现到前台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        onForeground=true
        LogUtil.msg("application "+ getMethodName(3))
    }

    /**
     * 应用程序退出到后台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        LogUtil.msg("application "+ getMethodName(3))
    }

    /**
     * 应用程序退出到后台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        onForeground=false
        LogUtil.msg("application "+ getMethodName(3))
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        LogUtil.msg("application "+ getMethodName(3))
    }
}