package com.ve.lib.common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import java.lang.reflect.ParameterizedType

abstract class BaseBindingAdapter<T,VB : ViewBinding>(data: MutableList<T>? = null) :
    BaseQuickAdapter<T, VBViewHolder<VB>>(0, data) {


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
        val mBinding = inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        return VBViewHolder(mBinding, mBinding.root)
    }
}