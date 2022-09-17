package com.ve.module.lockit.ui.page.privacy.card

import android.text.Editable
import android.text.TextWatcher
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.utils.system.LogUtil
import com.ve.module.lockit.databinding.LockitFragmentListPrivacySearchBinding
import com.ve.module.lockit.respository.database.entity.PrivacyCard
import com.ve.module.lockit.ui.adapter.PrivacyInfoCardAdapter
import com.ve.module.lockit.ui.viewmodel.LockitPrivacyCardViewModel

/**
 * @Author  weiyi
 * @Date 2022/4/18
 * @Description  current project lockit-android
 */
class LockitCardSearchFragment :
    BaseVmListFragment<LockitFragmentListPrivacySearchBinding, LockitPrivacyCardViewModel, PrivacyCard>() {

    override fun attachAdapter(): BaseQuickAdapter<PrivacyCard, *> {
        return PrivacyInfoCardAdapter()
    }

    override fun attachViewBinding(): LockitFragmentListPrivacySearchBinding {
        return LockitFragmentListPrivacySearchBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitPrivacyCardViewModel> {
        return LockitPrivacyCardViewModel::class.java
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout

    }

    override fun initListener() {
        super.initListener()
        mBinding.tvSearchText.addTextChangedListener(textWatcher)
    }

    override fun loadWebData() {
        super.loadWebData()
        mViewModel.getPrivacyCardList()
    }

    var mKeywords = ""
    val mAdapter by lazy { mListAdapter as PrivacyInfoCardAdapter }

    override fun initObserver() {
        super.initObserver()
        mViewModel.getPrivacyCardListResult.observe(this) {
            showAtAdapter(true, it)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            LogUtil.msg("before " + s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            LogUtil.msg("on " + s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            LogUtil.msg("after " + s.toString())
            mAdapter.setKeywords(s.toString())
            mViewModel.searchPrivacyCardList(keyWords = s.toString())
        }
    }
}