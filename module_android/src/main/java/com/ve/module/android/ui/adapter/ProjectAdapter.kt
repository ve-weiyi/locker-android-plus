package com.ve.module.android.ui.adapter

import android.text.Html
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.Article
import com.ve.lib.common.base.adapter.BaseSlideAdapter
import com.ve.lib.common.utils.data.ImageLoader

/**
 * Created by chenxz on 2018/5/20.
 */
class ProjectAdapter : BaseSlideAdapter<Article, BaseViewHolder>(R.layout.item_project_list), LoadMoreModule {

    init {
        addChildClickViewIds(R.id.item_project_list_like_iv)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        val authorStr = if (item.author.isNotEmpty()) item.author else item.shareUser
        holder.setText(R.id.item_project_list_title_tv, Html.fromHtml(item.title))
            .setText(R.id.item_project_list_content_tv, Html.fromHtml(item.desc))
            .setText(R.id.item_project_list_time_tv, item.niceDate)
            .setText(R.id.item_project_list_author_tv, authorStr)
            .setImageResource(
                R.id.item_project_list_like_iv,
                if (item.collect) R.drawable.ic_like_checked else R.drawable.ic_like_normal
            )
        ImageLoader.load(context, item.envelopePic, holder.getView(R.id.item_project_list_iv))
    }
}