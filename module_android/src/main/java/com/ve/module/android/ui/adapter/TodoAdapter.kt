package com.ve.module.android.ui.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.TodoBean
import com.ve.module.android.repository.bean.TodoDataBean
import com.ve.lib.view.widget.view.TiltTextView

/**
 * Created by chenxz on 2018/8/8.
 */
class TodoAdapter :
    BaseSectionQuickAdapter<TodoDataBean, BaseViewHolder>(R.layout.item_sticky_header, R.layout.item_todo_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.btn_delete, R.id.btn_done, R.id.item_todo_content)
    }

    override fun convertHeader(helper: BaseViewHolder, item: TodoDataBean) {
        helper.setText(R.id.tv_header, item.headerName)
    }

    override fun convert(holder: BaseViewHolder, item: TodoDataBean) {
        val itemData = item.todoBean as TodoBean
        holder.setText(R.id.tv_todo_title, itemData.title)

        val tv_todo_desc = holder.getView<TextView>(R.id.tv_todo_desc)
        tv_todo_desc.text = ""
        tv_todo_desc.visibility = View.INVISIBLE
        if (itemData.content.isNotEmpty()) {
            tv_todo_desc.visibility = View.VISIBLE
            tv_todo_desc.text = itemData.content
        }
        val btn_done = holder.getView<Button>(R.id.btn_done)
        if (itemData.status == 0) {
            btn_done.text = context.resources.getString(com.ve.lib.common.R.string.mark_done)
        } else if (itemData.status == 1) {
            btn_done.text = context.resources.getString(com.ve.lib.common.R.string.restore)
        }
        val tv_tilt = holder.getView<TiltTextView>(R.id.tv_tilt)
        if (itemData.priority == 1) {
            tv_tilt.setText(context.resources.getString(com.ve.lib.common.R.string.priority_1))
            tv_tilt.visibility = View.VISIBLE
        } else {
            tv_tilt.visibility = View.GONE
        }
    }
}