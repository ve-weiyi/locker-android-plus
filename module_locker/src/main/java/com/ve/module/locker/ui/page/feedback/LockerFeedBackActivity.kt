package com.ve.module.locker.ui.page.feedback

import android.os.Bundle
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.locker.databinding.LockerActivityFeedbackBinding

/**
 * @Author  weiyi
 * @Date 2022/4/18
 * @Description  current project locker-android
 */
class LockerFeedBackActivity:BaseActivity<LockerActivityFeedbackBinding>() {
    override fun attachViewBinding(): LockerActivityFeedbackBinding {
        return LockerActivityFeedbackBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
         initToolbar(mBinding.extToolbar.toolbar,"意见反馈")
    }
}