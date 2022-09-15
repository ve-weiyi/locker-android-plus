package com.ve.lib.common.ext

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.Checkable
import android.widget.ImageView


/**
 * ImageView旋转动画 180°
 */
fun View.startImageRotate(imageView: ImageView, toggle: Boolean) {
    val tarRotate: Float = if (toggle) 0f else 180f
    apply {
        ObjectAnimator.ofFloat(this, "rotation", rotation, tarRotate).let {
            it.duration = 300
            it.start()
        }
    }
}

/**
 * 设置view 背景 圆角
 */
fun View.setRoundRectBg(color: Int = Color.WHITE, radius: Float = 10f) {
    background = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
    }
}

// 扩展点击事件属性(重复点击时长)
var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

// 重复点击事件绑定
inline fun <T : View> T.setSingleClickListener(time: Long = 1000, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

/**
 * 防止重复点击
 */
var lastClickTime = 0L
var interval: Long = 500
fun View.setOnclickNoRepeat(onClick: (View) -> Unit,) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (com.ve.lib.common.ext.lastClickTime != 0L && (currentTime - com.ve.lib.common.ext.lastClickTime < interval)) {
            return@setOnClickListener
        }
        com.ve.lib.common.ext.lastClickTime = currentTime
        onClick.invoke(it)
    }
}

fun View.setOnclickNoRepeatListener( listener:View.OnClickListener) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (com.ve.lib.common.ext.lastClickTime != 0L && (currentTime - com.ve.lib.common.ext.lastClickTime < interval)) {
            return@setOnClickListener
        }
        com.ve.lib.common.ext.lastClickTime = currentTime
        listener.onClick(it)
    }
}