package com.ve.module.lockit.plus.ui.page.test

import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.plus.R
import com.ve.module.lockit.plus.databinding.FragmentHomeEmptyBinding

/**
 * @author waynie
 * @date 2022/10/11
 * @desc lockit-android
 */
class HomeEmptyFragment:BaseVBFragment<FragmentHomeEmptyBinding>() {
    
    override fun attachViewBinding(): FragmentHomeEmptyBinding {
        return FragmentHomeEmptyBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        mBinding.ivEndIcon.setImageResource(R.drawable.ic_help_feedback)
    }
}