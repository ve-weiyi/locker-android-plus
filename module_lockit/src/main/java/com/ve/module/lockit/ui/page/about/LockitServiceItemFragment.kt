package com.ve.module.lockit.ui.page.about

import android.text.Html
import android.text.method.LinkMovementMethod
import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.R
import com.ve.module.lockit.databinding.LockitFragmentServiceItemBinding

/**
 * @Author  weiyi
 * @Date 2022/4/21
 * @Description  current project lockit-android
 */
class LockitServiceItemFragment :BaseVBFragment<LockitFragmentServiceItemBinding>(){
    override fun attachViewBinding(): LockitFragmentServiceItemBinding {
        return LockitFragmentServiceItemBinding.inflate(layoutInflater)
    }
    private val about_content by lazy {  mBinding.aboutContent}
    override fun initialize() {
        about_content.run {
            text = Html.fromHtml(getString(R.string.lockit_service_item))
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}