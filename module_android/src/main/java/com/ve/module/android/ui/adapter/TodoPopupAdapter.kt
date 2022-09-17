package com.ve.module.android.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.TodoTypeBean

/**
 * @author chenxz
 * @date 2018/11/25
 * @desc
 */
class TodoPopupAdapter : BaseQuickAdapter<TodoTypeBean, BaseViewHolder>(R.layout.item_todo_popup_list) {

    override fun convert(helper: BaseViewHolder, item: TodoTypeBean) {
        val tv_popup = helper.getView<TextView>(R.id.tv_popup)
        tv_popup.text = item.name
        if (item.isSelected) {
            tv_popup.setTextColor(context.resources.getColor(com.ve.lib.application.R.color.colorAccent))
        } else {
            tv_popup.setTextColor(context.resources.getColor(com.ve.lib.application.R.color.color_text_dark))
        }
    }
}