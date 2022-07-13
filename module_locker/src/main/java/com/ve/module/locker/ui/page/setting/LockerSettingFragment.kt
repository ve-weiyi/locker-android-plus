package com.ve.module.locker.ui.page.setting

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.ve.lib.common.event.RefreshHomeEvent
import com.ve.lib.common.utils.ImageLoader
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.widget.preference.IconPreference
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.lib.common.vutils.ToastUtil
import com.ve.module.locker.common.config.LockerSpKey
import com.ve.module.locker.respository.database.AppDataBase
import com.ve.module.locker.respository.http.bean.LoginVO
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.key.LockerKeyFragment
import com.ve.module.locker.ui.page.user.LockerUserInfoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.greenrobot.eventbus.EventBus

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 */
class LockerSettingFragment : BaseSettingFragment() {


    override fun attachPreferenceResource(): Int {
        return com.ve.module.locker.R.xml.locker_pref_settings
    }

    val spClickPreferenceKeyList = mutableListOf<String>(
        LockerSpKey.SP_KEY_ACCOUNT_SETTING,
        LockerSpKey.SP_KEY_STYLE_SETTING,
        LockerSpKey.SP_KEY_CACHE_SETTING,
        LockerSpKey.SP_KEY_ABOUT_SETTING,
        LockerSpKey.SP_KEY_AUTO_FILL,
        LockerSpKey.SP_KEY_RECRATE_DATABASE,
        LockerSpKey.SP_KEY_KEY_MANAGER
        )

    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>
    override fun initPreferenceView() {
        startActivityLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            LogUtil.msg(result.toString())
        }
        spClickPreferenceKeyList.forEach { key ->
            run {
                findPreference<Preference>(key)?.onPreferenceClickListener = this
            }
        }
        val userinfo=SpUtil.getValue(LockerSpKey.SP_KEY_LOGIN_DATA_KEY, LoginVO())

        findPreference<Preference>(LockerSpKey.SP_KEY_ACCOUNT_SETTING)?.apply {

            CoroutineScope(Dispatchers.IO).launch {
                val avatar=ImageLoader.loadPicture(mContext,userinfo.userDetailDTO.avatar)
                withContext(Dispatchers.Main) {
                    icon=avatar
                }
            }

//            lifecycleScope.launch{
//                withContext(Dispatchers.Main){
//                    icon=ImageLoader.loadPicture(mContext,userinfo.userDetailDTO.avatar)
//                }
//            }
            summary=userinfo.userDetailDTO.nickname
        }
    }

    private lateinit var colorPreview: IconPreference

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        LogUtil.msg("sharedPreferences $key")
        when (key) {
            LockerSpKey.SP_KEY_SHOW_TOP -> {
                val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
                // 通知首页刷新数据
                // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
                mainThreadHandler.postDelayed({
                    EventBus.getDefault().post(RefreshHomeEvent(true))
                }, 100)
            }
            LockerSpKey.SP_KEY_BIOMETRICS->{
                val switchPreference=findPreference<SwitchPreference>(key)!!
                val executor = ContextCompat.getMainExecutor(mContext)
                val biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            ToastUtil.showCenter("认证错误！\n" + "Authentication error: $errString!")
                            switchPreference.isChecked= SpUtil.getBoolean(key)
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            ToastUtil.showCenter("认证成功！\nAuthentication succeeded!")
                            SpUtil.setValue(key,switchPreference.isChecked)
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            ToastUtil.showCenter("认证失败！请重试")
                            switchPreference.isChecked= SpUtil.getBoolean(key)
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authentication required")
                    .setSubtitle("授权登录locker")
                    .setNegativeButtonText("取消")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }

            else -> {
                showMsg("功能未实现. key=$key  ")
            }
        }
    }


    override fun onPreferenceClick(preference: Preference?): Boolean {
        LogUtil.msg("sharedPreferences ${preference?.key}")
        when (preference?.key) {
            LockerSpKey.SP_KEY_KEY_MANAGER->{
                LockerContainerActivity.start(mContext,LockerKeyFragment::class.java,"秘钥管理")
            }
            LockerSpKey.SP_KEY_RECRATE_DATABASE->{
                DialogUtil.getConfirmDialog(
                    mContext,
                    "是否重置应用数据，该过程会删除已保存的密码和卡片。请在备份后谨慎操作。",
                    onOKClickListener = {
                        d,w->
                        SpUtil.setValue(LockerSpKey.SP_KEY_DATABASE_INIT,false)
                        AppDataBase.initDataBase()
                    },
                    onCancelClickListener = null
                ).show()
            }
            LockerSpKey.SP_KEY_STYLE_SETTING -> {
                LockerSettingActivity.start(mContext, StyleSettingFragment::class.java.name, "主题设置")
            }
            LockerSpKey.SP_KEY_CACHE_SETTING -> {
                LockerSettingActivity.start(mContext, CacheSettingFragment::class.java.name, "缓存设置")
            }
            LockerSpKey.SP_KEY_ACCOUNT_SETTING -> {
                startActivity(mContext,LockerUserInfoActivity::class.java)
            }
            LockerSpKey.SP_KEY_ABOUT_SETTING -> {
                LockerSettingActivity.start(mContext, AboutSettingFragment::class.java.name, "关于")
            }
            LockerSpKey.SP_KEY_AUTO_FILL->{
                //打开自动填充服务设置界面
                val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE)
                intent.data = Uri.parse("package:com.android.settings")
                startActivityLaunch.launch(intent)
            }
            else -> {
                showMsg("${preference?.title} 功能未实现.key=${preference?.key}")
            }
        }
        return false
    }
}