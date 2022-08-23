package com.ve.module.lockit.ui.page

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseBinderAdapter
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.sp.SpUtil
import com.ve.module.lockit.LockitMainActivity
import com.ve.module.lockit.R
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
