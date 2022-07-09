package com.ve.module.locker.ui.page.search

import android.text.Editable
import android.text.TextWatcher
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.databinding.LockerFragmentListPrivacySearchBinding
import com.ve.module.locker.model.db.entity.PrivacyCardInfo
import com.ve.module.locker.model.http.model.ConditionVO
import com.ve.module.locker.ui.adapter.PrivacyInfoCardAdapter
import com.ve.module.locker.ui.viewmodel.LockerPrivacyCardViewModel

/**
 * @Author  weiyi
 * @Date 2022/4/18
 * @Description  current project locker-android
 */
class LockerListCardSearchFragment :
    BaseVmListFragment<LockerFragmentListPrivacySearchBinding, LockerPrivacyCardViewModel, PrivacyCardInfo>() {

    override fun attachAdapter(): BaseQuickAdapter<PrivacyCardInfo, *> {
        return PrivacyInfoCardAdapter()
    }

    override fun attachViewBinding(): LockerFragmentListPrivacySearchBinding {
        return LockerFragmentListPrivacySearchBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyCardViewModel> {
        return LockerPrivacyCardViewModel::class.java
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

    override fun initWebData() {
        super.initWebData()
        mViewModel.getPrivacyCardList()
    }
    var mKeywords=""
    val mAdapter by lazy { mListAdapter as PrivacyInfoCardAdapter }

    override fun initObserver() {
        super.initObserver()
        mViewModel.privacyCardInfoList.observe(this){
            showAtAdapter(true,it)
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
            mViewModel.getPrivacyCardList(ConditionVO().apply {
                keyWords = s.toString()
            })
        }
    }
}