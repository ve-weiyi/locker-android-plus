package com.ve.module.locker.ui.page.category

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.base.view.list.ListViewManager
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.databinding.LockerFragmentTagBinding
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.ui.adapter.PrivacyFolderAdapter
import com.ve.module.locker.ui.page.privacy.LockerGroupActivity
import com.ve.module.locker.ui.viewmodel.LockerCategoryViewModel

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockerFolderFragment :
    BaseVmListFragment<LockerFragmentTagBinding, LockerCategoryViewModel, PrivacyFolder>() {

    override fun attachViewBinding(): LockerFragmentTagBinding {
        return LockerFragmentTagBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerCategoryViewModel> {
        return LockerCategoryViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyFolder, *> {
        return PrivacyFolderAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.multipleStatusView
        mRecyclerView = mBinding.recyclerView
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout
    }

    override fun attachListManager(context: Context): ListViewManager<PrivacyFolder> {
        return ListViewManager<PrivacyFolder>(context).apply {
            mLinearLayoutManager = GridLayoutManager(requireContext(), 3)
            mConfig.enableLoadMore = false
        }
    }

    override fun initWebData() {
        mViewModel.folderList()
    }

    override fun initObserver() {
        mViewModel.folderList.observe(this) {
            LogUtil.e(it.toString())
            showAtAdapter(it)
            mListAdapter.loadMoreModule.loadMoreEnd(true)
        }
    }

    override fun onItemClickEvent(datas: MutableList<PrivacyFolder>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        val tag=datas[position]
        LockerGroupActivity.start(mContext,tag.folderName, Bundle().apply {
            putSerializable(LockerGroupActivity.FRAGMENT_DATA_KEY,tag)
        })
    }
}