package com.ve.lib.common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Checkable
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Created by chenxz on 2018/4/22.
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


