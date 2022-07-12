package com.ve.module.locker.ui.adapter

import com.ve.lib.common.base.adapter.BaseSlideBindingAdapter
import com.ve.lib.common.base.adapter.VBViewHolder
import com.ve.module.locker.R
import com.ve.module.locker.databinding.LockerItemSimpleListBinding
import com.ve.module.locker.respository.database.entity.PrivacyTag


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
class TagListAdapter : BaseSlideBindingAdapter<PrivacyTag, LockerItemSimpleListBinding>() {

    init {
        addChildClickViewIds(R.id.item_layout_content, R.id.item_btn_edit, R.id.item_btn_delete)
    }

    override fun convert(holder: VBViewHolder<LockerItemSimpleListBinding>, item: PrivacyTag) {
        holder.vb.tvTilt.setText(item.id.toString())
        holder.vb.itemId.text = item.id.toString()
        holder.vb.itemName.text = item.tagName
        holder.vb.itemDesc.text = item.tagDesc

    }
}