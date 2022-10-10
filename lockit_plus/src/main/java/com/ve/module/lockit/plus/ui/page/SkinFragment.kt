package com.ve.module.lockit.plus.ui.page

import com.ve.lib.application.skin.factory.SkinFactoryActivity
import com.ve.lib.application.skin.set.SkinSetActivity
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.lockit.plus.databinding.FragmentSkinBinding

/**
 * @author waynie
 * @date 2022/9/27
 * @desc lockit-android
 */
class SkinFragment:BaseFragment<FragmentSkinBinding>() {
    override fun attachViewBinding(): FragmentSkinBinding {
        return FragmentSkinBinding.inflate(layoutInflater)
    }

    override fun initialize() {
        mBinding.demo1Theme.setOnClickListener {
            startActivity(mContext,SkinSetActivity::class.java)
        }
        mBinding.demo2Theme.setOnClickListener {
            startActivity(mContext,SkinFactoryActivity::class.java)
        }
        mBinding.demo3Theme.setOnClickListener {
            startActivity(mContext,SkinSetActivity::class.java)
        }
    }
}