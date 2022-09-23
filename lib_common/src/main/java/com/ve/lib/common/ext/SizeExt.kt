package com.ve.lib.common.ext

import android.content.Context

/**
 * @author waynie
 * @date 2022/9/14
 * @desc EufyHomeNew
 */
/**
 * px to dp
 */
fun px2dp(context:Context,pxValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale)
}

fun dp2px(context:Context,dpValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale)
}

fun sp2px(context:Context,spValue: Float): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale)
}
