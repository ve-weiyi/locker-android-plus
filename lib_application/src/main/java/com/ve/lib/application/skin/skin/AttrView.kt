package com.ve.lib.application.skin.skin

import android.view.View


class AttrView(val view: View, val attrs: MutableList<AttrItem> = mutableListOf()) {

    class AttrItem(val attrName: String, val resId: Int)

    /**
     * attrName 需要修改的属性名，可以是 @string、@textColor
     * resId 具体的值
     */
    fun addAttr(attrName: String, resId: Int): AttrView {
        attrs.add(AttrItem(attrName, resId))
        return this
    }
}

