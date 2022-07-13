package com.ve.module.lockit.ui.page.category.folder

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.base.view.list.ListViewManager
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.lockit.databinding.LockitFragmentTagBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder
import com.ve.module.lockit.ui.adapter.FolderGridAdapter
import com.ve.module.lockit.ui.page.privacy.LockitGroupActivity
import com.ve.module.lockit.ui.viewmodel.LockitClassifyViewModel

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockitFolderGridFragment :
    BaseVmListFragment<LockitFragmentTagBinding, LockitClassifyViewModel, PrivacyFolder>() {

    override fun attachViewBinding(): LockitFragmentTagBinding {
        return LockitFragmentTagBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitClassifyViewModel> {
        return LockitClassifyViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyFolder, *> {
        return FolderGridAdapter()
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
        LockitGroupActivity.start(mContext,tag.folderName, Bundle().apply {
            putSerializable(LockitGroupActivity.FRAGMENT_DATA_KEY,tag)
        })
    }
}