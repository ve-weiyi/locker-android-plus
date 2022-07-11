package com.ve.module.locker.ui.page.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.LockerMainActivity
import com.ve.module.locker.common.config.LockerConstant
import com.ve.module.locker.common.config.SettingConstant

import com.ve.module.locker.databinding.LockerActivitySplashBinding
import com.ve.module.locker.model.http.model.LoginVO
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
        val biometric=SpUtil.getBoolean(SettingConstant.SP_KEY_BIOMETRICS)
        var isLogin=SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_STATE_KEY,false)
        val data=SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_DATA_KEY,LoginVO())

        LogUtil.msg(data.toString())
        alphaAnimation = AlphaAnimation(0.3F, 1.0F)
        alphaAnimation?.run {
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
//                    jumpToMain()
                    if(biometric){
                        jumpToAuth()
                    }else{
                        jumpToMain()
                    }

                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        layout_splash.startAnimation(alphaAnimation)
    }

}
