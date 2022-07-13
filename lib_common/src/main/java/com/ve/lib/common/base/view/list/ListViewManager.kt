package com.ve.lib.common.base.view.list

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.*
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ve.lib.common.R
import com.ve.lib.common.exception.BizException
import com.ve.lib.common.widget.SpaceItemDecoration
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.ToastUtil

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 *
 * 调用方式：
 * ListViewManager(context,ListViewConfig())
 * ListViewManager.setListListener()
 * ListViewManager.setListView()
 */

open class ListViewManager<LD> {

    lateinit var mConfig: ListViewConfig

    var mRecyclerView: RecyclerView? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var mFloatingActionBtn: FloatingActionButton? = null
    lateinit var mListAdapter: BaseQuickAdapter<LD, out BaseViewHolder>


    /**
     * LinearLayoutManager 布局管理器
     */
    lateinit var mLinearLayoutManager: LinearLayoutManager

    /**
     * RecyclerView Divider 分割线
     */
    lateinit var mRecyclerViewItemDecoration: SpaceItemDecoration

    /**
     * LoadMoreListener
     */
    lateinit var mLoadMoreListener: OnLoadMoreListener

    /**
     * RefreshListener
     */
    lateinit var mRefreshListener: SwipeRefreshLayout.OnRefreshListener

    /**
     * ItemClickListener  item点击监听
     */
    lateinit var mItemClickListener: OnItemClickListener

    /**
     * ItemChildClickListener  children item点击监听，需要设置 addChildClickViewIds()
     */
    lateinit var mItemChildClickListener: OnItemChildClickListener


    lateinit var mItemDragListener: OnItemDragListener

    lateinit var mItemSwipeListener: OnItemSwipeListener

    lateinit var mUpFetchListener: OnUpFetchListener

    constructor(context: Context) :this(context,ListViewConfig.REFRASH_AND_LOADMORE,null){

    }

    constructor(context: Context, model:Int,config: ListViewConfig?=null)  {
        init(context)
        initConfig(model,config)
    }

    private fun init(context: Context) {
        mLinearLayoutManager = LinearLayoutManager(context)
        mRecyclerViewItemDecoration = SpaceItemDecoration(context)
    }

    private fun initConfig(model:Int, config: ListViewConfig?=null){
        when(model){
            ListViewConfig.ALL_OPEN->{
                mConfig = ListViewConfig(
                    enableRefresh = true,
                    enableLoadMore = true,
                    enableDrag = true,
                    enableSwipe = true,
                    enableUpFetch = true,
                    enableAutoLoadMore = true,
                )
            }
            ListViewConfig.ALL_CLOSE->{
                mConfig = ListViewConfig(
                    enableRefresh = false,
                    enableLoadMore = false,
                    enableAutoLoadMore = false,
                    enableDrag = false,
                    enableSwipe = false,
                    enableUpFetch = false,
                )
            }
            ListViewConfig.DIY->{
                config?.let {
                    mConfig=config
                }
            }
            ListViewConfig.NO_NEW_DATA->{
                mConfig = ListViewConfig(
                    enableDrag = true,
                    enableSwipe = true,
                    enableUpFetch = true,
                )
            }
            ListViewConfig.REFRASH_AND_LOADMORE->{
                mConfig = ListViewConfig(
                    enableRefresh = true,
                    enableLoadMore = true,
                )
            }
            else->{

                mConfig=config?:ListViewConfig()

            }
        }

        contractConfig()
    }

