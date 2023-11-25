package com.ve.module.lockit.plus.ui.page.drawer

import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.plus.databinding.FragmentDrawerBinding

/**
 * @author waynie
 * @date 2022/9/16
 * @desc lockit-android
 */
class DrawerFragment:BaseVBFragment<FragmentDrawerBinding>() {
    override fun attachViewBinding(): FragmentDrawerBinding {
        return FragmentDrawerBinding.inflate(layoutInflater)
    }

    override fun initialize() {

    }
}