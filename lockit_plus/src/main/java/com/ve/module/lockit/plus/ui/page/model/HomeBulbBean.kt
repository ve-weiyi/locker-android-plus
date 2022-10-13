package com.ve.module.lockit.plus.ui.page.model

import android.graphics.drawable.Drawable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 * 灯组卡片：
    灯组图标
    灯组名称
    灯组开关
    灯组状态：显示该灯组中实际打开和关闭的灯的数量，格式{开启数量} ON/{关闭数量} 几OFF
 */
data class HomeBulbBean(
    var name: String,
    var RoomImage: Drawable? = null,
    var isOn:Boolean=true,
    var state: Int = 0,
) : MultiItemEntity {

    override var itemType: Int = HomeAdapterType.Bulb.Single

}