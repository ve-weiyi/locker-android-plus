package com.ve.lib.common.base.adapter

import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule


abstract class BaseSlideBindingAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null) :
    BaseBindingAdapter<T, VB>(data) , LoadMoreModule, DraggableModule ,UpFetchModule{

    init {

        //设置头部和尾部
        //setHeaderView(mBinding.root)
        //setFooterView(mBinding.root)
        //设置空布局,调用此方法前需要 recyclerView.adapter=MAdapter
//        setEmptyView(R.layout.empty_view)
        //开启加载动画,设置为 缩放显示
        animationEnable = true
        setAnimationWithDefault(AnimationType.AlphaIn)

    }


}