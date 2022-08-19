package com.ve.lib.common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import com.ve.lib.common.R

import java.lang.reflect.ParameterizedType

abstract class BaseSectionBindingAdapter<T : SectionEntity, VB : ViewBinding>(data: MutableList<T>? = null) :
    BaseSectionQuickAdapter<T, VBViewHolder<VB>>(R.layout.item_sticky_header)
    , LoadMoreModule, DraggableModule ,UpFetchModule{

    lateinit var mBinding: VB

    //重写返回自定义 ViewHolder
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
        val vbClass: Class<VB> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>
        val inflate = vbClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        mBinding = inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        return VBViewHolder(mBinding, mBinding.root)
    }
}