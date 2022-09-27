package com.ve.module.lockit.plus.ui.page

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gyf.immersionbar.ImmersionBar
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.router.ARouterPath
import com.ve.module.lockit.plus.databinding.ActivitySplashBinding


/**
 * null：    表示对象为空
 * empty：表示对象为空或长度为0
 * blank： 表示对象为空或长度为0、空格字符串
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private lateinit var splashScreen: SplashScreen
    override fun attachViewBinding(): ActivitySplashBinding {
        // Handle the splash screen transition.
        splashScreen = installSplashScreen()
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.iconAnimationDurationMillis
            jumpToMain()
        }
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        // Add a callback that's called when the splash screen is animating to
        // the app content.

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