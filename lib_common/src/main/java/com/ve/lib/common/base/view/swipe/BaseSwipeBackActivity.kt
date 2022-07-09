package com.ve.lib.common.base.view.swipe

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.view.vm.BaseActivity

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/18
 */
abstract class BaseSwipeBackActivity <VB : ViewBinding,> :
    BaseActivity<VB>() , ISwipeView{

    override lateinit var swipeBackManager: SwipeBackManager

    override fun initialize(saveInstanceState: Bundle?) {
        swipeBackManager=attachSwipeManager()
    }



}