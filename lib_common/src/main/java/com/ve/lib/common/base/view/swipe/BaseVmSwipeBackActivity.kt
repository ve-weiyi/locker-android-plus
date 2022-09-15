package com.ve.lib.common.base.view.swipe

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.base.view.vm.BaseVmActivity

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/18
 */
 abstract class BaseVmSwipeBackActivity <VB : ViewBinding,VM : BaseViewModel> :
    BaseVmActivity<VB, VM>() , ISwipeView {

    override lateinit var swipeBackManager: SwipeBackManager

    override fun initView(savedInstanceState: Bundle?) {
        swipeBackManager=attachSwipeManager()
    }


}