package com.ve.module.android.ui.page.activity

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityScoreBinding
import com.ve.module.android.repository.model.UserScore
import com.ve.module.android.ui.adapter.ScoreAdapter
import com.ve.module.android.ui.viewmodel.ScoreViewModel
import com.ve.lib.common.base.view.list.BaseVmListActivity

class ScoreActivity : BaseVmListActivity<ActivityScoreBinding, ScoreViewModel, UserScore>(){


    override fun attachAdapter(): BaseQuickAdapter<UserScore, *> {
        return ScoreAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout

        mToolbar=mBinding.extToolbar.toolbar
        mTitle=getString(R.string.score_detail)
        initToolbar(mToolbar,mTitle)
    }

    lateinit var mToolbar: Toolbar
    lateinit var mTitle: String


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
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun getRefreshData() {
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun getMoreData() {
        mViewModel.getUserScore(++mCurrentPage)
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