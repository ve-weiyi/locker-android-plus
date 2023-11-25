package com.ve.module.lockit.plus.ui.page.home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ve.module.lockit.plus.R

/**
 * @author waynie
 * @date 2022/10/13
 * @desc lockit-android
 */
class MineItemView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mIconView: ImageView? = null
    private var mTitleView: TextView? = null
    private var mBadgeView: ImageView? = null

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_mine_item, this, true)

        mIconView = view.findViewById(R.id.item_icon)
        mTitleView = view.findViewById(R.id.item_title)
        mBadgeView = view.findViewById(R.id.item_badge)

        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemView)
        val icon = if (typedArray.hasValue(R.styleable.MineItemView_itemIcon)) {
            typedArray.getDrawable(R.styleable.MineItemView_itemIcon)
        } else ContextCompat.getDrawable(context, R.drawable.ic_help_feedback)
        val title = typedArray.getString(R.styleable.MineItemView_itemTitle)
        val showBadge = typedArray.getInt(R.styleable.MineItemView_badgeVisibility, 0)

        typedArray.recycle()

        mIconView!!.setImageDrawable(icon)
        mTitleView?.text = title
        mBadgeView?.visibility = showBadge
    }

}