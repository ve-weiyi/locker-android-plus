package com.ve.module.lockit.plus.ui.page.test

import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.lockit.plus.databinding.FragmentHomeTestBinding

/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 */
class HomeTestFragment:BaseFragment<FragmentHomeTestBinding>() {
    override fun attachViewBinding(): FragmentHomeTestBinding {
        return FragmentHomeTestBinding.inflate(layoutInflater)
    }

    override fun initialize() {

    }
}