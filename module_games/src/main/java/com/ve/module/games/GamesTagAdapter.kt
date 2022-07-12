package com.ve.module.games

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.base.adapter.BaseSlideAdapter


/**
 * Author : ve
 * Date   : 2021.05.28
 * Describe : 继承RecyclerView.Adapter，泛型为PlaceAdapter.ViewHolder； ViewHolder是一个内部类，用于对实例进行缓存
 */
class GamesTagAdapter: BaseSlideAdapter<Game, BaseViewHolder>(R.layout.item_game){

    init {
        addChildClickViewIds(R.id.game_icon)
    }
    override fun convert(holder: BaseViewHolder, item: Game) {

        holder.setText(R.id.game_name,item.name)
        Glide.with(context).load(item.imageId).into(holder.getView(R.id.game_icon))
    }

}
