package com.ve.module.android.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.database.entity.SearchHistory
import com.ve.lib.common.base.adapter.BaseSlideAdapter

class SearchHistoryAdapter(datas: MutableList<SearchHistory>?=null)
    : BaseSlideAdapter<SearchHistory, BaseViewHolder>(R.layout.item_search_history, datas) {

    init {
        addChildClickViewIds(R.id.iv_clear)
    }

    override fun convert(holder: BaseViewHolder, item: SearchHistory) {

        holder.setText(R.id.tv_search_key, item.name)

    }


}