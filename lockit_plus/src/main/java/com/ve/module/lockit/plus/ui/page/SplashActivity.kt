package com.ve.module.lockit.plus.ui.page

import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.router.ARouterPath
import com.ve.module.lockit.plus.databinding.ActivitySplashBinding
import com.ve.module.lockit.plus.ui.page.test.EufyCleanNewActivity


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
//            jumpToMain()
            jumpToTest()
        }

        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        // Add a callback that's called when the splash screen is animating to
        // the app content.

    }

    private fun jumpToMain() {
        startRouteActivity(ARouterPath.MAIN_HOME)
        finish()
    }

    private fun jumpToLogin() {
        startRouteActivity(ARouterPath.LOGIN)
        finish()
    }

    private fun jumpToTest(){
        startActivityClass(mContext,EufyCleanNewActivity::class.java)
        finish()
    }
}