package com.ve.module.android.ui.page.main.child

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.databinding.FragmentRefreshLayoutBinding
import com.ve.module.android.repository.bean.Article
import com.ve.module.android.ui.adapter.KnowledgeAdapter
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.viewmodel.KnowledgeViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment


/**
 * Created by chenxz on 2018/5/10.
 */
class KnowledgeFragment : BaseVmListFragment<FragmentRefreshLayoutBinding, KnowledgeViewModel, Article>() {

    companion object {
        const val CID: String = "cid"

        /**
         * 创建fragment
         */
        fun newInstance(cid: Int): KnowledgeFragment {
            val projectChildFragment = KnowledgeFragment()
            val bundle = Bundle()
            bundle.putInt(CID, cid)
            projectChildFragment.arguments = bundle
            return projectChildFragment
        }
    }

    override fun attachAdapter(): BaseQuickAdapter<Article, *> {
        return KnowledgeAdapter()
    }

    override fun attachViewModelClass(): Class<KnowledgeViewModel> {
        return KnowledgeViewModel::class.java
    }

    override fun attachViewBinding(): FragmentRefreshLayoutBinding {
        return FragmentRefreshLayoutBinding.inflate(layoutInflater)
    }

    override fun initObserver() {
        mViewModel.articleList.observe(this) {
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
        mRecyclerView=mBinding.recyclerView
        mLayoutStatusView=mBinding.multipleStatusView
        mSwipeRefreshLayout=mBinding.swipeRefreshLayout
        mFloatingActionBtn=mBinding.floatingActionBtn
    }

    private var mCid: Int = 0

    override fun loadWebData() {
        mCid = arguments?.getInt(CID)!!
        mViewModel.getTreeChild(mCurrentPage, mCid)
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
        val data = mListAdapter.data
        //因为加了一个header
        mPosition = position
        if (data[position].collect) {
            mViewModel.unCollect(data[position].id)
        } else {
            mViewModel.collect(data[position].id)
        }
    }

}