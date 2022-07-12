package com.ve.module.android.ui.page.setting

import android.os.Bundle
import com.ve.module.android.databinding.FragmentQrCodeBinding
import com.ve.lib.common.base.view.vm.BaseFragment

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class QrCodeFragment : BaseFragment<FragmentQrCodeBinding>(){

    companion object {
        fun getInstance(): QrCodeFragment = QrCodeFragment()
    }

    override fun attachViewBinding(): FragmentQrCodeBinding {
        return FragmentQrCodeBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {

    }
}