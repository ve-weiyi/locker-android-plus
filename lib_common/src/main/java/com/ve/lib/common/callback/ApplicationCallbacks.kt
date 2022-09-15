package com.ve.lib.common.callback

import androidx.lifecycle.*
import com.ve.lib.common.utils.system.LogUtil

/**
 * application 生命周期
 */
open class ApplicationCallbacks: DefaultLifecycleObserver,LifecycleEventObserver{

    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    override fun onCreate(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    /**
     * 应用程序出现到前台时调用
     */
    override fun onStart(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    /**
     * 应用程序出现到前台时调用
     */
    override fun onResume(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    /**
     * 应用程序退出到后台时调用
     */
    override fun onPause(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    /**
     * 应用程序退出到后台时调用
     */
    override fun onStop(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    override fun onDestroy(owner: LifecycleOwner) {
        LogUtil.msg("application ")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }
}