package com.ve

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.mmkv.MMKV
import com.ve.lib.application.BaseApplication
import com.ve.lib.application.R
import com.ve.lib.common.ext.showToast
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.utils.AppContextUtil
import java.util.*

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/23
 */
class MainApplication : BaseApplication(){
    override fun onCreate() {
        super.onCreate()
        ARouter.openLog(); // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)

        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        AppContextUtil.init(this)

        initTheme()
        //初始化MMKV
        MMKV.initialize(this)

        modulesApplicationInit()
    }



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 安装 Tinker
        Beta.installTinker()
    }


    /**
     * 初始化主题
     */
    private fun initTheme() {

        if (SettingUtil.getIsAutoNightMode()) {
            //自动夜间模式开启时间
            val nightStartHour = SettingUtil.getNightStartHour().toInt()
            val nightStartMinute = SettingUtil.getNightStartMinute().toInt()
            val dayStartHour = SettingUtil.getDayStartHour().toInt()
            val dayStartMinute = SettingUtil.getDayStartMinute().toInt()

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val nightValue = nightStartHour * 60 + nightStartMinute
            val dayValue = dayStartHour * 60 + dayStartMinute
            val currentValue = currentHour * 60 + currentMinute

            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SettingUtil.setIsNightMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SettingUtil.setIsNightMode(false)
            }
        } else {
            // 获取当前的主题
            if (SettingUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private val upgradeStateListener = object : UpgradeStateListener {
        override fun onDownloadCompleted(isManual: Boolean) {
        }

        override fun onUpgradeSuccess(isManual: Boolean) {
        }

        override fun onUpgradeFailed(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_version_fail))
            }
        }

        override fun onUpgrading(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_version_ing))
            }
        }

        override fun onUpgradeNoVersion(isManual: Boolean) {
            if (isManual) {
                showToast(getString(R.string.check_no_version))
            }
        }
    }
}