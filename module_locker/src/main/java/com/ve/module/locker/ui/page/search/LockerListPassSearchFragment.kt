package com.ve.module.locker.ui.page.search

import android.text.Editable
import android.text.TextWatcher
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.databinding.LockerFragmentListPrivacySearchBinding
import com.ve.module.locker.model.db.entity.PrivacyPassInfo
import com.ve.module.locker.model.http.model.ConditionVO
import com.ve.module.locker.ui.adapter.PrivacyInfoPassAdapter
import com.ve.module.locker.ui.viewmodel.LockerPrivacyPassViewModel

/**
 * @Author  weiyi
 * @Date 2022/4/18
 * @Description  current project locker-android
 */
class LockerListPassSearchFragment :
    BaseVmListFragment<LockerFragmentListPrivacySearchBinding, LockerPrivacyPassViewModel, PrivacyPassInfo>() {

    override fun attachAdapter(): BaseQuickAdapter<PrivacyPassInfo, *> {
        return PrivacyInfoPassAdapter()
    }

    override fun attachViewBinding(): LockerFragmentListPrivacySearchBinding {
        return LockerFragmentListPrivacySearchBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyPassViewModel> {
        return LockerPrivacyPassViewModel::class.java
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
        mViewModel.getPrivacyPassList()
    }
    var mKeywords=""
    val mAdapter by lazy { mListAdapter as PrivacyInfoPassAdapter }

    override fun initObserver() {
        super.initObserver()
        mViewModel.privacyPassInfoList.observe(this){
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
            mViewModel.getPrivacyPassList(ConditionVO().apply {
                keyWords = s.toString()
            })
        }
    }
}