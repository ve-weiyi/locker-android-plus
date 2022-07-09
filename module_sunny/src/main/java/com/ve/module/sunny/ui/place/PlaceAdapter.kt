package com.ve.module.sunny.ui.place

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.sunny.R
import com.ve.module.sunny.logic.http.model.Place



/**
 * Author : ve
 * Date   : 2021.05.28
 * Describe : 继承RecyclerView.Adapter，泛型为PlaceAdapter.ViewHolder； ViewHolder是一个内部类，用于对实例进行缓存
 */
class PlaceAdapter():BaseQuickAdapter<Place,BaseViewHolder>(R.layout.item_place)
    ,LoadMoreModule{

    override fun convert(holder: BaseViewHolder, item: Place) {
        holder.setText(R.id.placeName,item.name)
        holder.setText(R.id.placeAddress,item.address)
        //或者先获得控件id，再绑定属性.都行
//        val placeName: TextView = holder.getView(R.id.placeName)
//        val placeAddress: TextView = holder.getView(R.id.placeAddress)
//        placeName.text = item.name
//        placeAddress.text = item.address
    }

}
