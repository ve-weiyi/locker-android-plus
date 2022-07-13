package com.ve.module.locker.ui.page.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.StatusBarUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.lib.common.vutils.ToastUtil
import com.ve.module.locker.LockerMainActivity
import com.ve.module.locker.common.config.LockerSpKey
import com.ve.module.locker.databinding.LockerActivityAuthBinding
import com.ve.module.locker.ui.page.container.LockerContainerActivity


class LockerAuthActivity : BaseActivity<LockerActivityAuthBinding>(){

    companion object{
        const val NEXT_ACTIVITY_KEY: String = "next_activity_key"
        const val NEXT_ACTIVITY_DATA_KEY: String = "next_activity_data_key"
        fun start(context: Context, activityClassName: String, activityBundle: Bundle? = null) {
            Intent(context, LockerAuthActivity::class.java).run {
                putExtra(NEXT_ACTIVITY_KEY, activityClassName)
                putExtra(NEXT_ACTIVITY_DATA_KEY, activityBundle)

                LogUtil.msg("start to "+activityClassName)
                context.startActivity(this)
            }
        }
    }

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

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var activityClassName: String
    private lateinit var activityBundle: Bundle

    override fun initialize(saveInstanceState: Bundle?) {
        activityClassName=intent.getStringExtra(NEXT_ACTIVITY_KEY)?: ""
        activityBundle=intent.getBundleExtra(NEXT_ACTIVITY_DATA_KEY)?: Bundle()


        val biometric= SpUtil.getBoolean(LockerSpKey.SP_KEY_BIOMETRICS)
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                ToastUtil.showCenter("认证错误！\n" + "Authentication error: $errString!")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                ToastUtil.showCenter("认证成功！\n" + "Authentication succeeded!")

                finish()
                if(activityClassName.isEmpty()){
                    LogUtil.msg()
                    finish()
                }else{
                    LogUtil.msg()
                    startActivity(mContext, Class.forName(activityClassName),activityBundle)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                ToastUtil.showCenter("认证失败！请重试")
            }
        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
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

    fun showFingerprintAuth(){
        biometricPrompt.authenticate(promptInfo)
    }
}