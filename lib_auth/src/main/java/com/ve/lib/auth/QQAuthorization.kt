package com.ve.lib.auth

import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.Platform.ShareParams
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.PlatformDb
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import com.mob.MobSDK
import com.ve.lib.common.utils.log.LogUtil

/**
 * @Author  weiyi
 * @Date 2022/7/16
 * @Description  current project lockit-android
 */
object QQAuthorization{

    init{
        MobSDK.submitPolicyGrantResult(true,null);
    }


    fun login() {
        LogUtil.msg(QQ.NAME)
        //初始化具体的平台
        val platform: Platform = ShareSDK.getPlatform(QQ.NAME)
        //设置false表示使用SSO授权方式
        platform.SSOSetting(false)

        //回调信息
        //可以在这里获取基本的授权返回的信息
        platform.platformActionListener = object : PlatformActionListener {
            /**
             * 登录成功了
             * @param platform
             * @param i
             * @param hashMap
             */
            override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String?, Any?>?) {
                //登录成功了

                //就可以获取到昵称，头像，OpenId
                //该方法回调不是在主线程

                //从数据库获取信息
                //也可以通过user参数获取
                val db: PlatformDb = platform.getDb()
                val nickname: String = db.getUserName()
                val avatar: String = db.getUserIcon()
                val openId: String = db.getUserId()
                LogUtil.d("other login success:" + nickname + "," + avatar + "," + openId + "," )

            }

            /**
             * 登录失败了
             * @param platform
             * @param i
             * @param throwable
             */
            override fun onError(platform: Platform?, i: Int, throwable: Throwable) {
                LogUtil.d("other login error:" + throwable.message + "," )

            }

            /**
             * 取消登录了
             * @param platform
             * @param i
             */
            override fun onCancel(platform: Platform?, i: Int) {
                LogUtil.d( "other login cancel:" + i + "," )
            }
        }
        val shareParams = ShareParams()
        shareParams.text = "标题"
        shareParams.title ="标题"
        shareParams.titleUrl = "http://download.sdk.mob.com/2021/01/25/16/16115618066761.02.html"
        shareParams.shareType = Platform.SHARE_WEBPAGE

        platform.share(shareParams)
        //authorize与showUser单独调用一个即可
        //授权并获取用户信息
        platform.showUser(null)
    }
}