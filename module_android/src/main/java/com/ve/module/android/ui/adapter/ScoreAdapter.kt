package com.ve.module.android.ui.adapter

import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.android.R
import com.ve.module.android.repository.bean.UserScore
import com.ve.lib.common.base.adapter.BaseSlideAdapter


/**
 * @author chenxz
 * @date 2019/9/5
 * @desc
 */
class ScoreAdapter : BaseSlideAdapter<UserScore, BaseViewHolder>(R.layout.item_socre_list) ,
    LoadMoreModule,DraggableModule {

    init{
        addChildClickViewIds(R.id.tv_desc)
    }
    override fun convert(holder: BaseViewHolder, item: UserScore) {
        holder.setText(R.id.tv_reason, item.reason)
                .setText(R.id.tv_desc, item.desc)
                .setText(R.id.tv_score, "+${item.coinCount}")
    }


}