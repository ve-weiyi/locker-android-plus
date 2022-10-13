package com.ve.module.lockit.plus.ui.page.model

import android.graphics.drawable.Drawable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 * 扫地机：
设备图片
设备名称
设备状态 On/Off/Offline 0,1,2
设备控制：启动/暂停
预约状态图标：该设备设置了预约时显示，点击进入「预约设置」功能页
OTA状态图标：该设备有新OTA推送时显示，点击进入「固件升级」功能页

 */
data class HomeDeviceBean(
    var name: String,
    var deviceImage: Drawable? = null,
    var deviceCode: String = HomeAdapterType.Code.Robot,
    var isOn: Boolean = false,
    var state: Int = 0,
    var isSchedule: Boolean = false,
    var isUpdate: Boolean = false,
) : MultiItemEntity {

    override var itemType: Int = HomeAdapterType.Device.CARD

}