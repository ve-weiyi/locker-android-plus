package com.ve.module.locker.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CanvasDemo(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mPaint: Paint? = null
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 第一次绘制：绘制矩形
         */
        canvas.drawRect(80f, 80f, 200f, 150f, mPaint!!) //通过draw方法，将内容显示在屏幕上，此次画布的任务已经完成
        /**
         * 第二次绘制：先平移，再绘制圆
         */
        canvas.translate(300f, 300f) //画布还是原来的画布，只是将它平移。内容是在屏幕上的，所以画布平移不影响第一次绘制的内容
        mPaint!!.color = Color.GREEN //改变画笔的颜色
        canvas.drawCircle(50f, 50f, 100f, mPaint!!) //通过draw方法，将内容显示在屏幕上，此次画布的任务已经完成。（这里的坐标多是相对画布而言）
    }

    /**
     * 直接在xml中定义并显示，所以只实现该构造函数即可
     * @param context
     * @param attrs
     */
    init {
        mPaint = Paint()
    }
}