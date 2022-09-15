package com.ve.module.android.ui.adapter

import android.text.Html
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.Collect
import com.ve.lib.application.BaseApplication.Companion.mContext
import com.ve.lib.common.base.adapter.BaseSlideAdapter
import com.ve.lib.common.utils.file.ImageLoader


/**
 * Created by chenxz on 2018/6/10.
 */
class CollectAdapter : BaseSlideAdapter<Collect, BaseViewHolder>(R.layout.item_collect_list),
    LoadMoreModule {

    init {
        //添加子项
        addChildClickViewIds(R.id.iv_article_favorite)
    }

    override fun convert(holder: BaseViewHolder, item: Collect) {
        val authorStr = when {
            item.author.isNotEmpty() -> item.author
            else -> mContext.getString(com.ve.lib.common.R.string.anonymous)
        }

        holder.setText(R.id.tv_article_title, Html.fromHtml(item.title))
                .setText(R.id.tv_article_author, authorStr)
                .setText(R.id.tv_article_date, item.niceDate)
                .setImageResource(R.id.iv_article_favorite, R.drawable.ic_like_checked)
                //.addOnClickListener(R.id.iv_like)

        //Glide.with(context).load(R.mipmap.ic_like_checked).into(holder.getView(R.id.iv_article_favorite))

        holder.setText(R.id.tv_article_chapterName, item.chapterName)
        if (item.envelopePic.isNotEmpty()) {
            holder.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.VISIBLE
            context.let {
                ImageLoader.load(it, item.envelopePic, holder.getView(R.id.iv_article_thumbnail))
            }
        } else {
            holder.getView<ImageView>(R.id.iv_article_thumbnail)
                    .visibility = View.GONE
        }
    }
}