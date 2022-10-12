package com.ve.module.lockit.plus.ui.page.list

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.module.lockit.plus.R

/**
 * @author waynie
 * @date 2022/10/12
 * @desc lockit-android
 */
class HomeRoomAdapter : BaseMultiItemQuickAdapter<HomeRoomBean, BaseViewHolder>() {

    init {
        addItemType(HomeRoomBean.HORIZONTAL,R.layout.item_home_single)
        addItemType(HomeRoomBean.VERTICAL,R.layout.item_home_group)
    }

    override fun convert(holder: BaseViewHolder, item: HomeRoomBean) {

    }
}