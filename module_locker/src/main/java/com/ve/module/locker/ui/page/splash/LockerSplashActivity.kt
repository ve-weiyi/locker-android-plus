package com.ve.module.locker.ui.page.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.LockerMainActivity
import com.ve.module.locker.common.config.LockerSpKey

import com.ve.module.locker.databinding.LockerActivitySplashBinding
import com.ve.module.locker.respository.http.bean.LoginVO
import com.ve.module.locker.ui.page.auth.LockerAuthActivity
import com.ve.module.locker.ui.page.auth.LockerLoginActivity


class LockerSplashActivity : BaseActivity<LockerActivitySplashBinding>() {

    private var alphaAnimation: AlphaAnimation? = null

    override fun useEventBus(): Boolean = false

    private val layout_splash by lazy { mBinding.layoutSplash }

    override fun initColor() {
        super.initColor()
//        layout_splash.setBackgroundColor(mThemeColor)
        mBinding.ivLogo.setColorFilter(mThemeColor)

    }

    fun jumpToMain() {
        val intent = Intent(this, LockerMainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun jumpToAuth() {
        val intent = Intent(this, LockerAuthActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun jumpToLogin() {
        val intent = Intent(this, LockerLoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun attachViewBinding(): LockerActivitySplashBinding {
        return LockerActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        val biometric=SpUtil.getBoolean(LockerSpKey.SP_KEY_BIOMETRICS)
        var isLogin=SpUtil.getValue(LockerSpKey.SP_KEY_LOGIN_STATE_KEY,false)
        val data=SpUtil.getValue(LockerSpKey.SP_KEY_LOGIN_DATA_KEY,LoginVO::class.java)

        LogUtil.msg(data.toString())
        alphaAnimation = AlphaAnimation(0.1F, 1.0F)
        alphaAnimation?.run {
            //持续时间
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
//                    jumpToMain()
                    //已经登录，跳转身份验证页面
//                    if(isLogin){
//                        if(!biometric){
//                            jumpToLogin()
//                        }else{
//                            jumpToAuth()
//                        }
//                    }else{
//                        jumpToMain()
//                    }
//                    jumpToMain()
                    LockerAuthActivity.start(mContext,LockerMainActivity::class.java.name)
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        layout_splash.startAnimation(alphaAnimation)
    }

}
