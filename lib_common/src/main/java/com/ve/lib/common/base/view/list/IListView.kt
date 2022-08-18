package com.ve.lib.common.base.view.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ve.lib.common.utils.log.LogUtil

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
    var mPageSize: Int    //当前加载数量，为bean返回结果的大小。即it=data.size，当it<mTotalCount表示数据已到达结尾
    var mCurrentPage: Int //当前加载页数，刷新页面数据时重置为0 ,加载更多时it++
    var mPosition: Int    //当前点击位置


    var mRecyclerView: RecyclerView?
    var mSwipeRefreshLayout: SwipeRefreshLayout?
    var mFloatingActionBtn: FloatingActionButton?

    var mListAdapter: BaseQuickAdapter<LD, out BaseViewHolder>
    var mListViewManager: ListViewManager<LD>

    /**
     * 如果有自定义的适配器不是BaseQuickAdapter, 可以在初始化后  mRecyclerView?.adapter=Adapter()
     */
    abstract fun attachAdapter(): BaseQuickAdapter<LD, *>

    fun attachListManager(context: Context): ListViewManager<LD> {
        return ListViewManager(context)
    }

    /**
     * RefreshListener  下拉刷新页面
     */
    abstract fun getRefreshData()

    /**
     * LoadMoreListener 上拉加载更多,在适配器处使用
     */
    abstract fun getMoreData()

    abstract fun onItemClickEvent(datas: MutableList<LD>, view: View, position: Int)

    abstract fun onItemChildClickEvent(datas: MutableList<LD>, view: View, position: Int)


    /**
     * 初始化ListView相关, 初始化 recyclerview，swipeRefreshView 相关数据，应该在initView之前调用
     */
    fun defaultListView(context: Context) {
        mTotalCount = 20
        mPageSize = 0
        mCurrentPage = 0

        mListAdapter = attachAdapter()
        mListViewManager = attachListManager(context)

        mListViewManager
            .initListener(
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
            .initListView(
                mRecyclerView,
                mSwipeRefreshLayout,
                mFloatingActionBtn,
                mListAdapter
            )
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

//        LogUtil.e("isNewData:$isSetNewData ")
//        LogUtil.e("data:$data ")
//
//        if (mRecyclerView == null) {
//            throw BizException(" mRecyclerView 未初始化，无法执行 showAtAdapter")
//        }
//        //hideLoading
//        mSwipeRefreshLayout?.isRefreshing = false
//
//        mListAdapter.apply {
//            if (isSetNewData) {
//                //刷新数据，如果实现了loadModule。则会检查数据是否满一屏，如果满足条件，再开启 自动调用加载更多
//                mListAdapter.data.clear()
//                setNewInstance(data)
//            } else {
//                //添加数据
//                if (data != null) {
//                    addData(data)
//                }
//            }
//
//            LogUtil.msg("加载更多 " + mListViewManager!!.mConfig.enableLoadMore)
//
//            if (mListViewManager!!.mConfig.enableLoadMore) {
//
//                // 处理加载更多    End/Complete（End：不会再触发上拉加载更多，Complete：还会继续触发上拉加载更多）
//                if (data == null) {
//                    // 加载更多结束（true：不展示「加载更多结束」的view，false则展示「没有更多数据」）
//                    loadMoreModule.loadMoreEnd(true)
//                } else {
//                    // loadMoreComplete()  刷新完成 。设置auto loadModel则会自动调用加载更多
//                    loadMoreModule.loadMoreComplete()
//                }
//            }
//        }

    }

    fun showAtAdapter(data: MutableList<LD>?) {
        showAtAdapter(mCurrentPage == 0, data)
    }
}

