package com.ve.module.locker.ui.page.about

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.locker.R
import com.ve.module.locker.databinding.LockerFragmentServiceItemBinding

/**
 * @Author  weiyi
 * @Date 2022/4/21
 * @Description  current project locker-android
 */
class LockerServiceItemFragment :BaseFragment<LockerFragmentServiceItemBinding>(){
    override fun attachViewBinding(): LockerFragmentServiceItemBinding {
        return LockerFragmentServiceItemBinding.inflate(layoutInflater)
    }
    private val about_content by lazy {  mBinding.aboutContent}
    override fun initialize(saveInstanceState: Bundle?) {
        about_content.run {
            text = Html.fromHtml(getString(R.string.locker_service_item))
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}