package com.ve.module.android.ui.page.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.databinding.FragmentCollectBinding
import com.ve.module.android.repository.bean.Collect
import com.ve.module.android.ui.adapter.CollectAdapter
import com.ve.module.android.ui.page.todo.AddTodoFragment
import com.ve.module.android.ui.viewmodel.WanAndroidViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.module.android.ui.page.activity.ArticleDetailActivity


class CollectFragment : BaseVmListFragment<FragmentCollectBinding, WanAndroidViewModel, Collect>() {

    companion object {
        fun getInstance(bundle: Bundle): AddTodoFragment {
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun attachAdapter(): BaseQuickAdapter<Collect, *> {
        return CollectAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        mFloatingActionBtn=mBinding.fragmentRefreshLayout.floatingActionBtn
//        在之后设置，因为此时mListAdapter=null
//        mRecyclerView?.adapter = mListAdapter
    }

    override fun attachViewBinding(): FragmentCollectBinding {
        return FragmentCollectBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<WanAndroidViewModel> {
        return WanAndroidViewModel::class.java
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.collectArticles.observe(this) {
            showAtAdapter(it)
        }

        mViewModel.unCollectState.observe(this) {
            if (it) {
                ToastUtil.show("取消成功")
                mListAdapter.removeAt(mPosition)
            }
        }
    }

    override fun initWebData() {
        mViewModel.getCollectList(mCurrentPage)
    }

    override fun getRefreshData() {
        mCurrentPage = 0
        mViewModel.getCollectList(mCurrentPage)
    }

    override fun getMoreData() {
        mViewModel.getCollectList(++mCurrentPage)
    }

    override fun onItemClickEvent(datas: MutableList<Collect>, view: View, position: Int) {
        val article=datas[position]
        ArticleDetailActivity.start(requireContext(),article.title,article.link)
    }

    override fun onItemChildClickEvent(datas: MutableList<Collect>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
        val data = mListAdapter.data
        //因为加了一个header，所以得到的文章位置是pos+1
        mPosition = position
        mViewModel.unCollect(data[position].id)
    }
}