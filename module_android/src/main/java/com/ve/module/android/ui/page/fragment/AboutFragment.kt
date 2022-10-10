package com.ve.module.android.ui.page.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.ve.module.android.R
import com.ve.module.android.databinding.FragmentAboutBinding
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.lib.common.utils.SettingUtil

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>(){

    companion object {
        fun getInstance(bundle: Bundle): AboutFragment {
            val fragment = AboutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachViewBinding(): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(layoutInflater)
    }

    private val iv_logo by lazy { mBinding.ivLogo }

    private val about_version by lazy {  mBinding.aboutVersion}
    private val about_content by lazy {  mBinding.aboutContent}

    private fun setLogoBg() {
        val drawable = iv_logo.background as GradientDrawable
        drawable.setColor(SettingUtil.getColor())
        iv_logo.setBackgroundDrawable(drawable)
    }

    override fun initialize() {
        about_content.run {
            text = Html.fromHtml(getString(R.string.about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }

        val versionName = activity?.packageManager?.getPackageInfo(activity?.packageName ?: "", 0)?.versionName
        val versionStr = "${getString(R.string.app_name)} V${versionName}"
        about_version.text = versionStr

        setLogoBg()
    }
}