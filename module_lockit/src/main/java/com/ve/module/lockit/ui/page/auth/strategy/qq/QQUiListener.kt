package com.ve.module.lockit.ui.page.auth.strategy.qq

import com.alibaba.fastjson.JSON
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.UiError
import com.ve.lib.common.ext.showToast
import com.ve.lib.common.utils.log.LogUtil

open class QQUiListener() : DefaultUiListener() {

    private val kv = MMKV.defaultMMKV()

    /**
     * 登录完成
     */
    override fun onComplete(response: Any?) {
        if (response == null) {
            "返回为空,登录失败".showToast()
            return
        }

        "登录成功".showToast()
        LogUtil.msg(response)

        val qqLogin = JSON.parseObject(response.toString(), QQLogin::class.java)
        kv.encode("qq_login",qqLogin.toString())

        afterGetUserInfo(qqLogin)
    }

    open fun afterGetUserInfo(info: QQLogin) {

    }
    /**
     * 登录失败
     */
    override fun onError(e: UiError) {
        "登录已过期，请重新登录".showToast()
        LogUtil.msg()
    }

    /**
     * 登录警告
     */
    override fun onWarning(p0: Int) {
        "登录警告".showToast()
        LogUtil.msg()
    }
    /**
     * 登录取消
     */
    override fun onCancel() {
        "登录取消".showToast()
        LogUtil.msg()
    }
}