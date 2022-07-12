package com.ve.module.android.ui.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.CoinInfo
import com.ve.lib.common.base.adapter.BaseSlideAdapter

/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
class RankAdapter : BaseSlideAdapter<CoinInfo, BaseViewHolder>(R.layout.item_rank_list) {

    override fun convert(holder: BaseViewHolder, item: CoinInfo) {
        val index = holder.layoutPosition

        holder.setText(R.id.tv_username, item.username)
                .setText(R.id.tv_score, item.coinCount.toString())
                .setText(R.id.tv_ranking, (index + 1).toString())
    }
}