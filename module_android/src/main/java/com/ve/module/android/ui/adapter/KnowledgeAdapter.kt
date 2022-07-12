package com.ve.module.android.ui.adapter

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.model.Article
import com.ve.lib.common.base.adapter.BaseSlideAdapter
import com.ve.lib.common.utils.ImageLoader

/**
 * Created by chenxz on 2018/4/22.
 */
class KnowledgeAdapter : BaseSlideAdapter<Article, BaseViewHolder>(R.layout.item_knowledge_list) {

    init {
        addChildClickViewIds(R.id.iv_article_favorite)
    }
    override fun convert(holder: BaseViewHolder, item: Article) {
        item ?: return
        holder ?: return
        val authorStr = if (item.author.isNotEmpty()) item.author else item.shareUser
        holder.setText(R.id.tv_article_title, Html.fromHtml(item.title))
                .setText(R.id.tv_article_author, authorStr)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_article_favorite,
                        if (item.collect) R.drawable.ic_like_checked else R.drawable.ic_like_normal
                )
              //  .addOnClickListener(R.id.iv_like)
        val chapterName = when {
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() ->
                "${item.superChapterName} / ${item.chapterName}"
            item.superChapterName.isNotEmpty() -> item.superChapterName
            item.chapterName.isNotEmpty() -> item.chapterName
            else -> ""
        }
        holder.setText(R.id.tv_article_chapterName, chapterName)

        if (!TextUtils.isEmpty(item.envelopePic)) {
            holder.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(it, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
            }
        } else {
            holder.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.GONE
        }
    }

}