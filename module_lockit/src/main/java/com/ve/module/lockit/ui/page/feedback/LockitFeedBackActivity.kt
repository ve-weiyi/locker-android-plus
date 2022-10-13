package com.ve.module.lockit.ui.page.feedback

import com.ve.lib.common.base.view.vm.BaseVBActivity
import com.ve.module.lockit.databinding.LockitActivityFeedbackBinding

/**
 * @Author  weiyi
 * @Date 2022/4/18
 * @Description  current project lockit-android
 */
class LockitFeedBackActivity:BaseVBActivity<LockitActivityFeedbackBinding>() {
    override fun attachViewBinding(): LockitActivityFeedbackBinding {
        return LockitActivityFeedbackBinding.inflate(layoutInflater)
    }

    override fun initialize() {
         initToolbar(mBinding.extToolbar.toolbar,"意见反馈")
    }
}