package com.ve.lib.common.base.view.list

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.listener.*
import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.view.ToastUtil

/**
 * @Author  weiyi
 * @Date 2022/9/18
 * @Description  current project lockit-android
 */
class ListViewListener {

    /**
     * ItemClickListener  item点击监听
     */
    var mItemClickListener: OnItemClickListener? = null

    /**
     * ItemChildClickListener  children item点击监听，需要设置 addChildClickViewIds()
     */
    var mItemChildClickListener: OnItemChildClickListener? = null

    /**
     * RefreshListener
     */
    var mRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    /**
     * LoadMoreListener
     */
    var mLoadMoreListener: OnLoadMoreListener? = null

    /**
     * 拖动
     */
    var mItemDragListener: OnItemDragListener? = null

    /**
     * 滑动监听
     */
    var mItemSwipeListener: OnItemSwipeListener? = null

    /**
     * 上划监听
     */
    var mUpFetchListener: OnUpFetchListener? = null


    init {

    }

    fun setListener(
        refreshListener: SwipeRefreshLayout.OnRefreshListener? = null,
        loadMoreListener: OnLoadMoreListener? = null,
        upFetchListener: OnUpFetchListener? = null,
        itemClickListener: OnItemClickListener? = null,
        itemChildClickListener: OnItemChildClickListener? = null,
        itemDragListener: OnItemDragListener? = null,
        itemSwipeListener: OnItemSwipeListener? = null,
    ): ListViewListener {
        refreshListener?.let {
            mRefreshListener = it
        }

        refreshListener?.let {
            mRefreshListener = it
        }
        loadMoreListener?.let {
            mLoadMoreListener = it
        }
        upFetchListener?.let {
            mUpFetchListener = it
        }
        itemClickListener?.let {
            mItemClickListener = it
        }
        itemChildClickListener?.let {
            mItemChildClickListener = it
        }
        itemDragListener?.let {
            mItemDragListener = it
        }
        itemSwipeListener?.let {
            mItemSwipeListener = it
        }
        return this
    }

    private fun contractListener() {

        /**
         * 默认的监听器
         */
        mItemClickListener = OnItemClickListener { adapter, view, position ->
            when (view.id) {
                else -> ToastUtil.show("you click ${view.id} item ")
            }
        }

        mItemChildClickListener = OnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                else -> ToastUtil.show("you click child ${view.id} item ")
            }
        }

        /**
         * 随配置修改
         */

        mRefreshListener = SwipeRefreshLayout.OnRefreshListener {

        }



        mLoadMoreListener = OnLoadMoreListener {

        }

        mItemDragListener = object : OnItemDragListener {
            //拖拽开始
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {

            }

            //拖拽结束
            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }
        }

        mItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }

            override fun onItemSwipeMoving(
                canvas: Canvas?,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {

            }

        }

        mUpFetchListener = OnUpFetchListener { LogUtil.msg("up fetch") }

    }

    enum class Config(
        /**
         * 开启刷新
         */
        var enableRefresh: Boolean = false,

        /**
         * 开启加载更多数据
         */
        var enableLoadMore: Boolean = false,

        /**
         * 当数据不满一页时，自动加载
         */
        var enableAutoLoadMore: Boolean = false,

        /**
         *     允许拖动
         */
        var enableDrag: Boolean = false,

        /**
         *     允许侧滑
         */
        var enableSwipe: Boolean = false,

        /**
         *     向上加载更多
         */
        var enableUpFetch: Boolean = false,
    ) {
        /**
         * 全部功能开启
         */
        ALL_OPEN(true, true, true, true, true, true),

        /**
         * 全部功能关闭
         */
        ALL_CLOSE(true, true, true, true, true, true),

        /**
         * 自定义功能
         */
        DIY(true, true, true, true, true, true),

        /**
         * 没有新数据
         */
        NO_NEW_DATA(true, true, true, true, true, true),

        /**
         * 只支持刷新和加载更多
         */
        REFRASH_AND_LOADMORE(true, true, true, true, true, true),
    }
}