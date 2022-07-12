package com.ve.module.android.ui.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.model.Article
import com.ve.lib.common.base.adapter.BaseSlideAdapter

import com.ve.lib.common.utils.ImageLoader

/**
 * @author chenxz
 * @date 2019/11/15
 * @desc
 */
class ShareAdapter : BaseSlideAdapter<Article, BaseViewHolder>(R.layout.item_share_list),
    LoadMoreModule {

    init {
        addChildClickViewIds(R.id.iv_article_favorite, R.id.btn_delete, R.id.rl_content)
    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        val authorStr = if (item.author.isNotEmpty()) item.author else item.shareUser
        holder.setText(R.id.tv_article_title, Html.fromHtml(item.title))
            .setText(R.id.tv_article_author, authorStr)
            .setText(R.id.tv_article_date, item.niceDate)
            .setImageResource(R.id.iv_article_favorite, if (item.collect) R.drawable.ic_like_checked else R.drawable.ic_like_normal)
        val chapterName = when {
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() ->
                "${item.superChapterName} / ${item.chapterName}"
            item.superChapterName.isNotEmpty() -> item.superChapterName
            item.chapterName.isNotEmpty() -> item.chapterName
            else -> ""
        }
        holder.setText(R.id.tv_article_chapterName, chapterName)
        if (item.envelopePic.isNotEmpty()) {
            holder.getView<ImageView>(R.id.iv_article_thumbnail)
                .visibility = View.VISIBLE
            ImageLoader.load(context, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
        } else {
            holder.getView<ImageView>(R.id.iv_article_thumbnail).visibility = View.GONE
        }
        val tv_fresh = holder.getView<TextView>(R.id.tv_article_fresh)
        if (item.fresh) {
            tv_fresh.visibility = View.VISIBLE
        } else {
            tv_fresh.visibility = View.GONE
        }
        val tv_top = holder.getView<TextView>(R.id.tv_article_top)
        if (item.top == "1") {
            tv_top.visibility = View.VISIBLE
        } else {
            tv_top.visibility = View.GONE
        }
        val tv_article_tag = holder.getView<TextView>(R.id.tv_article_tag)
        if (item.tags.size > 0) {
            tv_article_tag.visibility = View.VISIBLE
            tv_article_tag.text = item.tags[0].name
        } else {
            tv_article_tag.visibility = View.GONE
        }
        val tv_article_audit = holder.getView<TextView>(R.id.tv_article_audit)
        if (item.audit == 0) {
            tv_article_audit.visibility = View.VISIBLE
            tv_article_audit.text = context.getString(com.ve.lib.application.R.string.audited)
        } else {
            tv_article_audit.visibility = View.GONE
        }
    }
}