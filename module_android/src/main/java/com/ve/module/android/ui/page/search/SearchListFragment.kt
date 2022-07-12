package com.ve.module.android.ui.page.search

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.FragmentSearchListBinding
import com.ve.module.android.repository.model.Article
import com.ve.module.android.ui.adapter.ArticleAdapter
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.viewmodel.SearchViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment

class SearchListFragment : BaseVmListFragment<FragmentSearchListBinding, SearchViewModel, Article>() {

    companion object {
        fun getInstance(bundle: Bundle): SearchListFragment {
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachViewBinding(): FragmentSearchListBinding {
        return FragmentSearchListBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<SearchViewModel> {
        return SearchViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<Article, *> {
        return ArticleAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        mFloatingActionBtn=mBinding.fragmentRefreshLayout.floatingActionBtn

        mKey = arguments?.getString(Constant.SEARCH_KEY, "") ?: ""

    }

    private var mKey = ""
    override fun initWebData() {
        mViewModel.getSearchList(mCurrentPage, mKey)
    }

    override fun initObserver() {
        mViewModel.searchReslut.observe(this) {
            if (it.size == 0)
                showError("搜索结果为空")
            else
                showAtAdapter(it.datas)
        }
        mViewModel.collectState.observe(this) {
            if (it) {
                showMsg("收藏成功")
                mListAdapter.data[mPosition].collect = true
                mListAdapter.notifyItemChanged(mPosition)
            }
        }

        mViewModel.unCollectState.observe(this) {
            if (it) {
                showMsg("取消成功")
                mListAdapter.data[mPosition].collect = false
                mListAdapter.notifyItemChanged(mPosition)
            }
        }
    }

    override fun onItemClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        val article=datas[position]
        ArticleDetailActivity.start(requireContext(),article.title,article.link)
    }

    override fun onItemChildClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        showMsg("取消成功")
        if (datas[position].collect) {
            mViewModel.unCollect(datas[position].id)
        } else {
            mViewModel.collect(datas[position].id)
        }
    }
}