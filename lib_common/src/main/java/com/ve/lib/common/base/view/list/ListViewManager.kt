package com.ve.lib.common.base.view.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.*
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @Author  weiyi
 * @Date 2022/9/18
 * @Description  current project lockit-android
 *
 * 装饰者模式 ListManager(context).onCreateListView(...).setListener(...).contact()
 */
open class ListViewManager<LD : Any>(context: Context) {

    var mRecyclerView: RecyclerView? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    lateinit var mListAdapter: BaseQuickAdapter<LD, out BaseViewHolder>
    lateinit var mListListener: ListViewListener

    /**
     * LinearLayoutManager 布局管理器
     */
    lateinit var mLinearLayoutManager: RecyclerView.LayoutManager

    /**
     * RecyclerView Divider 分割线
     */
    lateinit var mRecyclerViewItemDecoration: RecyclerView.ItemDecoration


    init {
        init(context)
    }

    /**
     * 调用 ListManager(context).onCreateListView(...).setListener(...).contact()
     */
    open fun init(context: Context) {
        mLinearLayoutManager = LinearLayoutManager(context)
        mRecyclerViewItemDecoration = SpaceItemDecoration(context)
        mListListener=ListViewListener()
    }

    open fun onCreateListView(
        recyclerView: RecyclerView?,
        swipeRefreshLayout: SwipeRefreshLayout?,
        listAdapter: BaseQuickAdapter<LD, out BaseViewHolder>,
    ): ListViewManager<LD> {
        mRecyclerView = recyclerView
        mSwipeRefreshLayout = swipeRefreshLayout
        mListAdapter = listAdapter
        return this
    }

    fun setListener(
        refreshListener: SwipeRefreshLayout.OnRefreshListener? = null,
        loadMoreListener: OnLoadMoreListener? = null,
        upFetchListener: OnUpFetchListener? = null,
        itemClickListener: OnItemClickListener? = null,
        itemChildClickListener: OnItemChildClickListener? = null,
        itemDragListener: OnItemDragListener? = null,
        itemSwipeListener: OnItemSwipeListener? = null,
    ): ListViewManager<LD> {
        mListListener.setListener(
            refreshListener,
            loadMoreListener,
            upFetchListener,
            itemClickListener,
            itemChildClickListener,
            itemDragListener,
            itemSwipeListener
        )
        return this
    }

    fun contract(): ListViewManager<LD> {
        contractAdapter()
        contractListView()
        return this
    }


    private fun contractAdapter() {
        mListAdapter.apply {
            //开启加载动画
            animationEnable = true
            //加载动画为左侧划入
            setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn)
            //绑定视图
            //recyclerView=mRecyclerView
            //设置空布局,调用此方法前需要 recyclerView.adapter=MAdapter
//            setEmptyView(R.layout.layout_empty_view)

            //允许拖拽
            mListListener.mItemDragListener?.let {
                draggableModule.isDragEnabled = true
                draggableModule.setOnItemDragListener(it)
            }

            //允许侧滑
            mListListener.mItemSwipeListener?.let {
                draggableModule.isSwipeEnabled = true
                draggableModule.setOnItemSwipeListener(it)
            }

            //下拉加载（符合聊天软件下拉历史数据需求）
            mListListener.mUpFetchListener?.let {
                upFetchModule.isUpFetchEnable = true
                upFetchModule.setOnUpFetchListener(it)
            }

            //加载更多,自动加载
            mListListener.mLoadMoreListener?.let {
                loadMoreModule.isEnableLoadMore = true
                loadMoreModule.isAutoLoadMore = false
                loadMoreModule.setOnLoadMoreListener(it)
            }

            //item点击，在fragment中完成，也可以在adapter中完成
            setOnItemClickListener(mListListener.mItemClickListener)
            //item子view点击，收藏
            setOnItemChildClickListener(mListListener.mItemChildClickListener)
        }
    }

    private fun contractListView() {
        //适配器绑定视图,不绑定不显示
        mRecyclerView?.apply {
            layoutManager = mLinearLayoutManager
            itemAnimator = DefaultItemAnimator()
            //添加分割线
            addItemDecoration(mRecyclerViewItemDecoration)
        }

        //下拉刷新
        mSwipeRefreshLayout?.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
            )

            setOnRefreshListener(mListListener.mRefreshListener)

        }
        //如果有fab

    }

    /**
     * 刷新全部数据 或者 追加数据
     *
     * @param isSetNewData Boolean  是否设置新数据，全刷新，否则追加数据，局部刷新
     * @param data List<T>  数据集合
     */
    open fun showAtAdapter(isSetNewData: Boolean, data: MutableList<LD>?) {

        if (mRecyclerView == null) {
            throw IllegalAccessException(" mRecyclerView 未初始化，无法执行 showAtAdapter")
        }
        //hideLoading
        mSwipeRefreshLayout?.isRefreshing = false

        mListAdapter.apply {
            if (isSetNewData) {
                //刷新数据，如果实现了loadModule。则会检查数据是否满一屏，如果满足条件，再开启 自动调用加载更多
                mListAdapter.data.clear()
                setNewInstance(data)
            } else {
                //添加数据
                if (data != null) {
                    addData(data)
                }
            }

            if (mListListener.mLoadMoreListener!=null) {

                // 处理加载更多    End/Complete（End：不会再触发上拉加载更多，Complete：还会继续触发上拉加载更多）
                if (data == null) {
                    // 加载更多结束（true：不展示「加载更多结束」的view，false则展示「没有更多数据」）
                    loadMoreModule.loadMoreEnd(true)
                } else {
                    // loadMoreComplete()  刷新完成 。设置auto loadModel则会自动调用加载更多
                    loadMoreModule.loadMoreComplete()
                }
            }
        }

    }

    var mFloatingActionBtn: FloatingActionButton? = null
    fun setFabView(floatingActionBtn: FloatingActionButton?): ListViewManager<LD> {
        mFloatingActionBtn = floatingActionBtn
        //如果有fab
        mFloatingActionBtn?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                scrollToTop(mRecyclerView)
            }
        }
        return this
    }

    private fun scrollToTop(mRecyclerView: RecyclerView?) {
        mRecyclerView?.run {
            if ((mLinearLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

}