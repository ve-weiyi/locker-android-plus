package com.ve.module.android.ui.page.main

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.databinding.FragmentSquareBinding
import com.ve.module.android.repository.model.Article
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.adapter.ArticleAdapter
import com.ve.module.android.ui.viewmodel.SquareViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment


/**
 * @Description 2.广场
 * @Author  weiyi
 * @Date 2022/3/20
 */
class SquareFragment : BaseVmListFragment<FragmentSquareBinding, SquareViewModel, Article>() {

    companion object {
        fun getInstance(): SquareFragment = SquareFragment()
    }

    override fun attachViewModelClass(): Class<SquareViewModel> {
        return SquareViewModel::class.java
    }

    /**
     * 得到绑定对象
     */
    override fun attachViewBinding(): FragmentSquareBinding {
        return FragmentSquareBinding.inflate(layoutInflater)
    }

    /**
     * step 1.初始化liveData.observe
     */
    override fun initObserver() {
        mViewModel.squareArticles.observe(this) {
            showAtAdapter(it)
        }
        mViewModel.collectState.observe(this) {
            if (it) {
                //由于加入header，所以pos+1
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

    override fun initListView() {
        //设置了该属性才能修改activity的toolbar
        setHasOptionsMenu(true)
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        mFloatingActionBtn=mBinding.fragmentRefreshLayout.floatingActionBtn
    }

    /**
     * step 3.初始化data相关
     */
    override fun initWebData() {
        mViewModel.getSquareList(mCurrentPage)
    }

    override fun attachAdapter(): BaseQuickAdapter<Article, *> {
        return ArticleAdapter()
    }

    override fun onItemClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        val intent = Intent(requireContext(), ArticleDetailActivity::class.java).apply {
            putExtra(ArticleDetailActivity.ARTICLE_URL_KEY, mListAdapter.data[position].link)
            putExtra(ArticleDetailActivity.ARTICLE_TITLE_KEY, mListAdapter.data[position].title)
        }
        startActivity(intent)
    }

    override fun onItemChildClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
        val data= mListAdapter.data
        mPosition = position
        if (data[position].collect) {
            mViewModel.unCollect(data[position].id)
        } else {
            mViewModel.collect(data[position].id)
        }
    }
}
