package com.ve.module.locker.ui.page.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.StatusBarUtil
import com.ve.lib.common.vutils.ToastUtil
import com.ve.module.locker.LockerMainActivity
import com.ve.module.locker.databinding.LockerActivityAuthBinding


class LockerAuthActivity : BaseActivity<LockerActivityAuthBinding>(){
    override fun attachViewBinding(): LockerActivityAuthBinding {
        return LockerActivityAuthBinding.inflate(layoutInflater)
    }

    private fun jumpToMain() {
        val intent = Intent(this, LockerMainActivity::class.java)
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

    override fun initColor() {
        super.initColor()
        //沉浸式状态栏
        StatusBarUtil.setColor(this, Color.WHITE, 0)
    }

    override fun initialize(saveInstanceState: Bundle?) {


        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    ToastUtil.showCenter("认证错误！\n" +
                            "Authentication error: $errString!")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    ToastUtil.showCenter("认证成功！\nAuthentication succeeded!")
                    jumpToMain()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    ToastUtil.showCenter("认证失败！请重试")
                }
            })
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication required")
            .setSubtitle("授权登录locker")
            .setNegativeButtonText("取消")
            .build()
        biometricPrompt.authenticate(promptInfo)
        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        mBinding.layoutAuth.setOnclickNoRepeatListener{
            biometricPrompt.authenticate(promptInfo)
        }

        mBinding.tvLoginUsePassword.setOnclickNoRepeatListener{
            jumpToLogin()
        }
    }

}