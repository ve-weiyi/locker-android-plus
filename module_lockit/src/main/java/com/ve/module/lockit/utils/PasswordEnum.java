package com.ve.module.lockit.utils;

import android.graphics.Color;

/**
 * @Author weiyi
 * @Date 2022/4/22
 * @Description current project lockit-android
 */
public enum PasswordEnum {

    /**
     * 非常安全
     */
    VERY_SECURE(90, "VERY_SECURE", "非常安全", Color.GREEN),

    SECURE(80, "SECURE", "安全", Color.GREEN),

    VERY_STRONG(70, "VERY_STRONG", "非常强", Color.YELLOW),

    STRONG(60, "STRONG", "强", Color.YELLOW),

    AVERAGE(50, "AVERAGE", "中等", Color.RED),
    WEAK(25, "WEAK", "弱", Color.RED),
    VERY_WEAK(0, "VERY_WEAK", "非常弱", Color.RED),
    unknown(-1, "unknown", "unknown", Color.RED);
    
    /**
     * 得分
     */
    public final Integer score;

    /**
     * 标签
     */
    public final String label;

    /**
     * 描述
     */
    public final String desc;

    /**
     * 得分
     */
    public final Integer colorInt;

    PasswordEnum(Integer score, String label, String desc, Integer colorInt) {
        this.score = score;
        this.label = label;
        this.desc = desc;
        this.colorInt = colorInt;
    }
}
