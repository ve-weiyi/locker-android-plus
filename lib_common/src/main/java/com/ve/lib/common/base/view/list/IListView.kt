package com.ve.lib.common.base.view.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ve.lib.common.utils.system.LogUtil

/**
 * @author ve
 * @date 2021/2/26
 * @desc 带有List的fragment接口
 * 必要的初始化： mLayoutStatusView、mRecyclerView、mSwipeRefreshLayout
 */
interface IListView<LD : Any> {

    /**
     * 分页参数，每页数据的个数
     */
    var mTotalCount: Int  // 服务器可返回数据的总数
    var mCurrentPage: Int //当前加载页数，刷新页面数据时重置为0 ,加载更多时it++
    var mOnePageSize: Int //当前加载数量，为bean返回结果的大小。即it=data.size，当data.size<mOnePageSize表示数据已到达结尾
    var mPosition: Int    //当前点击位置


    var mRecyclerView: RecyclerView?
    var mSwipeRefreshLayout: SwipeRefreshLayout?
    var mFloatingActionBtn: FloatingActionButton?

    var mListAdapter: BaseQuickAdapter<LD, out BaseViewHolder>
    var mListViewManager: ListViewManager<LD>


    fun attachListManager(context: Context): ListViewManager<LD> {
        return ListViewManager(context)
    }

    /**
     * 初始化ListView相关, 初始化 recyclerview，swipeRefreshView 相关数据，应该在initView之前调用
     */
    fun onCreateListView(context: Context) {
        mTotalCount = 100
        mOnePageSize = 20
        mCurrentPage = 0
        mPosition = 0

        mListAdapter = attachAdapter()
        mListViewManager = attachListManager(context)
            .onCreateListView(mRecyclerView, mSwipeRefreshLayout, mListAdapter)
            .setListener(
                refreshListener = {
                    mSwipeRefreshLayout?.postDelayed({
                        getRefreshData()
                        LogUtil.msg("refresh page at : " + javaClass.simpleName)
                    }, 1500)
                },
                loadMoreListener = {
                    mRecyclerView?.postDelayed({
                        //刷新视图是否应显示刷新进度，关闭刷新loading
                        mSwipeRefreshLayout?.isRefreshing = false

                        getMoreData()
                        LogUtil.msg("Load more at : " + javaClass.simpleName)
                    }, 500)
                },

                itemClickListener = { adapter, view, position ->
                    mPosition = position
                    onItemClickEvent(mListAdapter.data, view, position)
                },

                itemChildClickListener = { adapter, view, position ->
                    mPosition = position
                    onItemChildClickEvent(mListAdapter.data, view, position)
                }
            )
            .setFabView(mFloatingActionBtn)
            .contract()

        //在最后设置adapter ，否则会出现错误
        mRecyclerView?.adapter = mListAdapter
    }

    /**
     * 刷新全部数据 或者 追加数据
     *
     * @param isSetNewData Boolean  是否设置新数据，全刷新，否则追加数据，局部刷新
     * @param data List<T>  数据集合
     */
    open fun showAtAdapter(isSetNewData: Boolean, data: MutableList<LD>?) {
        mListViewManager.showAtAdapter(isSetNewData, data)
    }

    fun showAtAdapter(data: MutableList<LD>?) {
        showAtAdapter(mCurrentPage == 0, data)
    }

    /**
     * 如果有自定义的适配器不是BaseQuickAdapter, 可以在初始化后  mRecyclerView?.adapter=Adapter()
     */
    abstract fun attachAdapter(): BaseQuickAdapter<LD, *>

    /**
     * RefreshListener  下拉刷新页面
     */
    abstract fun getRefreshData()

    /**
     * LoadMoreListener 上拉加载更多,在适配器处使用
     */
    abstract fun getMoreData()

    /**
     * 点击事件
     */
    abstract fun onItemClickEvent(datas: MutableList<LD>, view: View, position: Int)

    /**
     * 子控件点击事件
     */
    abstract fun onItemChildClickEvent(datas: MutableList<LD>, view: View, position: Int)
}

