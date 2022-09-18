package com.ve.module.lockit.ui.adapter

import com.ve.lib.common.base.adapter.BaseSlideBindingAdapter
import com.ve.lib.common.base.adapter.ViewBindingHolder
import com.ve.module.lockit.R
import com.ve.module.lockit.databinding.LockitItemSimpleListBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
class FolderListAdapter: BaseSlideBindingAdapter<PrivacyFolder, LockitItemSimpleListBinding>() {

    init {
        addChildClickViewIds(R.id.item_layout_content, R.id.item_btn_edit, R.id.item_btn_delete)
    }

    override fun convert(holder: ViewBindingHolder<LockitItemSimpleListBinding>, item: PrivacyFolder) {
        holder.vb.tvTilt.setText(item.id.toString())
        holder.vb.itemId.text = item.id.toString()
        holder.vb.itemName.text = item.folderName
        holder.vb.itemDesc.text = item.folderName
        context
    }
}