package com.ve.module.android.ui.page.main

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.databinding.FragmentRefreshLayoutBinding
import com.ve.module.android.repository.bean.Tree
import com.ve.module.android.ui.adapter.KnowledgeTreeAdapter
import com.ve.module.android.ui.viewmodel.TreeViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment

/**
 * @Description 6.体系
 * @Author  weiyi
 * @Date 2022/3/20
 */
class TreeFragment : BaseVmListFragment<FragmentRefreshLayoutBinding, TreeViewModel, Tree>() {
    companion object {
        const val title="体系"
        fun getInstance(): TreeFragment = TreeFragment()
    }

    override fun attachAdapter(): BaseQuickAdapter<Tree, *> {
        return KnowledgeTreeAdapter()
    }

    override fun attachViewModelClass(): Class<TreeViewModel> {
        return TreeViewModel::class.java
    }

    override fun initObserver() {
        mViewModel.treeList.observe(this) {
            showAtAdapter(it)
        }
    }

    override fun loadWebData() {
        mViewModel.getTree()
    }


    override fun onItemChildClickEvent(datas: MutableList<Tree>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
    }

    override fun initListView() {
        mRecyclerView=mBinding.recyclerView
        mSwipeRefreshLayout=mBinding.swipeRefreshLayout
        mFloatingActionBtn=mBinding.floatingActionBtn
    }

    override fun attachViewBinding(): FragmentRefreshLayoutBinding {
        return FragmentRefreshLayoutBinding.inflate(layoutInflater)
    }
}