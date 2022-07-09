package com.ve.module.locker.ui.page.message

import android.os.Bundle
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.locker.databinding.LockerFragmentMessageBinding

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockerMessageFragment: BaseFragment<LockerFragmentMessageBinding>() {
    override fun attachViewBinding(): LockerFragmentMessageBinding {
        return LockerFragmentMessageBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {

    }
}