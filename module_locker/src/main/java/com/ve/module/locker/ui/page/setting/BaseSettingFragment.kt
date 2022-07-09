package com.ve.module.locker.ui.page.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ve.lib.common.vutils.ToastUtil

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 */
abstract class BaseSettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {

    lateinit var mContext: Context
    lateinit var mSettingActivity: LockerSettingActivity

    abstract fun attachPreferenceResource(): Int
    abstract fun initPreferenceView()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(attachPreferenceResource(), rootKey)
        setHasOptionsMenu(true)
        mContext = requireContext()
        mSettingActivity = activity as LockerSettingActivity

        initPreferenceView()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }


    /**
     * 显示加载
     */
    fun showAlertDialog(title: CharSequence?, content: String?) {
        AlertDialog.Builder(mContext)
            .setTitle(title)
            .setMessage(content)
            .setCancelable(true)
            .show()
    }

    /**
     * 显示信息
     */
    fun showMsg(msg: String) {
        ToastUtil.show(msg)
    }

    /**
     * 显示错误信息
     */
    fun showError(errorMsg: String) {
        ToastUtil.show(errorMsg)
    }

    fun startActivity(context: Context, activityClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(context, activityClass)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }
}