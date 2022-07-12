package com.ve.module.android

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.bugly.Bugly
import com.ve.lib.application.BaseApplication
import com.ve.lib.application.BuildConfig
import com.ve.lib.common.config.AppConfig
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.vutils.AppContextUtils
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import java.util.*

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
    1.locker(下称"本产品"）是一款开源的隐私数据管理工具。应用实现个人银行账户及密码、邮件地址及密码、好友联系信息、web应用系统账户及密码等个人隐私信息的加密保存。应用设计初衷是交付华中科技大学本科生毕业设计作业，源码仅供学习，请勿将其用于其他商业用途。
    2.本产品不做任何担保。由于用户行为(Root等)导致用户信息泄漏或丢失，本产品免责。
    3.任何由于黑客攻击、计算机病毒侵入或发作、因政府管制而造成的暂时性关闭等影响网络正常经营的不可抗力而造成的个人资料泄露、丢失、被盗用或被窜改等，本产品均得免责。
    4.使用者因为违反本声明的规定而触犯中华人民共和国法律的，一切后果自己负责，本产品不承担任何责任。
    5.开发者不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息。locker也 ，不会将普通用户的信息用于商业用途。
    6.在使用过程中，locker需要获取手机的存储权限(导入及导出功能)、手机识别码 (用于locker用户统计)、 生物识别验证(用于指纹登录)及联网权限(检查更新)。
 */
class WazMainApplication:BaseApplication() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        if (BuildConfig.DEBUG) {
            ARouter.openLog(); // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        AppContextUtils.init(this)
        initTheme()

        Bugly.init(applicationContext, AppConfig.BUGLY_ID, false)

        LogUtil.e("Application init ")

        SpUtil.getAll().forEach{
            map->
            LogUtil.msg(map.toString())
        }
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
}