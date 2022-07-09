package com.ve.module.locker.utils

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ve.lib.common.vutils.ToastUtil

/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project locker-android
 */
object AuthUtils {

    fun showBiometricAuth(fragment: Fragment){
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())
        val biometricPrompt = BiometricPrompt(fragment, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    ToastUtil.showCenter("认证错误！\n" + "Authentication error: $errString!")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    ToastUtil.showCenter("认证成功！\nAuthentication succeeded!")
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
    }
}