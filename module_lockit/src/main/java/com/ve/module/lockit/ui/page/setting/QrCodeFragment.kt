package com.ve.module.lockit.ui.page.setting

import android.os.Bundle

import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.lockit.databinding.LockitFragmentQrCodeBinding

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class QrCodeFragment : BaseFragment<LockitFragmentQrCodeBinding>(){

    companion object {
        fun getInstance(): QrCodeFragment = QrCodeFragment()
    }

    override fun attachViewBinding(): LockitFragmentQrCodeBinding {
        return LockitFragmentQrCodeBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {

    }
}