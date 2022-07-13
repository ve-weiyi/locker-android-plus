package com.ve.module.locker.ui.page.setting


import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper

import androidx.preference.Preference
import com.ve.lib.common.event.RefreshHomeEvent
import com.ve.lib.common.utils.CacheDataUtil

import com.ve.module.locker.R
import com.ve.module.locker.common.config.LockerSpKey
import org.greenrobot.eventbus.EventBus

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 */
class CacheSettingFragment :  BaseSettingFragment(){
    override fun attachPreferenceResource(): Int {
        return R.xml.locker_pref_setting_cache
    }

    override fun initPreferenceView() {
        setDefaultText()

        findPreference<Preference>(LockerSpKey.SP_KEY_SHOW_TOP)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_NO_PHOTO)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_CLEAR_CACHE)?.onPreferenceClickListener = this

    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            LockerSpKey.SP_KEY_SHOW_TOP-> {
                val mainThreadHandler: Handler =Handler(Looper.getMainLooper())
                // 通知首页刷新数据
                // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
                mainThreadHandler.postDelayed({
                    EventBus.getDefault().post(RefreshHomeEvent(true))
                }, 100)
            }
            else -> {
                showMsg("功能未实现. key=$key  ")
            }
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            LockerSpKey.SP_KEY_CLEAR_CACHE -> {
                CacheDataUtil.clearAllCache(mContext)
                showMsg(getString(com.ve.lib.application.R.string.clear_cache_successfully))
                setDefaultText()
            }
            else->{
                showMsg("${preference?.title} 功能未实现.key=${preference?.key}")
            }
        }
        return false
    }

    private fun setDefaultText() {
        try {
            findPreference<Preference>(LockerSpKey.SP_KEY_CLEAR_CACHE)?.summary=
                CacheDataUtil.getTotalCacheSize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}