package com.ve.module.lockit.ui.page.setting


import android.content.SharedPreferences

import androidx.preference.Preference
import com.ve.lib.common.utils.system.CacheDataUtil

import com.ve.module.lockit.R
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.ui.page.about.LockitAboutActivity

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project lockit-android
 */
class AboutSettingFragment :  BaseSettingFragment(){
    override fun attachPreferenceResource(): Int {
        return R.xml.lockit_pref_setting_about
    }

    override fun initPreferenceView() {
setDefaultText()
        findPreference<Preference>(LockitSpKey.SP_KEY_SCAN_QR_CODE)?.onPreferenceClickListener = this

        findPreference<Preference>(LockitSpKey.SP_KEY_APP_VERSION)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_APP_WEBSITE)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_UPDATE_LOG)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_SOURCE_CODE)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_COPYRIGHT)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_ABOUT_US)?.onPreferenceClickListener = this

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        showMsg("功能未实现. key=$key  ")
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            LockitSpKey.SP_KEY_APP_VERSION -> {

            }
            LockitSpKey.SP_KEY_APP_WEBSITE -> {

            }
            LockitSpKey.SP_KEY_UPDATE_LOG -> {

            }
            LockitSpKey.SP_KEY_SOURCE_CODE -> {

            }
            LockitSpKey.SP_KEY_COPYRIGHT -> {
                showAlertDialog(preference.title, preference.key)
            }
            LockitSpKey.SP_KEY_ABOUT_US -> {
                startActivity(mContext, LockitAboutActivity::class.java)
            }
            else->{
                showAlertDialog(preference?.title,preference?.key)
            }
        }
        return false
    }

    private fun setDefaultText() {
        try {
            findPreference<Preference>(LockitSpKey.SP_KEY_CLEAR_CACHE)?.summary=
                CacheDataUtil.getTotalCacheSize(requireContext())
            val version = context?.resources?.getString(com.ve.lib.application.R.string.current_version).toString()
                .plus(
                    context?.packageManager?.getPackageInfo(
                        context?.packageName ?: "",
                        0
                    )?.versionName
                )
            findPreference<Preference>(LockitSpKey.SP_KEY_APP_VERSION)!!.summary = version

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}