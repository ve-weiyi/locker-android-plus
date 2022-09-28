package com.ve.lib.application.skin.factory

import android.view.View

/**
 * @author waynie
 * @date 2022/9/28
 * @desc lockit-android
 */
data class AttrView(val view: View, val attrs: MutableList<AttrItem> = mutableListOf()) {

    data class AttrItem(val attrName: String,val resId: Int, val attrId: Int?=null)

    /**
     * attrName 需要修改的属性名，可以是 @string、@textColor
     * resId 具体的值
     * attrId
     */
    fun addAttr(attrName: String, resId: Int, attrId: Int?=null): AttrView {
        attrs.add(AttrItem(attrName, resId,attrId))
        return this
    }
}