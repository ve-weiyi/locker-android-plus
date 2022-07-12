package com.ve.module.android.ui.page.activity

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityScoreBinding
import com.ve.module.android.repository.bean.UserScore
import com.ve.module.android.ui.adapter.ScoreAdapter
import com.ve.module.android.ui.viewmodel.ScoreViewModel
import com.ve.lib.common.base.view.list.BaseVmListActivity

class ScoreActivity : BaseVmListActivity<ActivityScoreBinding, ScoreViewModel, UserScore>() {


    override fun attachAdapter(): BaseQuickAdapter<UserScore, *> {
        return ScoreAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout

        initToolbar(mBinding.extToolbar.toolbar, getString(R.string.score_detail))

        mCurrentPage = 1
    }


    override fun attachViewBinding(): ActivityScoreBinding {
        return ActivityScoreBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<ScoreViewModel> {
        return ScoreViewModel::class.java
    }

    override fun initObserver() {
        mViewModel.userScore.observe(this) {
            hideLoading()
            showAtAdapter(it)
        }

    }

    override fun initWebData() {
        showLoading()
        mCurrentPage = 1
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun getRefreshData() {
        mCurrentPage = 1
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun getMoreData() {
        mCurrentPage++
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun onItemClickEvent(datas: MutableList<UserScore>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        showMsg("you click $position item ")
    }

    override fun onItemChildClickEvent(datas: MutableList<UserScore>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
        val data = mListAdapter.data
        when (view.id) {
            R.id.tv_desc -> showMsg(data[position].desc)
            else -> showMsg("you click $position item ")
        }
    }
}