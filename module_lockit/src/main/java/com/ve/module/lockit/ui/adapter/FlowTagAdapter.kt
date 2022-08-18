package com.ve.module.lockit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ve.lib.common.utils.color.ColorUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.respository.database.entity.PrivacyTag
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

/**
 * @Author  weiyi
 * @Date 2022/4/22
 * @Description  current project lockit-android
 */
class FlowTagAdapter( data:List<PrivacyTag>?=null):TagAdapter<PrivacyTag>(data) {


    override fun getView(parent: FlowLayout?, position: Int, t: PrivacyTag): View {

        val tv: TextView = LayoutInflater.from(parent?.context).inflate(
            R.layout.lockit_item_flow_tag, parent, false
        ) as TextView

        tv.text = t.tagName
        tv.setTextColor(ColorUtil.randomColor())
        return tv
    }

//    abstract fun attachFlowLayout():FlowLayout
}
