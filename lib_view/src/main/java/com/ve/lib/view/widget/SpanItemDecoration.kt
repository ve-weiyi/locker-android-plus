package com.ve.lib.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * @author waynie
 * @date 2022/9/22
 * @desc EufyHomeNew
 */
class SpanItemDecoration(
    context: Context,
    val orientation: Int,
    val padding: Int,
    @DrawableRes dividerRes: Int
) : RecyclerView.ItemDecoration() {

    val outLeft: Int = 0
    val outRight: Int = 0
    val outTop: Int = 0
    val outBottom: Int = 0

    val paint=Paint()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
    canvas.save()
        val left: Int = padding
        val right: Int = parent.width - padding

        //获取屏幕可见的view数量
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            //计算分割线起始位置
            val child: View = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            // divider的top 应该是 item的bottom 加上 marginBottom 再加上 Y方向上的位移  -->计算起始y坐标
            val top = child.bottom + params.bottomMargin + child.translationY
            // divider的bottom就是top加上divider的高度了
            val bottom = (top + 1)         //计算结束y坐标
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint) //画笔自定义
        }

        when (parent.layoutManager) {
            is GridLayoutManager -> {

            }

            is StaggeredGridLayoutManager -> {

            }


            is LinearLayoutManager -> {


            }

        }
    }


    /**
     * getItemOffsets的主要作用就是给item的四周加上边距，实现的效果类似于margin，
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.layoutManager) {
            is GridLayoutManager -> getGridLayoutItemOffsets(
                outRect,
                view,
                parent,
                state,
                (parent.layoutManager as GridLayoutManager).spanCount
            )
            is StaggeredGridLayoutManager -> getStaggeredGridHorizontalItemOffsets(
                outRect,
                view,
                parent,
                state,
                (parent.layoutManager as StaggeredGridLayoutManager).spanCount,
                (parent.layoutManager as StaggeredGridLayoutManager).orientation
            )
            is LinearLayoutManager -> getLinearLayoutItemOffsets(
                outRect,
                view,
                parent,
                state,
                (parent.layoutManager as LinearLayoutManager).orientation
            )
        }
    }

    /***
     * 针对LinearLayoutManager的边距
     */
    private fun getLinearLayoutItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        orientation: Int
    ) {
        val itemCount = parent.adapter?.itemCount ?: 0
        val currentCount = parent.getChildAdapterPosition(view)
        when (orientation) {
            LinearLayoutManager.HORIZONTAL -> outRect.set(
                outLeft,
                outTop,
                if (itemCount - 1 == currentCount) outRight else 0,
                outBottom
            )
            LinearLayoutManager.VERTICAL -> outRect.set(
                outLeft,
                outTop,
                outRight,
                if (itemCount - 1 == currentCount) outBottom else 0
            )
        }
    }

    /***
     * 针对GridLayoutManager 顶部水平的GridLayout
     */
    private fun getGridLayoutItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        spanCount: Int
    ) {
        val itemCount = parent.adapter?.itemCount ?: 0
        val currentCount = parent.getChildAdapterPosition(view)
        //每一行的最后一个View才需要添加right
        val lastOfLine = currentCount % spanCount == spanCount - 1 || currentCount == itemCount - 1
        outRect.set(
            outLeft,
            outTop,
            if (lastOfLine) outRight else 0,
            if (lastOfLine) outBottom else 0
        )
    }

    /***
     * 针对StaggeredGridLayoutManager的设置边距离  交错的GridLayout
     */
    private fun getStaggeredGridHorizontalItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
        spanCount: Int,
        orientation: Int
    ) {
        val params = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex
        val lastOfLine = spanIndex == spanCount - 1
        when (orientation) {
            RecyclerView.HORIZONTAL -> {
                outRect.set(outLeft, outTop, 0, if (lastOfLine) outBottom else 0)
            }
            RecyclerView.VERTICAL -> {
                outRect.set(outLeft, outTop, if (lastOfLine) outRight else 0, 0)
            }
        }

    }
}

