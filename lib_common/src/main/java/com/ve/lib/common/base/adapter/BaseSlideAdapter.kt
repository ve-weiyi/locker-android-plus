package com.ve.lib.common.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder



abstract class BaseSlideAdapter<T,  VH : BaseViewHolder>(layoutResId: Int, data: MutableList<T>? = null) :
    BaseQuickAdapter<T, VH>(layoutResId,data),LoadMoreModule,DraggableModule , UpFetchModule {

    init {

        //设置头部和尾部
        //setHeaderView(mBinding.root)
        //setFooterView(mBinding.root)
        //设置空布局,调用此方法前需要 recyclerView.adapter=MAdapter
//        setEmptyView(R.layout.empty_view)
        //开启加载动画,设置为 缩放显示
        animationEnable = true
        setAnimationWithDefault(AnimationType.SlideInLeft)

    }
}