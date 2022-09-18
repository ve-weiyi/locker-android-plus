package com.ve.lib.common.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.ve.lib.common.utils.ui.ActivityController
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by yechao on 2020/1/7.
 * Describe : 快速开发工具集合
 * <p>
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object AppContextUtil {

    lateinit var mContext: Application

    fun init(app: Application) {
        mContext = app
        app.registerActivityLifecycleCallbacks(ActivityController.activityLifecycleCallbacks)
    }


    fun getApp(): Application {
        if (this::mContext.isInitialized) {
            return mContext
        } else {
            throw UninitializedPropertyAccessException("YUtils is not initialized in application")
        }
    }



    /**
     * MD5加密
     */
    fun MD5(data: String): String {
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        md5!!.update(data.toByteArray())
        val m = md5.digest()
        return Base64.encodeToString(m, Base64.DEFAULT)
    }



}
