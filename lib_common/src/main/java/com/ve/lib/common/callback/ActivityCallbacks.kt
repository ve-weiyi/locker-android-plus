package com.ve.lib.common.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.ve.lib.application.BaseApplication

/**
 * @author waynie
 * @date 2022/9/16
 * @desc lockit-android
 */
class ActivityCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(BaseApplication.TAG, "onCreated: " + activity.componentName.className)
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(BaseApplication.TAG, "onStart: " + activity.componentName.className)
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(BaseApplication.TAG, "onDestroy: " + activity.componentName.className)
    }
}
