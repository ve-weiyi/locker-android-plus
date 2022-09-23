package com.ve.module.lockit.plus.ui.page

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.gyf.immersionbar.ImmersionBar
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.router.ARouterPath
import com.ve.module.lockit.plus.databinding.ActivitySplashBinding

/**
 * null：    表示对象为空
 * empty：表示对象为空或长度为0
 * blank： 表示对象为空或长度为0、空格字符串
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(){

    override fun attachViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    private var alphaAnimation: AlphaAnimation? = null
    override fun initialize(saveInstanceState: Bundle?) {
        ImmersionBar.with(this).init()
        alphaAnimation = AlphaAnimation(0.3F, 1.0F)
        alphaAnimation?.run {
            //持续时间
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    jumpToMain()
//                    jumpToLogin()
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        mBinding.ivLogo.startAnimation(alphaAnimation)
    }

    fun jumpToMain() {
        startActivity(ARouterPath.MAIN_HOME)
        finish()
    }

    fun jumpToLogin() {
        startActivity(ARouterPath.LOGIN)
        finish()
    }
}