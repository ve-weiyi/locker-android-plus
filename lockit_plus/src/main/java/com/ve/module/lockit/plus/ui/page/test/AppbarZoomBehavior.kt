package com.ve.module.lockit.plus.ui.page.test

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.ext.dp2px
import com.ve.lib.common.ext.px2dp
import com.ve.module.lockit.plus.R

/**
 * create by libo
 * create on 2020-05-22
 * description
 */
class AppbarZoomBehavior(context: Context?, attrs: AttributeSet?) : AppBarLayout.Behavior(context, attrs) {
    private var mImageView: ImageView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    private var mAppbarHeight //记录AppbarLayout原始高度
            = 0
    private var mImageViewHeight //记录ImageView原始高度
            = 0
    private var mTotalDy //手指在Y轴滑动的总距离
            = 0f
    private var mScaleValue //图片缩放比例
            = 0f
    private var mLastBottom //Appbar的变化高度
            = 0
    private var isAnimate //是否做动画标志
            = false

    companion object {
        private const val MAX_ZOOM_HEIGHT = 200f //放大最大高度
    }

    /**
     * AppBarLayout布局时调用
     *
     * @param parent 父布局CoordinatorLayout
     * @param abl 使用此Behavior的AppBarLayout
     * @param layoutDirection 布局方向
     * @return 返回true表示子View重新布局，返回false表示请求默认布局
     */
    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout, layoutDirection: Int): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        init(abl)
        return handled
    }

    /**
     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     *
     * @param abl
     */
    private fun init(abl: AppBarLayout) {
        abl.clipChildren = false
        mAppbarHeight = abl.height


        mImageView = abl.findViewById(R.id.appbar_image)
        if (mImageView != null) {
            mImageViewHeight = mImageView!!.height
        }
//        mSwipeRefreshLayout=abl.findViewById(R.id.appbar_refresh)
        mSwipeRefreshLayout?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light
        )

    }

    /**
     * 当CoordinatorLayout的子View尝试发起嵌套滚动时调用
     *
     * @param parent 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes 嵌套滚动的方向
     * @return 返回true表示接受滚动
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        isAnimate = true
        return true
    }

    /**
     * 当嵌套滚动已由CoordinatorLayout接受时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes 嵌套滚动的方向
     */
    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type)
    }

    /**
     * 当准备开始嵌套滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param dx 用户在水平方向上滑动的像素数
     * @param dy 用户在垂直方向上滑动的像素数
     * @param consumed 输出参数，consumed[0]为水平方向应该消耗的距离，consumed[1]为垂直方向应该消耗的距离
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        // 1.mTargetView不为null
        // 2.是向下滑动，dy<0表示向下滑动
        // 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
        if (mImageView != null && child.bottom >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
            zoomHeaderImageView(child, dy)
        } else {
            //向上滑动
            if (mImageView != null && child.bottom >= mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                consumed[1] = dy
                zoomHeaderImageView(child, dy)
            } else {
                if (valueAnimator == null || !valueAnimator!!.isRunning) {
                    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
                }
            }
        }
    }


    /**
     * 当停止滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param abl 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     */
    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
        recovery(abl)
        LogUtil.msg("isRefreshing")
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    /**
     * 当嵌套滚动的子View准备快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @return 如果Behavior消耗了快速滚动返回true
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (velocityY > 100) {
            isAnimate = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    var valueAnimator: ValueAnimator? = null

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     *
     * @param abl
     */
    private fun recovery(abl: AppBarLayout) {
        if (mTotalDy > 0) {
            mTotalDy = 0f
            if (isAnimate) {
                valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220)
                valueAnimator?.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    scaleView(abl, value)

                    abl.bottom = (mLastBottom - (mLastBottom - mAppbarHeight) * animation.animatedFraction).toInt()
                }
                valueAnimator?.start()
            } else {
                scaleView(abl, 1f)

                abl.bottom = mAppbarHeight
            }
        }
    }

    /**
     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
     *
     * @param abl
     * @param dy
     */
    private fun zoomHeaderImageView(abl: AppBarLayout, dy: Int) {
        mTotalDy += -dy.toFloat()
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT)
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT)

        scaleView(abl, mScaleValue)

        mLastBottom = mAppbarHeight + (mImageViewHeight / 2 * (mScaleValue - 1)).toInt()
        abl.bottom = mLastBottom
    }

    private fun scaleView(abl: AppBarLayout, scaleValue: Float) {
        val collapsingToolbarLayout = abl.findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val params = AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT,
            mAppbarHeight * scaleValue.toInt()
        )

//        collapsingToolbarLayout.layoutParams = params

//        abl.bottom = (mAppbarHeight * scaleValue).toInt()
        mImageView?.scaleX = scaleValue
        mImageView?.scaleY = scaleValue
    }

}