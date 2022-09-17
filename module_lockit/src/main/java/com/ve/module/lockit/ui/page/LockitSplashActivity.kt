package com.ve.module.lockit.ui.page

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.system.SpUtil
import com.ve.module.lockit.LockitMainActivity
import com.ve.module.lockit.common.config.LockitSpKey

import com.ve.module.lockit.databinding.LockitActivitySplashBinding
import com.ve.module.lockit.respository.http.bean.LoginDTO
import com.ve.module.lockit.ui.page.auth.LockitAuthActivity
import com.ve.module.lockit.ui.page.auth.LockitLoginActivity


class LockitSplashActivity : AppCompatActivity(){

    private var alphaAnimation: AlphaAnimation? = null

    lateinit var mBinding: LockitActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = LockitActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initialize(savedInstanceState)
    }

     fun initialize(saveInstanceState: Bundle?) {
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
                    jumpToMain()
//                    jumpToLogin()
//                    LockitAuthActivity.start(mContext,LockitMainActivity::class.java.name)
                }

                override fun onAnimationStart(p0: Animation?) {
                }
            })
        }
        mBinding.layoutSplash.startAnimation(alphaAnimation)
    }


    fun jumpToMain() {
        val intent = Intent(this, LockitMainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun jumpToAuth() {
        val intent = Intent(this, LockitAuthActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun jumpToLogin() {
        val intent = Intent(this, LockitLoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


}
