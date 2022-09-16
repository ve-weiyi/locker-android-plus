package com.ve.lib.view.widget.preference


import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

import com.ve.lib.view.R
import com.ve.lib.view.widget.view.CircleView

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/17
 */
class IconPreference(
    context: Context,
    attrs: AttributeSet
) : Preference(context, attrs) {

    private var circleImageView: CircleView? = null

    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        circleImageView = holder.findViewById(R.id.iv_preview) as CircleView

        val color = com.ve.lib.common.utils.SettingUtil.getColor()
        setColor(color)

    }

    fun setView() {
        val color = com.ve.lib.common.utils.SettingUtil.getColor()
        setColor(color)
    }

    fun setColor(color: Int) {
        circleImageView!!.setBackgroundColor(color)
    }
}