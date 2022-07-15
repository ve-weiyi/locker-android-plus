package com.ve.module.lockit.ui.page.auth.strategy.qq

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.tencent.connect.UserInfo
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.ext.showToast
import com.ve.module.lockit.ui.page.auth.LockitLoginActivity
import org.json.JSONObject

open class BaseUiListener(private val mTencent: Tencent) : DefaultUiListener() {

    private val kv = MMKV.defaultMMKV()

    override fun onComplete(response: Any?) {
        if (response == null) {
            "返回为空,登录失败".showToast()
            return
        }
        val jsonResponse = response as JSONObject
        if (jsonResponse.length() == 0) {
            "返回为空,登录失败".showToast()
            return
        }
        kv.encode("qq_login",response.toString())
        "登录成功".showToast()
        doComplete(response)
        getQQInfo()
        val intent = Intent(BaseApplication.context, LockitLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(BaseApplication.context,intent,null)
    }

    private fun doComplete(values: JSONObject?) {
        val gson = Gson()
        val qqLogin = gson.fromJson(values.toString(), QQLogin::class.java)
        mTencent.setAccessToken(qqLogin.access_token, qqLogin.expires_in.toString())
        mTencent.openId = qqLogin.openid

    }
    override fun onError(e: UiError) {

    }

    override fun onCancel() {
        "取消登录".showToast()
    }

    private fun getQQInfo(){
        val qqToken = mTencent.qqToken
        val info = UserInfo(BaseApplication.context,qqToken)
        info.getUserInfo(object : BaseUiListener(mTencent){
            override fun onComplete(response: Any?){
                kv.encode("qq_info",response.toString())
            }
        })
    }
}