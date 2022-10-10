package com.ve.lib.common.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.reflect.ParameterizedType

/**
 * BaseQuickAdapter与ViewBinding结合使用
 * https://www.jianshu.com/p/1a95e8af5efb
 * @Description hello word!
 * @Author  waynie
 * @Date 2022/4/10
 */
abstract class BaseBindingAdapter<T, VB : ViewBinding>( data: MutableList<T>? = null,res: Int = 0,) :
    BaseQuickAdapter<T, BaseBindingAdapter.ViewBindingHolder<VB>>(res, data) {


    //重写返回自定义 ViewHolder
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder<VB> {
        //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
        val vbClass: Class<VB> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VB>
        val inflate = vbClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val mBinding =
            inflate.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        return ViewBindingHolder(mBinding, mBinding.root)
    }

    open class ViewBindingHolder<VB : ViewBinding>(val vb: VB, view: View) : BaseViewHolder(view)
}