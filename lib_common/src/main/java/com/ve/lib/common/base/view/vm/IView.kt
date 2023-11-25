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
interface IView{


    /**
     * step 1.初始化view相关数据, 需要在view初始化之前完成
     */
    fun initData() = true

    /**
     * step 2.初始化函数，命名与子类BaseVmActivity初始化函数区分。
     */
    abstract fun initialize()





    /**
     * 显示错误信息
     */
    fun showError(errorMsg: String) {
        ToastUtil.show(errorMsg)
    }

    /**
     * 显示信息
     */
    fun showMsg(msg: String) {
        ToastUtil.show(msg)
    }
    /**
     * 显示加载
     */
    fun showLoading() {

    }

    /**
     * 隐藏加载
     */
    fun hideLoading() {

    }


    fun startRouteActivity(path: String) {
        startRouteActivity(null, path, null)
    }

    fun startRouteActivity(
        activity:Activity?,
        path: String,
        bundle: Bundle? = null,
        requestCode: Int = -1,
        type: RouteType = RouteType.ACTIVITY
    ) {
        val postcard = ARouter.getInstance()
            .build(path)
            .with(bundle)

        postcard.type = type

        postcard.navigation(activity, requestCode)
    }

    fun startActivityClass(context: Context, activityClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(context, activityClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }


    fun launchOnBackground(function: suspend () -> Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            function.invoke()
        }
    }

}
