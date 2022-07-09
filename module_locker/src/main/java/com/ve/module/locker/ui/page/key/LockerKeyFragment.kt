package com.ve.module.locker.ui.page.key

import android.os.Bundle
import android.text.Html
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.locker.R
import com.ve.module.locker.databinding.LockerFragmentKeyBinding
import com.ve.module.locker.utils.PasswordUtils

/**
 * @Author  weiyi
 * @Date 2022/5/13
 * @Description  current project locker-android
 */
class LockerKeyFragment:BaseFragment<LockerFragmentKeyBinding>() {
    override fun attachViewBinding(): LockerFragmentKeyBinding {
        return LockerFragmentKeyBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        mBinding.tvKeyTip.apply {
            text= Html.fromHtml(getString(R.string.locker_key_tip))
        }
        mBinding.tvKeyTip1.apply {
            text= Html.fromHtml(getString(R.string.locker_key_tip_1))
        }
        mBinding.etKey.setText(PasswordUtils.generatePassword(32,true,true,true,true))
    }
}