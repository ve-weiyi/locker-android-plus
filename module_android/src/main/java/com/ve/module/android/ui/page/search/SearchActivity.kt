package com.ve.module.android.ui.page.search

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListActivity
import com.ve.lib.common.utils.data.ColorUtil
import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.ui.DisplayUtil
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.ActivitySearchBinding
import com.ve.module.android.repository.bean.Hotkey
import com.ve.module.android.repository.database.entity.SearchHistory
import com.ve.module.android.ui.adapter.SearchHistoryAdapter
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.viewmodel.SearchViewModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/21
 */
class SearchActivity: BaseVmListActivity<ActivitySearchBinding, SearchViewModel, SearchHistory>(){
    override fun attachViewBinding(): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<SearchViewModel> {
        return SearchViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<SearchHistory, *> {
        return SearchHistoryAdapter()
    }

    override fun initListView() {
        mRecyclerView = mBinding.rvHistorySearch

    }

    override fun initView() {
        super.initView()
        mListAdapter.apply {
            setEmptyView(R.layout.search_empty_view)
        }
        initToolbar(mBinding.extToolbar.toolbar,"搜索")
    }
    private lateinit var mKey: String
    private lateinit var mEditText: EditText
    lateinit var searchView :SearchView
    /**
     * 热搜数据
     */
    private var mHotSearchDatas = mutableListOf<Hotkey>()

    override fun initObserver() {
        mViewModel.hotkeyList.observe(this){
            showHotkey(it)
        }

        mViewModel.historyList.observe(this) {
            showHistory(it.toMutableList())
           // showAtAdapter(it.toMutableList())
        }
    }

    override fun loadWebData() {
        mViewModel.getHotkey()
        mViewModel.getHistorySearch()
    }


    override fun initListener() {
        mBinding.hotSearchFlowLayout.run {
            setOnTagClickListener { view, position, parent ->
                if (mHotSearchDatas.size > 0) {
                    val Hotkey = mHotSearchDatas[position]
                    goToSearchList(Hotkey.name)
                    true
                }
                false
            }
        }
        mBinding.searchHistoryClearAllTv.setOnClickListener {
            mListAdapter.setList(mutableListOf())
            mViewModel.cleanHistorySearch()
        }
    }
    /**
     * 添加SearchView
     * SearchView使用参考：https://blog.csdn.net/yechaoa/article/details/80658940
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //引用menu文件
        menuInflater.inflate(R.menu.menu_search_flow, menu)
        //找到SearchView并配置相关参数
        searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.apply {
            //搜索图标是否显示在搜索框内
            setIconifiedByDefault(true)
            //设置搜索框展开时是否显示提交按钮，可不显示
            isSubmitButtonEnabled = true
            //让键盘的回车键设置成搜索
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            //搜索框是否展开，false表示展开
            isIconified = false
            //获取焦点
            isFocusable = true
            requestFocusFromTouch()
            //设置提示词
            queryHint = getString(R.string.search_tint)

            searchView.maxWidth = Integer.MAX_VALUE
            //当此视图作为操作视图展开时调
            searchView.onActionViewExpanded()
            //设置搜索文本监听
            setOnQueryTextListener(queryTextListener)
        }
        val typedValue = TypedValue()
        theme.resolveAttribute(com.ve.lib.application.R.attr.sc_text_light, typedValue, true)
        val color = typedValue.data
        //设置输入框文字颜色
        mEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
        mEditText.setHintTextColor(color)
        mEditText.setTextColor(color)


        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(com.ve.lib.application.R.drawable.ic_baseline_search_24)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun showHistory(historyBeans: MutableList<SearchHistory>) {
        mListAdapter.setList(historyBeans)
    }

    private fun showHotkey(hotSearchDatas: MutableList<Hotkey>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        mBinding.hotSearchFlowLayout
        mBinding.hotSearchFlowLayout.adapter = object : TagAdapter<Hotkey>(hotSearchDatas) {
            override fun getView(parent: FlowLayout?, position: Int, Hotkey: Hotkey?): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                    R.layout.flow_layout_tv,
                    mBinding.hotSearchFlowLayout, false
                ) as TextView
                val padding: Int = DisplayUtil.dip2px(10F)
                tv.setPadding(padding, padding, padding, padding)
                tv.text = Hotkey?.name
                tv.setTextColor(ColorUtil.randomColor())
                tv.setBackgroundColor(Color.LTGRAY)
                return tv
            }
        }


    }

    override fun onItemClickEvent(datas: MutableList<SearchHistory>, view: View, position: Int) {
        goToSearchList(datas[position].name!!)
    }

    override fun onItemChildClickEvent(datas: MutableList<SearchHistory>, view: View, position: Int) {
        when (view.id) {
            R.id.iv_clear -> {
                mViewModel.deleteHistorySearch(datas[position].name!!)
                mListAdapter.removeAt(position)
            }
        }
    }

    /**
     * OnQueryTextListener 设置搜索文本监听
     */
    private val queryTextListener = object : SearchView.OnQueryTextListener {
        // 当点击搜索按钮时触发该方法
        override fun onQueryTextSubmit(query: String): Boolean {
            LogUtil.msg( "搜索内容===$query")
            mKey = query
            mCurrentPage = 0 //重置分页，避免二次加载分页混乱
            //搜索请求
            goToSearchList(mKey)
            //清除焦点，收软键盘
            searchView.clearFocus()
            return false
        }
        // 当搜索内容改变时触发该方法
        override fun onQueryTextChange(newText: String): Boolean {
            //当没有输入任何内容的时候显示搜索热词，看实际需求
            return false
        }
    }

    private fun goToSearchList(key: String) {
        //保存历史搜索
        mViewModel.saveHistorySearch(key)
        //转到搜索结果界面
        val bundle=Bundle()
        bundle.putString(Constant.SEARCH_KEY, key)
        CommonActivity.start(this, key, SearchListFragment::class.java.name, bundle)
    }

}