package com.ve.module.lockit.plus.ui.page.test

import android.view.View
import com.ve.lib.common.base.adapter.BaseBindingAdapter
import com.ve.lib.common.base.adapter.ViewBindingHolder
import com.ve.module.lockit.plus.databinding.ItemSimpleSettingBinding

class SimpleSettingAdapter :
    BaseBindingAdapter<SimpleSettingBean, ItemSimpleSettingBinding>() {
    override fun convert(holder: ViewBindingHolder<ItemSimpleSettingBinding>, item: SimpleSettingBean) {

        holder.vb.apply {
            ivStartIcon.setImageDrawable(item.startIcon)
            ivStartIcon.visibility=View.VISIBLE
            tvStartText.text=item.title
            tvTipText.text=item.endText
            ivRedDot.visibility=if(item.isShowDot) View.VISIBLE else View.GONE
        }

        if(!item.clickable){
            holder.itemView.alpha= 0.5F
            holder.itemView.isEnabled=false
        }else{
            holder.itemView.alpha= 1F
            holder.itemView.isEnabled=true
        }

    }


}