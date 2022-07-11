package com.ve.module.locker.ui.page.category.tag

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.base.view.list.ListViewManager
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.databinding.LockerFragmentTagBinding
import com.ve.module.locker.model.db.entity.PrivacyTag
import com.ve.module.locker.ui.adapter.TagGridAdapter
import com.ve.module.locker.ui.page.privacy.LockerGroupActivity
import com.ve.module.locker.ui.viewmodel.LockerClassifyViewModel

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockerTagGridFragment :
    BaseVmListFragment<LockerFragmentTagBinding, LockerClassifyViewModel, PrivacyTag>() {

    override fun attachViewBinding(): LockerFragmentTagBinding {
        return LockerFragmentTagBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerClassifyViewModel> {
        return LockerClassifyViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyTag, *> {
        return TagGridAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.multipleStatusView
        mRecyclerView = mBinding.recyclerView
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout
    }

    override fun attachListManager(context: Context): ListViewManager<PrivacyTag> {
        return ListViewManager<PrivacyTag>(context).apply {
            mLinearLayoutManager = GridLayoutManager(requireContext(), 3)
            mConfig.enableLoadMore = false
        }
    }

    override fun initWebData() {
        mViewModel.getTagList()
    }

    override fun initObserver() {
        mViewModel.tagList.observe(this) {
            LogUtil.e(it.toString())
            showAtAdapter(it)
            mListAdapter.loadMoreModule.loadMoreEnd(true)
        }
    }

    override fun onItemClickEvent(datas: MutableList<PrivacyTag>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        val tag=datas[position]
        LockerGroupActivity.start(mContext,tag.tagName, Bundle().apply {
            putSerializable(LockerGroupActivity.FRAGMENT_DATA_KEY,tag)
        })
    }
}