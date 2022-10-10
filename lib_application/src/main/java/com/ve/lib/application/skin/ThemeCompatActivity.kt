package com.ve.lib.application.skin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ve.lib.application.R
import com.ve.lib.application.skin.set.SkinManager

open class ThemeCompatActivity: AppCompatActivity() {

    var flag=true
    protected  var mThemeColor:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        SkinManager.setTheme(this)
        super.onCreate(savedInstanceState)
        mThemeColor= getThemeColor(this,R.attr.colorBrand,Color.CYAN)
        //沉浸式状态栏
//        setColor(this, mThemeColor, 0)
    }


    fun switchTheme(theme: Int){
        SkinManager.switchTheme(this,theme)
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0) //不设置进入退出动画
        startActivity(intent)
        overridePendingTransition(0, 0)
    }


    /**
     * 获取主题属性的资源id
     */
    fun getThemeColor(context: Context, attr: Int, defaultColor: Int): Int {
        val obtainStyledAttributes = context.theme.obtainStyledAttributes(intArrayOf(attr))
        val redIds = IntArray(obtainStyledAttributes.indexCount)
        for (i in 0 until obtainStyledAttributes.indexCount) {
            val type = obtainStyledAttributes.getType(i)
            redIds[i] =
                    //这个用来保证获取到的资源是颜色
                if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
                    obtainStyledAttributes.getColor(i, defaultColor)
                } else {
                    defaultColor
                }
        }
        obtainStyledAttributes.recycle()
        return redIds[0]
    }





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

    /**
     * 设置为true时，系统会给布局增加一个状态栏高度的top padding, 这样布局背景就显示在系统的status bar下面
     *
     * @param fitSystemWindows
     */
    protected open fun setFitsSystemWindows(fitSystemWindows: Boolean) {
        val contentView: View = (this.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)
        contentView.fitsSystemWindows = fitSystemWindows
        contentView.requestFitSystemWindows()
    }

    /**
     * 设置全屏
     */
    protected open fun overflowStatusBar(overflow: Boolean) {
        overflowStatusBarSinceLollipop(overflow)
    }
    private fun overflowStatusBarSinceLollipop(overflow: Boolean) {
        val window = window
        val decorView = window.decorView
        var option = decorView.systemUiVisibility
        option = if (overflow) {
            option or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        } else {
            option and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv() and View.SYSTEM_UI_FLAG_LAYOUT_STABLE.inv()
        }
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 状态栏亮色模式，状态栏字体颜色为黑色.
     */
    protected open fun lightStatusBar() {
        setSystemBarTheme(false)
    }

    /**
     * 状态栏暗色模式，状态栏字体颜色为白色.
     */
    protected open fun darkStatusBar() {
        setSystemBarTheme(true)
    }

    private fun getStatusBarHeight(activity: Activity): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Changes System Bar Theme.
     */
    protected fun setSystemBarTheme(isDark: Boolean) {


        //        val controller = ViewCompat.getWindowInsetsController(window.decorView)
//        controller?.isAppearanceLightStatusBars = isDark

//        // 设置状态栏反色
//        controller?.isAppearanceLightStatusBars = true
//        // 取消状态栏反色
//        controller?.isAppearanceLightStatusBars = false
//        // 设置导航栏反色
//        controller?.isAppearanceLightNavigationBars = true
//        // 取消导航栏反色
//        controller?.isAppearanceLightNavigationBars = false
//        // 隐藏状态栏
//        controller?.hide(WindowInsets.Type.statusBars())
//        // 显示状态栏
//        controller?.show(WindowInsets.Type.statusBars())
//        // 隐藏导航栏
//        controller?.hide(WindowInsets.Type.navigationBars())
//        // 显示导航栏
//        controller?.show(WindowInsets.Type.navigationBars())
//        // 同时隐藏状态栏和导航栏
//        controller?.hide(WindowInsets.Type.systemBars())
//        // 同时隐藏状态栏和导航栏
//        controller?.show(WindowInsets.Type.systemBars())

        // Fetch the current flags.
        val curFlags = window.decorView.systemUiVisibility
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        window.decorView.systemUiVisibility =
            if (isDark) curFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else curFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}