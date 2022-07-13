package com.ve.module.lockit.common.enums

import android.graphics.Color
import com.ve.module.lockit.common.enums.ColorEnum

/**
 * @Author weiyi
 * @Date 2022/4/22
 * @Description current project lockit-android
 */
enum class PasswordEnum(
    /**
     * 得分
     */
    val score: Int,
    /**
     * 标签
     */
    val label: String,
    /**
     * 描述
     */
    val desc: String,
    /**
     * 得分
     */
    val colorInt: Int
) {
    /**
     * 非常安全
     */
    VERY_SECURE(90, "VERY_SECURE", "非常安全", ColorEnum.GREEN),

    SECURE(80, "SECURE", "安全", ColorEnum.GREEN),

    VERY_STRONG(70, "VERY_STRONG", "非常强", ColorEnum.YELLOW),

    STRONG(60, "STRONG", "强", ColorEnum.YELLOW),

    AVERAGE(50, "AVERAGE", "中等", ColorEnum.YELLOW),

    WEAK(25, "WEAK", "弱", ColorEnum.RED),

    VERY_WEAK(0, "VERY_WEAK", "非常弱", ColorEnum.RED),

    unknown(-1, "unknown", "unknown", ColorEnum.RED);
}