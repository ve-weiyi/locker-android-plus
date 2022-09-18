package com.ve.module.lockit.ui.page.category.tag

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.base.view.list.ListViewManager
import com.ve.lib.common.utils.system.LogUtil
import com.ve.module.lockit.databinding.LockitFragmentTagBinding
import com.ve.module.lockit.respository.database.entity.PrivacyTag
import com.ve.module.lockit.ui.adapter.TagGridAdapter
import com.ve.module.lockit.ui.page.privacy.LockitGroupActivity
import com.ve.module.lockit.ui.viewmodel.LockitClassifyViewModel

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockitTagGridFragment :
    BaseVmListFragment<LockitFragmentTagBinding, LockitClassifyViewModel, PrivacyTag>() {

    override fun attachViewBinding(): LockitFragmentTagBinding {
        return LockitFragmentTagBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitClassifyViewModel> {
        return LockitClassifyViewModel::class.java
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
        }
    }

    override fun loadWebData() {
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
        LockitGroupActivity.start(mContext,tag.tagName, Bundle().apply {
            putSerializable(LockitGroupActivity.FRAGMENT_DATA_KEY,tag)
        })
    }
}