package com.ve.module.android.ui.page.activity

import androidx.appcompat.widget.Toolbar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityShareBinding
import com.ve.module.android.repository.model.Article
import com.ve.module.android.ui.adapter.ShareAdapter
import com.ve.module.android.ui.viewmodel.ShareViewModel
import com.ve.lib.common.base.view.list.BaseVmListActivity

class ShareActivity : BaseVmListActivity<ActivityShareBinding, ShareViewModel, Article>(){


    override fun attachAdapter(): BaseQuickAdapter<Article, *> {
        return ShareAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        mToolbar=mBinding.extToolbar.toolbar
        mTitle=getString(R.string.share_article)
        initToolbar(mToolbar,mTitle)
    }

    lateinit var mToolbar: Toolbar
    lateinit var mTitle: String



    override fun attachViewBinding(): ActivityShareBinding {
        return ActivityShareBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<ShareViewModel> {
        return ShareViewModel::class.java
    }
    override fun initObserver() {
        mViewModel.userShare.observe(this) {
            showAtAdapter(it.shareArticles.datas)
        }

    }



    override fun initWebData() {
        mViewModel.getUserShare(mCurrentPage)
    }

    override fun getRefreshData() {
        mViewModel.getUserShare(mCurrentPage)
    }

    override fun getMoreData() {
        mViewModel.getUserShare(mCurrentPage)
    }
}