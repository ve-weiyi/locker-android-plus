package com.ve.module.locker.ui.page.setting

import android.os.Bundle

import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.locker.databinding.LockerFragmentQrCodeBinding

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class QrCodeFragment : BaseFragment<LockerFragmentQrCodeBinding>(){

    companion object {
        fun getInstance(): QrCodeFragment = QrCodeFragment()
    }

    override fun attachViewBinding(): LockerFragmentQrCodeBinding {
        return LockerFragmentQrCodeBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {

    }
}