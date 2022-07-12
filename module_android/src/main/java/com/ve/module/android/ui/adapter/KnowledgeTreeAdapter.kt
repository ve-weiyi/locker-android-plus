package com.ve.module.android.ui.adapter

import android.text.Html
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.Tree
import com.ve.lib.common.base.adapter.BaseSlideAdapter

/**
 * Created by chenxz on 2018/5/9.
 */
class KnowledgeTreeAdapter : BaseSlideAdapter<Tree, BaseViewHolder>(R.layout.item_knowledge_tree_list),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Tree) {
        holder.setText(R.id.title_first, item.name)
        item.children.let {
            holder.setText(
                R.id.title_second,
                it.joinToString("    ", transform = { child ->
                    Html.fromHtml(child.name)
                })
            )
        }
    }
}