package com.ve.module.android.ui.page.setting

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ve.lib.common.R
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.page.fragment.AboutFragment
import com.ve.lib.common.widget.preference.IconPreference
import com.ve.lib.common.event.RefreshHomeEvent
import com.ve.lib.common.ext.showSnackMsg
import com.ve.lib.common.ext.showToast
import com.ve.lib.common.utils.system.CacheDataUtil
import org.greenrobot.eventbus.EventBus

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var context: SettingsActivity? = null
    private lateinit var colorPreview: IconPreference

    companion object {


        fun getInstance(bundle: Bundle): SettingsFragment {
            val fragment = SettingsFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun getInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(com.ve.module.android.R.xml.pref_settings, rootKey)

        setHasOptionsMenu(true)
        context = activity as SettingsActivity

        colorPreview = findPreference("theme_color")!!
        setDefaultText()
        // 在主线程创建 Handler 对象
        val mainThreadHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 3) {
                }
            }
        }

        findPreference<Preference>("switch_show_top")
            ?.setOnPreferenceChangeListener { preference, newValue ->
                // 通知首页刷新数据
                // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
                mainThreadHandler.postDelayed({
                    EventBus.getDefault().post(RefreshHomeEvent(true))
                }, 100)
                true
            }

        findPreference<Preference>("auto_nightMode")?.setOnPreferenceClickListener {
            (context as SettingsActivity).startWithFragment(
                AutoNightModeFragment::class.java.name,
                null,
                null,
                0,
                null
            )
            true
        }

        findPreference<Preference>("text_size")?.setOnPreferenceClickListener {
            //Beta.checkUpgrade()
            AlertDialog.Builder((context as SettingsActivity))
                .setTitle(it.title)
                .setMessage("功能未实现")
                .setCancelable(true)
                .show()
            false
        }

        findPreference<IconPreference>("theme_color")?.setOnPreferenceClickListener {

            false
        }

        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            CacheDataUtil.clearAllCache((context as SettingsActivity))
            showSnackMsg(getString(R.string.clear_cache_successfully))
            setDefaultText()
            false
        }

        findPreference<Preference>("scanQrCode")?.setOnPreferenceClickListener {
            CommonActivity.start(requireContext(), "扫码下载", QrCodeFragment::class.java.name)
            false
        }

        try {
            val version = context?.resources?.getString(R.string.current_version).toString()
                .plus(
                    context?.packageManager?.getPackageInfo(
                        context?.packageName ?: "",
                        0
                    )?.versionName
                )
            findPreference<Preference>("version")!!.summary = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
//
        findPreference<Preference>("version")?.setOnPreferenceClickListener {
            //Beta.checkUpgrade()
            AlertDialog.Builder((context as SettingsActivity))
                .setTitle("version")
                .setMessage("功能未实现")
                .setCancelable(true)
                .show()
            false
        }

        findPreference<Preference>("official_website")?.setOnPreferenceClickListener {
            ArticleDetailActivity.start(activity, "官方网站", getString(R.string.official_website_url))
            false
        }

        findPreference<Preference>("changelog")?.setOnPreferenceClickListener {
            // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.changelog_url))))
            ArticleDetailActivity.start(activity, "更新日志", getString(R.string.changelog_url))
            false
        }

        findPreference<Preference>("sourceCode")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                // context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.source_code_url))))
                ArticleDetailActivity.start(activity, "源代码", getString(R.string.source_code_url))
                false
            }

        findPreference<Preference>("copyRight")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AlertDialog.Builder((context as SettingsActivity))
                    .setTitle(R.string.copyright)
                    .setMessage(R.string.copyright_content)
                    .setCancelable(true)
                    .show()
                false
            }

        findPreference<Preference>("about_us")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                CommonActivity.start(requireContext(), "关于", AboutFragment::class.java.name)
                false
            }
    }

    private fun setDefaultText() {
        try {
            findPreference<Preference>("clearCache")?.summary =
                CacheDataUtil.getTotalCacheSize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (key == "color") {
            colorPreview.setView()
        }
    }


}