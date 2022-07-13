package com.ve.lib.common.vutils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ve.lib.common.ext.getMethodName
import java.util.*


/**
 * Created by yechao on 2020/1/7.
 * Describe : Activity管理
 *
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object ActivityUtil {
    //Stack(栈)，后进先出
    private val activityStack = Stack<Activity>()

    //Activity的生命周期回调，要求API14+（Android 4.0+）
    private val instance = MyActivityLifecycleCallbacks()

    val activityLifecycleCallbacks: ActivityLifecycleCallbacks
        get() = instance

    val applicationObserver by lazy { ApplicationObserver() }



    /**
     * 获得当前栈顶Activity
     */
    val currentActivity: Activity?
        get() {
            var activity: Activity? = null
            if (!activityStack.isEmpty()) activity = activityStack.peek()
            return activity
        }

    /**
     * 获得当前Activity名字,带包名
     */
    val currentActivityClassName: String
        get() {
            val activity = currentActivity
            var name = ""
            if (activity != null) {
                name = activity.componentName.className
            }
            return name
        }

    /**
     * 获得当前Activity名字，simple name
     */
    val currentActivityName: String
        get() {
            val activity = currentActivity
            var name = ""
            if (activity != null) {
                name = activity.componentName.shortClassName
            }
            return name.substring(name.lastIndexOf(".")+1,name.length)
        }

    /**
     * 启动指定Activity 参数可选，intent.get***Extra 即可获取参数
     */
    fun start(clazz: Class<out Activity>, bundle: Bundle = Bundle()) {
        val intent = Intent(currentActivity, clazz)
        intent.putExtras(bundle)
        currentActivity?.startActivity(intent)
    }

    /**
     * 关闭指定Activity
     */
    @Deprecated(
        "简化调用，使用ActivityUtil.finish(activity)即可",
        ReplaceWith("ActivityUtil.finish(activity)")
    )
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 关闭指定Activity
     */
    fun finish(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 关闭所有Activity
     */
    fun closeAllActivity() {
        while (true) {
            val activity = currentActivity ?: break
            finish(activity)
        }
    }

    fun closeActivityByName(name: String?) {
        var index = activityStack.size - 1
        while (true) {
            val activity = activityStack[index] ?: break
            val activityName = activity.componentName.className
            if (!TextUtils.equals(name, activityName)) {
                index--
                if (index < 0) {
                    break
                }
                continue
            }
            finish(activity)
            break
        }
    }

    fun getActivityStack(): Stack<Activity> {
        val stack = Stack<Activity>()
        stack.addAll(activityStack)
        return stack
    }

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

    /**
     * activity 生命周期
     */
    private class MyActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityStack.remove(activity)
            activityStack.push(activity)

            if(savedInstanceState!=null){
                LogUtil.e("$currentActivityName $savedInstanceState")
            }
        }

        override fun onActivityStarted(activity: Activity) {
//            LogUtil.msg(currentActivityName + getMethodName(3))
        }

        override fun onActivityResumed(activity: Activity) {
            LogUtil.msg(currentActivityName + getMethodName(3))
        }

        override fun onActivityPaused(activity: Activity) {
//            LogUtil.msg(currentActivityName + getMethodName(3))
        }

        override fun onActivityStopped(activity: Activity) {
//            LogUtil.msg(currentActivityName + getMethodName(3))
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
//            LogUtil.msg(currentActivityName + getMethodName(3))
        }

        override fun onActivityDestroyed(activity: Activity) {
            activityStack.remove(activity)
        }
    }
}