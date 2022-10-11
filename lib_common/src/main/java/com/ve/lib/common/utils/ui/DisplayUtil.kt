package com.ve.lib.common.utils.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import com.ve.lib.common.utils.AppContextUtil

/**
 * @author ve
 * @date 2022/9/16
 * @desc lockit-android
 */
object DisplayUtil {

    private var displayMetrics: DisplayMetrics = AppContextUtil.mContext.resources.displayMetrics


    private var screenWidth: Int = 0
//    @Deprecated(message = "use screenWidth", replaceWith = ReplaceWith("this.getScreenHeight()"))
    private var screenHeight: Int = 0
    private var screenDpi: Int = 0

    init {
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        screenDpi = displayMetrics.densityDpi
    }

    /**
     * px to dp
     */
    @JvmStatic
    fun px2dip(pxValue: Float): Int {
        val scale = displayMetrics.density
        return (pxValue / scale+ 0.5f).toInt()
    }
    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        val scale = displayMetrics.density
        return (dpValue * scale+ 0.5f).toInt()
    }
    fun sp2px(spValue: Float): Int {
        val fontScale = displayMetrics.scaledDensity
        return (spValue * fontScale+ 0.5f).toInt()
    }

    @JvmStatic
    fun dip2px(dpValue: Float): Int {
        val scale = displayMetrics.density
        return (dpValue * scale+ 0.5f).toInt()
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


   fun getScreenWidth(): Int {
        return screenWidth
    }

   fun getScreenHeight(): Int {
        return screenHeight
    }



    //UI图的大小
    private val STANDARD_WIDTH = 1080
    private val STANDARD_HEIGHT = 1920
    
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
        return (px / parentWidth * screenWidth).toInt()
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
        return (px / parentHeight * screenHeight).toInt()
    }
}