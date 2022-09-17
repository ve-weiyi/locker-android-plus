package com.ve.module.android.ui.page.activity

import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityRankBinding
import com.ve.module.android.repository.bean.CoinInfo
import com.ve.module.android.ui.adapter.RankAdapter
import com.ve.module.android.ui.viewmodel.RankViewModel
import com.ve.lib.common.base.view.list.BaseVmListActivity

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/21
 */
class RankActivity: BaseVmListActivity<ActivityRankBinding, RankViewModel, CoinInfo>() {


    override fun attachViewBinding(): ActivityRankBinding {
        return ActivityRankBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<RankViewModel> {
        return RankViewModel::class.java
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.rankList.observe(this) {
            showAtAdapter(it)
        }
    }

    override fun loadWebData() {
        showLoading()
        mPageSize=30
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun getMoreData() {
        mViewModel.getUserScore(++mCurrentPage)
    }

    override fun getRefreshData() {
        mCurrentPage=0
        mViewModel.getUserScore(mCurrentPage)
    }

    override fun attachAdapter(): BaseQuickAdapter<CoinInfo, *> {
        return RankAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        initToolbar(mBinding.extToolbar.toolbar,getString(R.string.score_list))

    }

}