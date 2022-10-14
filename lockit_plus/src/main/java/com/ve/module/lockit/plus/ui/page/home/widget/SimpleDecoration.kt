package com.ve.module.lockit.plus.ui.page.home.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * @author anker
 */
class SimpleDecoration(
    context: Context,
    orientation: Int,
    padding: Int,
    @DrawableRes dividerRes: Int
) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable?
    private var mOrientation = 0
    private val mBounds = Rect()
    private val padding: Int
    private var enableDrawBackgroundColor = false

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val TAG = "DividerItem"
        private val ATTRS = intArrayOf(16843284)
    }

    init {
        this.padding = (context.resources.displayMetrics.density * padding).toInt()
        setOrientation(orientation)
        mDivider = ContextCompat.getDrawable(context, dividerRes)
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        if (mDivider == null) {
            Log.w(
                "DividerItem",
                "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
    }

    fun setOrientation(orientation: Int) {
        require(!(orientation != 0 && orientation != 1)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        mOrientation = orientation
    }

    fun setEnableDrawBackgroundColor(enable: Boolean) {
        enableDrawBackgroundColor = enable
    }

    fun setDrawable(drawable: Drawable) {
        requireNotNull(drawable) { "Drawable cannot be null." }
        mDivider = drawable
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager != null && mDivider != null) {
            if (mOrientation == 1) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left = padding
        val right = parent.width - padding
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider!!.intrinsicHeight
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + Math.round(child.translationX)
            val left = right - mDivider!!.intrinsicWidth
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //这个设置的意思是，在Item的底部加上一段距离，让分隔线画在这个范围
        //不设置的话，分隔线会画到Item的视图上，造成重合
        //如果你的onDraw没有给出自己的实现，只是通过这个方法设置一块区域，那么也会出现一段距离，背景色是这个界面的背景色
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
        }
    }


}