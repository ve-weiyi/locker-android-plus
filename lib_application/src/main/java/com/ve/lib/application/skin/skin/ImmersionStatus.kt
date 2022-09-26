package com.ve.lib.application.skin.skin

import android.app.Activity
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt

/**
 * @author waynie
 * @date 2022/9/26
 * @desc lockit-android
 * 沉浸式状态栏
 */
interface ImmersionStatus {
    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度 @IntRange(from = 0, to = 255)
     */

    fun setColor(activity: Activity, @ColorInt color: Int, statusBarAlpha: Int) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = calculateStatusColor(color, statusBarAlpha)
        activity.window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值  @IntRange(from = 0, to = 255)
     * @return 最终的状态栏颜色
     */
    private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }
}