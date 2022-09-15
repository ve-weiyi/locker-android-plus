package com.ve.module.lockit.ui.page.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.ui.StatusBarUtil
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.sp.SpUtil
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.module.lockit.LockitMainActivity
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.databinding.LockitActivityAuthBinding


class LockitAuthActivity : BaseActivity<LockitActivityAuthBinding>(){

    companion object{
        const val NEXT_ACTIVITY_KEY: String = "next_activity_key"
        const val NEXT_ACTIVITY_DATA_KEY: String = "next_activity_data_key"
        fun start(context: Context, activityClassName: String, activityBundle: Bundle? = null) {
            Intent(context, LockitAuthActivity::class.java).run {
                putExtra(NEXT_ACTIVITY_KEY, activityClassName)
                putExtra(NEXT_ACTIVITY_DATA_KEY, activityBundle)

                LogUtil.msg("start to "+activityClassName)
                context.startActivity(this)
            }
        }
    }

    override fun attachViewBinding(): LockitActivityAuthBinding {
        return LockitActivityAuthBinding.inflate(layoutInflater)
    }

    private fun jumpToMain() {
        val intent = Intent(this, LockitMainActivity::class.java)
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


    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var activityClassName: String
    private lateinit var activityBundle: Bundle

    override fun initialize(saveInstanceState: Bundle?) {
        activityClassName=intent.getStringExtra(NEXT_ACTIVITY_KEY)?: ""
        activityBundle=intent.getBundleExtra(NEXT_ACTIVITY_DATA_KEY)?: Bundle()


        val biometric= SpUtil.getBoolean(LockitSpKey.SP_KEY_BIOMETRICS)
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
            .setSubtitle("授权登录lockit")
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