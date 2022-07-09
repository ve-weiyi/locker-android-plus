package com.ve.lib.common.base.adapter

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * BaseQuickAdapter与ViewBinding结合使用
 * https://www.jianshu.com/p/1a95e8af5efb
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
open class VBViewHolder<VB : ViewBinding>(val vb: VB, view: View) : BaseViewHolder(view)

