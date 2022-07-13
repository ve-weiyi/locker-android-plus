package com.ve.module.locker.ui.page.setting


import android.content.SharedPreferences

import androidx.preference.Preference
import com.ve.lib.common.utils.CacheDataUtil

import com.ve.module.locker.R
import com.ve.module.locker.common.config.LockerSpKey
import com.ve.module.locker.ui.page.about.LockerAboutActivity

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 */
class AboutSettingFragment :  BaseSettingFragment(){
    override fun attachPreferenceResource(): Int {
        return R.xml.locker_pref_setting_about
    }

    override fun initPreferenceView() {
setDefaultText()
        findPreference<Preference>(LockerSpKey.SP_KEY_SCAN_QR_CODE)?.onPreferenceClickListener = this

        findPreference<Preference>(LockerSpKey.SP_KEY_APP_VERSION)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_APP_WEBSITE)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_UPDATE_LOG)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_SOURCE_CODE)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_COPYRIGHT)?.onPreferenceClickListener = this
        findPreference<Preference>(LockerSpKey.SP_KEY_ABOUT_US)?.onPreferenceClickListener = this

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        showMsg("功能未实现. key=$key  ")
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            LockerSpKey.SP_KEY_APP_VERSION -> {

            }
            LockerSpKey.SP_KEY_APP_WEBSITE -> {

            }
            LockerSpKey.SP_KEY_UPDATE_LOG -> {

            }
            LockerSpKey.SP_KEY_SOURCE_CODE -> {

            }
            LockerSpKey.SP_KEY_COPYRIGHT -> {
                showAlertDialog(preference.title, preference.key)
            }
            LockerSpKey.SP_KEY_ABOUT_US -> {
                startActivity(mContext, LockerAboutActivity::class.java)
            }
            else->{
                showAlertDialog(preference?.title,preference?.key)
            }
        }
        return false
    }

    private fun setDefaultText() {
        try {
            findPreference<Preference>(LockerSpKey.SP_KEY_CLEAR_CACHE)?.summary=
                CacheDataUtil.getTotalCacheSize(requireContext())
            val version = context?.resources?.getString(com.ve.lib.application.R.string.current_version).toString()
                .plus(
                    context?.packageManager?.getPackageInfo(
                        context?.packageName ?: "",
                        0
                    )?.versionName
                )
            findPreference<Preference>(LockerSpKey.SP_KEY_APP_VERSION)!!.summary = version

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}