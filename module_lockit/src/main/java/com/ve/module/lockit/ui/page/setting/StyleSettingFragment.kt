package com.ve.module.lockit.ui.page.setting


import android.content.SharedPreferences
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.ve.lib.common.event.ColorEvent
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.view.widget.preference.IconPreference
import com.ve.module.lockit.R
import com.ve.module.lockit.common.config.LockitSpKey
import org.greenrobot.eventbus.EventBus

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project lockit-android
 */
class StyleSettingFragment :  BaseSettingFragment(){

    private lateinit var colorPreview: IconPreference

    override fun attachPreferenceResource(): Int {
        return R.xml.lockit_pref_setting_style
    }

    override fun initPreferenceView() {

        colorPreview=findPreference<IconPreference>(LockitSpKey.SP_KEY_THEME_COLOR)!!

        findPreference<Preference>(LockitSpKey.SP_KEY_NIGHT_MODE)?.onPreferenceClickListener = this

        findPreference<Preference>(LockitSpKey.SP_KEY_AUTO_NIGHT_MODE)?.onPreferenceClickListener = this

        findPreference<Preference>(LockitSpKey.SP_KEY_TEXT_SIZE)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_THEME_COLOR)?.onPreferenceClickListener = this
        findPreference<Preference>(LockitSpKey.SP_KEY_NAV_COLOR)?.onPreferenceClickListener = this

        setDefaultText()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "color"->{
                colorPreview.setView()
            }
            else -> {
                showMsg("功能未实现. key=$key  ")
            }
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference?.key) {
            /**
             * 其他设置
             */
            LockitSpKey.SP_KEY_THEME_COLOR -> {
                MaterialDialog(mContext).show {
                    title(text = "主题颜色")
                    colorChooser(
                        colors = ColorPalette.Primary,
                        subColors = ColorPalette.PrimarySub,
                        allowCustomArgb = true,
                        showAlphaSelector = true
                    ) { _, selectedColor ->
                        //设置主题颜色
                        SettingUtil.setColor(selectedColor)
                        EventBus.getDefault().post(ColorEvent(true))
                    }
                    positiveButton(text = "确认")
                    negativeButton(text = "取消")

                    lifecycleOwner(this@StyleSettingFragment)
                }
//                ColorChooserDialog.Builder(mSettingActivity as lockitSettingActivity, com.ve.lib.common.R.string.choose_theme_color)
//                    .backButton(com.ve.lib.common.R.string.back)
//                    .cancelButton(com.ve.lib.common.R.string.cancel)
//                    .doneButton(com.ve.lib.common.R.string.done)
//                    .customButton(com.ve.lib.common.R.string.custom)
//                    .presetsButton(com.ve.lib.common.R.string.back)
//                    .allowUserColorInputAlpha(false)
//                    .show()
                false
            }
            LockitSpKey.SP_KEY_AUTO_NIGHT_MODE -> {
                LockitSettingActivity.start(mContext,AutoNightModeFragment::class.java.name)
            }
            else->{
                showMsg("${preference?.title} 功能未实现.key=${preference?.key}")
            }
        }
        return false
    }

    private fun setDefaultText() {
        try {
            colorPreview = findPreference<IconPreference>(LockitSpKey.SP_KEY_THEME_COLOR)!!
            colorPreview.setView()
            val version = context?.resources?.getString(com.ve.lib.common.R.string.current_version).toString()
                .plus(
                    context?.packageManager?.getPackageInfo(
                        context?.packageName ?: "",
                        0
                    )?.versionName
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}