package com.ve.lib.common.utils.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import com.ve.lib.common.utils.AppContextUtil

/**
 * Created by yechao on 2020/10/30.
 * Describe : 屏幕相关 密度
 *
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object DisplayUtil {

    /**
     * dp to px
     */
    @JvmStatic
    fun dp2px(dp: Float): Int {
        val density = AppContextUtil.getApp().resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    /**
     * px to dp
     */
    fun px2dp(px: Int): Float {
        val density = AppContextUtil.getApp().resources.displayMetrics.density
        return px / density
    }

    /**
     * 获取状态栏高度 px
     */
    fun getStatusBarHeight(): Int {
        val resources = AppContextUtil.getApp().resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取ActionBar高度 px
     */
    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (AppContextUtil.getApp().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, AppContextUtil.getApp().resources.displayMetrics)
        } else 0
    }

    /**
     * 获取导航栏高度 px
     */
    fun getNavBarHeight(): Int {
        val res: Resources = AppContextUtil.getApp().resources
        val resourceId: Int = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }


    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    init{
        displayMetrics = AppContextUtil.mContext.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }


    //UI图的大小
    private val STANDARD_WIDTH = 1080
    private val STANDARD_HEIGHT = 1920

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(): Int {
        return screenWidth!!
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(): Int {
        return screenHeight!!
    }


    /**
     * 传入UI图中问题的高度，单位像素
     * @param size
     * @return
     */
    fun getPaintSize(size: Int): Int {
        return getRealHeight(size)
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealWidth(px: Int): Int {
        //ui图的宽度
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px          ui图中的大小
     * @param parentWidth 父view在ui图中的高度
     * @return
     */
    fun getRealWidth(px: Int, parentWidth: Float): Int {
        return (px / parentWidth * getScreenWidth()!!).toInt()
    }

    /**
     * 输入UI图的尺寸，输出实际的px
     *
     * @param px ui图中的大小
     * @return
     */
    fun getRealHeight(px: Int): Int {
        //ui图的宽度
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * 输入UI图的尺寸，输出实际的px,第二个参数是父布局
     *
     * @param px           ui图中的大小
     * @param parentHeight 父view在ui图中的高度
     * @return
     */
    fun getRealHeight(px: Int, parentHeight: Float): Int {
        return (px / parentHeight * getScreenHeight()!!).toInt()
    }

    /**
     * dip转px
     * @param dipValue
     * @return int
     */
    fun dip2px(dipValue: Float): Int {
        val scale = displayMetrics?.density
        return (dipValue * scale!! + 0.5f).toInt()
    }
}