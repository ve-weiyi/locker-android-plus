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
import com.ve.lib.common.utils.manager.ActivityController
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
     * 根据时间休眠然后关闭当前页面
     * 比如：5秒自动返回
     * 或者只需要后台给一个结果而已
     */
    fun finishBySleep(millis: Long) {
        object : Thread() {
            override fun run() {
                try {
                    sleep(millis)
                    ActivityController.currentActivity?.finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    /**
     * 获取版本名
     */
    fun getVersionName(): String {
        return try {
            val packageManager = getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(getApp().packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取版本号
     */
    fun getVersionCode(): Int {
        return try {
            val packageManager = getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(getApp().packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
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


    /**
     * 字体高亮
     */
    fun foreground(view: View?, color: Int, start: Int, end: Int): View? {
        if (view is Button) {
            // 获取文字
            val span: Spannable = SpannableString(view.text.toString())
            //设置颜色和起始位置
            span.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            view.text = span
            return view
        } else if (view is TextView) { //EditText extends TextView
            val span: Spannable = SpannableString(view.text.toString())
            span.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            view.text = span
            return view
        }
        return null
    }

    /**
     * 弹出软键盘
     */
    fun showSoftKeyboard(view: View) {
        val inputManger = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 关闭软键盘
     */
    fun closeSoftKeyboard() {
        val inputManger = ActivityController.currentActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManger.hideSoftInputFromWindow(ActivityController.currentActivity?.window?.decorView?.windowToken, 0)
    }

    /**
     * 是否有sim卡 即设备是否可以拨打电话等
     */
    fun hasSim(): Boolean {
        val telephonyManager = getApp().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var result = true
        when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> result = false
            TelephonyManager.SIM_STATE_UNKNOWN -> result = false
        }
        return result
    }
}
