package com.ve.module.lockit.plus.ui.page.home.model

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.lockit.plus.R

/**
 * @author waynie
 * @date 2022/10/12
 * @desc lockit-android
 */
class HomeBulbAdapter : BaseMultiItemQuickAdapter<HomeBulbBean, BaseViewHolder>() {

    init {
        addItemType(HomeAdapterType.Bulb.Single,R.layout.item_home_bulb_single)
        addItemType(HomeAdapterType.Bulb.Multi,R.layout.item_home_bulb_multi)
    }

    override fun convert(holder: BaseViewHolder, item: HomeBulbBean) {

    }

    var count = 0

    fun swapLayoutType() {
        swapLayoutType(++count % 2)
    }


    fun swapLayoutType(type: Int) {
        data.forEach { item ->
            item.itemType = type
        }
        notifyDataSetChanged()
    }
}