package com.ve.module.android.ui.page.splash

import android.content.Intent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.android.WazMainActivity
import com.ve.module.android.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var alphaAnimation: AlphaAnimation? = null

    override fun useEventBus(): Boolean = false

    private val layout_splash by lazy { mBinding.layoutSplash }


    fun jumpToMain() {
        val intent = Intent(this, WazMainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun attachViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        alphaAnimation = AlphaAnimation(0.3F, 1.0F)
        alphaAnimation?.run {
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    jumpToMain()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        layout_splash.startAnimation(alphaAnimation)
    }

}
