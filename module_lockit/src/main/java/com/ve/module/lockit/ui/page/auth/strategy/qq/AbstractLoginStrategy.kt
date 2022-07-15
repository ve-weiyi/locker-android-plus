package com.ve.module.lockit.ui.page.auth.strategy.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * @Author  weiyi
 * @Date 2022/7/15
 * @Description  current project lockit-android
 */
interface AbstractLoginStrategy {

//    var listener:T

    fun init()

    fun doLogin(activity: Activity)

    fun loginOut(context: Context)


    fun checkLogin(activity: Activity)


    fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent?){

    }
//    fun onComplete(var1: Any?) {
//
//    }
//
//    fun onError(var1: UiError?) {
//
//    }
//
//    fun onCancel() {
//
//    }
//
//    fun onWarning(var1: Int) {
//
//    }
}