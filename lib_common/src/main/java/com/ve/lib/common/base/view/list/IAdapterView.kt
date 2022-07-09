package com.ve.lib.common.base.view.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author ve
 * @date 2021/2/26
 * @desc 带有List的fragment接口
 */
interface IAdapterView<LD : Any> {

    var mPosition: Int    //当前点击位置

    val mListAdapter: BaseQuickAdapter<LD, out BaseViewHolder>
    var mListViewManager: ListViewManager<LD>?

    open fun getAdapter(): RecyclerView.Adapter<*> {
        return mListAdapter
    }

    abstract fun attachAdapter(): BaseQuickAdapter<LD, *>


    open fun onItemClickEvent(datas: MutableList<LD>, view: View, position: Int) {

    }

    open fun onItemChildClickEvent(datas: MutableList<LD>, view: View, position: Int) {

    }

    /**
     * LoadMoreListener 上拉加载更多,在适配器处使用
     */
    fun onLoadMoreList(): OnLoadMoreListener

    fun getMoreData()

}

