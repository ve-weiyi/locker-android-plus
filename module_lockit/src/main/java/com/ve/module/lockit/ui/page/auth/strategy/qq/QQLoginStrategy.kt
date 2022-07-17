package com.ve.module.lockit.ui.page.auth.strategy.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.ext.showToast
import com.ve.lib.common.vutils.LogUtil

/**
 * @Author  weiyi
 * @Date 2022/7/15
 * @Description  current project lockit-android
 * https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650261649&idx=1&sn=4b750c596b0c02b56f5aa0cee806037d&chksm=886339febf14b0e8ebaa7202a0801bca9e98e566c8f1a9f6524e1aea2ed51907f6dd7c969ac9&scene=27
 */
object QQLoginStrategy {

    private val kv = MMKV.defaultMMKV()

    var mQQUserInfo:QQUserInfo ?=null

    val mTencent: Tencent by lazy {
        Tencent.createInstance(
            "102017330",
            BaseApplication.context,
            "com.tencent.login.fileprovider"
        )
    }

    init{
        //初始化配置
        Tencent.setIsPermissionGranted(true, Build.MODEL)
        Tencent.resetTargetAppInfoCache()
        Tencent.resetQQAppInfoCache()
        Tencent.resetTimAppInfoCache()

        LogUtil.msg()
    }

    fun checkLogin(activity: Activity,listener: IUiListener) {
        //检查是否已有数据
        kv.decodeString("qq_login")?.let {
            val gson = Gson()
            val qqLogin = gson.fromJson(it, QQLogin::class.java)
            mTencent.setAccessToken(qqLogin.access_token, qqLogin.expires_in.toString())
            mTencent.openId = qqLogin.openid
        }

        mTencent.checkLogin(listener)
    }

    fun doLogin(activity: Activity,listener: IUiListener) {
        if (mTencent.isSessionValid) {
            LogUtil.msg("已登录")
        }

        loginOut(activity)
        // 1.当前的context  2.权限，一般选择all即可  3.监听回调
        val result = mTencent.login(activity, "all", listener)
        when (result) {
            0 -> "正常登录".showToast()
            1 -> "开始登录".showToast()
            -1 -> {
                "登录异常".showToast()
                LogUtil.msg("执行强制下线操作即可。")
                mTencent.logout(activity)
            }
            2 -> {
                "使用H5登陆或显示下载页面".showToast()
                LogUtil.msg("通常情况下是未安装QQ等软件导致的，这种情况无需处理，SDK自动封装好了，这种情况会自动跳转QQ下载界面。同样的有出现UIListener就需要调用回调进行数据的传输。")
            }
            else -> "出错".showToast()
        }
    }

    fun loginOut(context: Context) {
        mTencent.logout(context)
        kv.remove("qq_login")
    }

    /**
     * 保存token
     */
    fun getQQInfo(qqLogin: QQLogin,after:(QQUserInfo)->Unit) {
        mTencent.setAccessToken(qqLogin.access_token, qqLogin.expires_in.toString())
        mTencent.openId = qqLogin.openid


        val info = UserInfo(BaseApplication.context,mTencent.qqToken)
        info.getUserInfo(object : QQUiListener(){
            override fun onComplete(response: Any?){
                kv.encode("qq_info",response.toString())
                LogUtil.msg(response)
                after.invoke(JSON.parseObject(response.toString(),QQUserInfo::class.java))
            }
        })
    }

    fun onLoginResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //腾讯QQ回调
        Tencent.onActivityResultData(requestCode, resultCode, data, QQUiListener())
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, QQUiListener())
            }
        }
    }
}