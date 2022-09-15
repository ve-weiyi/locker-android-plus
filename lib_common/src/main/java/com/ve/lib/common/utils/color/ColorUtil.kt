package com.ve.lib.common.utils.color

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import java.util.*

/**
 * @author chenxz
 * @date 2019/11/24
 * @desc ColorUtil
 */
object ColorUtil {

    val primary = intArrayOf(
        Color.parseColor("#F44336"), Color.parseColor("#E91E63"), Color.parseColor("#9C27B0"),
        Color.parseColor("#673AB7"), Color.parseColor("#3F51B5"), Color.parseColor("#2196F3"),
        Color.parseColor("#03A9F4"), Color.parseColor("#00BCD4"), Color.parseColor("#009688"),
        Color.parseColor("#4CAF50"), Color.parseColor("#8BC34A"), Color.parseColor("#CDDC39"),
        Color.parseColor("#FFEB3B"), Color.parseColor("#FFC107"), Color.parseColor("#FF9800"),
        Color.parseColor("#FF5722"), Color.parseColor("#795548"), Color.parseColor("#9E9E9E"),
        Color.parseColor("#607D8B")
    )
    /**
     * 随机Color 避免纯色没有从255取值
     */
    fun randomColor(): Int {
        Random().run {
            val red = nextInt(210)
            val green = nextInt(210)
            val blue = nextInt(210)
            return Color.rgb(red, green, blue)
        }
    }
    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(bound:Int=255 ): Int {
        return primary.random()
//        val random = Random()
//        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
//        val deep=random.nextInt(3)
//        var red = random.nextInt(bound)
//        var green = random.nextInt(bound)
//        var blue = random.nextInt(bound)
//
//        if (SettingUtil.getIsNightMode()) {
//            //150-255 浅色
//            red = random.nextInt(105) + 150
//            green = random.nextInt(105) + 150
//            blue = random.nextInt(105) + 150
//        }
//        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
//        return Color.rgb(red, green, blue)
    }
    /**
     * 计算颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    fun alphaColor(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 计算颜色
     *
     * @param color color值
     * @param alpha alpha值[0-1]
     * @return 最终的状态栏颜色
     */
    fun alphaColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        return alphaColor(color, (alpha * 255).toInt())
    }

    /**
     * 根据fraction值来计算当前的颜色
     *
     * @param colorFrom 起始颜色
     * @param colorTo   结束颜色
     * @param fraction  变量
     * @return 当前颜色
     */
    fun changingColor(@ColorInt colorFrom: Int, @ColorInt colorTo: Int, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Int {
        val redStart = Color.red(colorFrom)
        val blueStart = Color.blue(colorFrom)
        val greenStart = Color.green(colorFrom)
        val alphaStart = Color.alpha(colorFrom)

        val redEnd = Color.red(colorTo)
        val blueEnd = Color.blue(colorTo)
        val greenEnd = Color.green(colorTo)
        val alphaEnd = Color.alpha(colorTo)

        val redDifference = redEnd - redStart
        val blueDifference = blueEnd - blueStart
        val greenDifference = greenEnd - greenStart
        val alphaDifference = alphaEnd - alphaStart

        val redCurrent = (redStart + fraction * redDifference).toInt()
        val blueCurrent = (blueStart + fraction * blueDifference).toInt()
        val greenCurrent = (greenStart + fraction * greenDifference).toInt()
        val alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }

}