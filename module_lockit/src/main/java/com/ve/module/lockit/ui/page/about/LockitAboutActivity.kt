package com.ve.module.lockit.ui.page.about

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.lockit.R
import com.ve.module.lockit.databinding.LockitActivityAboutBinding

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
class LockitAboutActivity: BaseActivity<LockitActivityAboutBinding>() {
    override fun attachViewBinding(): LockitActivityAboutBinding {
        return LockitActivityAboutBinding.inflate(layoutInflater)
    }
    private val iv_logo by lazy { mBinding.ivLogo }

    private val about_version by lazy {  mBinding.aboutVersion}
    private val about_content by lazy {  mBinding.aboutContent}

    override fun initialize(saveInstanceState: Bundle?) {
        initToolbar(mBinding.extToolbar.toolbar, "关于", true)
        about_content.run {
            text = Html.fromHtml(getString(R.string.lockit_about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }

        val versionName = packageManager?.getPackageInfo(packageName ?: "", 0)?.versionName
        val versionStr = "${getString(R.string.lockit_app_name)} V${versionName}"
        about_version.text = versionStr

    }
}