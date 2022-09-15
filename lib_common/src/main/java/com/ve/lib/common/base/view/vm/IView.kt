package com.ve.lib.common.base.view.vm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.launcher.ARouter
import com.ve.lib.common.utils.view.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * @author chenxz
 *
 * JDK1.8之后，在接口里面可以定义default方法，default方法里面是可以具备方法体的，
 * 当子类实现该接口之后，不需要重写该方法即可以调用该方法。
 *
 * @date 2019/11/1
 * @desc IView
 */
interface IView <VB : ViewBinding> {

    /**
     * 返回绑定对象
     * return ActivityMainBinding.inflate(layoutInflater)
     */
    abstract fun attachViewBinding(): VB

    /**
     * 初始化函数，命名与子类BaseVmActivity初始化函数区分。
     */
    abstract fun initialize(saveInstanceState: Bundle?)

    /**
     * 显示加载
     */
    fun showLoading() {

    }

    /**
     * 隐藏加载
     */
    fun hideLoading(){

    }

    /**
     * 显示信息
     */
    fun showMsg(msg: String){
        ToastUtil.show(msg)
    }

    /**
     * 显示错误信息
     */
    fun showError(errorMsg: String) {
        ToastUtil.show(errorMsg)
    }


    fun startActivity(path: String?) {
        startActivity(path, null)
    }

    fun startActivity(activity: Activity?, path: String?) {
        startActivity(activity, path, -1)
    }

    fun startActivity(path: String?, bundle: Bundle?) {
        startActivity(null, path, bundle, -1)
    }

    fun startActivity(activity: Activity?, path: String?, requestCode: Int) {
        startActivity(activity, path, null, requestCode)
    }

    fun startActivity(activity: Activity?, path: String?, bundle: Bundle?, requestCode: Int) {
        val postcard = ARouter.getInstance()
            .build(path)
            .with(bundle)
        if (activity != null) {
            postcard.type = RouteType.ACTIVITY
        }
        postcard.navigation(activity, requestCode)
    }

    fun startActivity(context: Context,activityClass: Class<*>,bundle: Bundle?=null){
        val intent = Intent(context,activityClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }



    fun launchOnBackground(function:suspend () -> Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            function.invoke()
        }
    }


}
