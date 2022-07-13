package com.ve.lib.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat

import com.ve.lib.common.utils.ColorUtil
import com.ve.lib.common.utils.SettingUtil


/**
 * @author chenxz
 * @date 2019/11/24
 * @desc WebContainer
 */
class WebContainer : CoordinatorLayout {

    private var mDarkTheme: Boolean = false

    private var mMaskColor = Color.TRANSPARENT

    init {
        mDarkTheme = SettingUtil.getIsNightMode()
        if (mDarkTheme) {
            //夜间模式下的颜色
            mMaskColor = ColorUtil.alphaColor(ContextCompat.getColor(getContext(), com.ve.lib.application.R.color.mask_color), 0.6f)
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mDarkTheme) {
            canvas.drawColor(mMaskColor)
        }
    }
}