    fun initListener(
        refreshListener: SwipeRefreshLayout.OnRefreshListener? = null,
        loadMoreListener: OnLoadMoreListener? = null,
        upFetchListener: OnUpFetchListener? = null,
        itemClickListener: OnItemClickListener? = null,
        itemChildClickListener: OnItemChildClickListener? = null,
        itemDragListener: OnItemDragListener? = null,
        itemSwipeListener: OnItemSwipeListener? = null,
    ): ListViewManager<LD> {
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

    /**
     * 必要的初始化： mLayoutStatusView、mRecyclerView、mSwipeRefreshLayout,mListAdapter
     */
    fun initListView(
        recyclerView: RecyclerView?,
        swipeRefreshLayout: SwipeRefreshLayout?,
        floatingActionBtn: FloatingActionButton?,
        listAdapter: BaseQuickAdapter<LD, out BaseViewHolder>,
    ): ListViewManager<LD> {
        mRecyclerView = recyclerView
        mSwipeRefreshLayout = swipeRefreshLayout
        mFloatingActionBtn = floatingActionBtn
        mListAdapter=listAdapter
        return this
    }

    fun contract() {
        contractAdapter()
        contractListView()
    }

    private fun contractConfig(){
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
        if (mConfig.enableRefresh) {
            mRefreshListener = SwipeRefreshLayout.OnRefreshListener {

            }
        }

        if (mConfig.enableLoadMore) {
            mLoadMoreListener = OnLoadMoreListener {
                val status=1
                mListAdapter.apply {
                    when(status){
                        //状态-》成功，失败，到结尾
                        1->loadMoreModule.loadMoreComplete()
                        2->loadMoreModule.loadMoreFail()
                        3->loadMoreModule.loadMoreEnd()
                    }
                }
            }
        }

        if (mConfig.enableDrag) {
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
            if (mConfig.enableSwipe) {
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
            }
        }

        if (mConfig.enableUpFetch) {
            mUpFetchListener = object : OnUpFetchListener {
                override fun onUpFetch() {
                    LogUtil.msg("up fetch")
                }
            }
        }
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
            setEmptyView(R.layout.layout_empty_view)

            //允许侧滑、拖动
            if (mConfig.enableDrag) {
                draggableModule.isSwipeEnabled = mConfig.enableSwipe
                draggableModule.isDragEnabled = mConfig.enableDrag
            }
            //
            if (mConfig.enableUpFetch) {
                upFetchModule.isUpFetchEnable = mConfig.enableUpFetch
            }
            //自动加载
            if (mConfig.enableLoadMore) {
                loadMoreModule.isEnableLoadMore = mConfig.enableLoadMore
                loadMoreModule.isAutoLoadMore = mConfig.enableAutoLoadMore
                loadMoreModule.setOnLoadMoreListener(mLoadMoreListener)
            }
            //加载更多

            //item点击，在fragment中完成，也可以在adapter中完成
            setOnItemClickListener(mItemClickListener)
            //item子view点击，收藏
            setOnItemChildClickListener(mItemChildClickListener)
        }
    }

    private fun contractListView() {

        //适配器绑定视图,不绑定不显示
        mRecyclerView?.apply {
            layoutManager = mLinearLayoutManager
            itemAnimator = DefaultItemAnimator()
            //添加分割线
//            addItemDecoration(mRecyclerViewItemDecoration)
        }

        //下拉刷新
        mSwipeRefreshLayout?.apply {
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
            )
            if(mConfig.enableRefresh){
                setOnRefreshListener(mRefreshListener)
            }
        }

        //如果有fab
        mFloatingActionBtn?.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                scrollToTop(mRecyclerView)
            }
        }
    }

    //滑动顶部
    private fun scrollToTop(mRecyclerView: RecyclerView?) {
        mRecyclerView?.run {
            if (mLinearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    /**
     * 刷新全部数据 或者 追加数据
     *
     * @param isSetNewData Boolean  是否设置新数据，全刷新，否则追加数据，局部刷新
     * @param data List<T>  数据集合
     */
    open fun showAtAdapter(isSetNewData: Boolean, data: MutableList<LD>?) {
//        LogUtil.e("isNewData:$isSetNewData ")
//        LogUtil.e("data:$data ")

        if (mRecyclerView == null) {
            throw BizException(" mRecyclerView 未初始化，无法执行 showAtAdapter")
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

//            LogUtil.msg("加载更多 " + mConfig.enableLoadMore)

            if (mConfig.enableLoadMore) {

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

}