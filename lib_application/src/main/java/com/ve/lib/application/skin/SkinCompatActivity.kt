package com.ve.lib.application.skin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.ve.lib.application.R

open class SkinCompatActivity: AppCompatActivity() {

    var flag=true
    protected  var mThemeColor:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        SkinFactory.setTheme(this)
        super.onCreate(savedInstanceState)
        mThemeColor= getThemeColor(this,R.attr.colorBackground,Color.CYAN)
        //沉浸式状态栏
        setColor(this, mThemeColor, 0)
    }

    private fun toggleTheme() {
        SkinFactory.changeTheme(this)
    }

    fun onChangeTheme2() {
        toggleTheme()
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
}