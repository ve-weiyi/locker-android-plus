package com.ve.lib.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Checkable
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.ve.lib.application.BaseApplication

import com.ve.lib.common.R
import com.ve.lib.common.widget.CustomToast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chenxz on 2018/4/22.
 */
/**
 * Log
 */



fun startActivity(context: Context,activityClass: Class<*>,bundle: Bundle?=null){
    val intent = Intent(context,activityClass)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    /**
     * 启动的standard模式下的activity需要在启动它的Activity的task（任务栈）里面执行
     * 但是由于非Activity类型的Context并没有所谓的任务栈,并且这个时候Activity是以singleTask模式启动的，
     */
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )

    context.startActivity(intent)
}

fun showToast(content: String) {
    CustomToast(BaseApplication.mContext, content).show()
}

fun Fragment.showToast(content: String) {
    CustomToast(this.requireContext(), content).show()
}

fun Context.showToast(content: String) {
    CustomToast(this, content).show()
}

fun Activity.showSnackMsg(msg: String) {
    val snackbar = Snackbar.make(this.window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColor(Color.WHITE)
    snackbar.show()
}

fun Fragment.showSnackMsg(msg: String) {
    this.activity ?: return
    val snackbar = Snackbar.make(this.requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT)
    val view = snackbar.view
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTextColor(Color.WHITE)
    snackbar.show()
}

fun Fragment.getInstance() :Fragment{
    return javaClass.newInstance()
}

fun Fragment.newInstance() :Fragment{
    return javaClass.newInstance()
}



// 扩展点击事件属性(重复点击时长)
var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

// 重复点击事件绑定
inline fun <T : View> T.setSingleClickListener(time: Long = 1000, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

/**
 * getAgentWeb
 */
fun String.getAgentWeb(
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webViewClient: WebViewClient?,
    webChromeClient: WebChromeClient?,
    indicatorColor: Int
): AgentWeb = AgentWeb.with(activity)//传入Activity or Fragment
    .setAgentWebParent(webContent, 1, layoutParams)//传入AgentWeb 的父控件
    .useDefaultIndicator(indicatorColor, 2)// 使用默认进度条
    .setWebView(webView)
    .setWebViewClient(webViewClient)
    .setWebChromeClient(webChromeClient)
    .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
    .interceptUnkownUrl()
    .createAgentWeb()//
    .ready()
    .go(this)

/**
 * 格式化当前日期
 */
fun formatCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

/**
 * String 转 Calendar
 */
fun String.stringToCalendar(): Calendar {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}