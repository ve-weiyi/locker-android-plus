package com.ve.module.lockit.plus.ui.page.home.model

import android.graphics.drawable.Drawable

data class SimpleSettingBean(
    val title:String="",
    val endText:String="",
    val startIcon: Drawable? = null,
    //右边文字旁显示小红点
    val isShowDot:Boolean=false,
    val clickable: Boolean = true,
    val actionKey:String="",
    // 点击后执行
    val onClickAction:(()->Unit)?=null
) {

}