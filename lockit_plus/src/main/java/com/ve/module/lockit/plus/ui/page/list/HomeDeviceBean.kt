package com.ve.module.lockit.plus.ui.page.list

import android.graphics.drawable.Drawable

/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 */
data class HomeDeviceBean(
    var name:String,
    var isOnline:Boolean=true,
    var deviceImage:Drawable?=null,
) {

}