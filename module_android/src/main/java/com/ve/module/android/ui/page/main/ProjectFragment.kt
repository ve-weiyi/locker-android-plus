package com.ve.module.android.ui.page.main

import com.ve.module.android.databinding.FragmentProjectBinding
import com.ve.module.android.ui.page.main.child.ProjectListFragment
import com.ve.module.android.ui.viewmodel.ProjectViewModel
import com.ve.lib.common.base.view.pager.BaseVmPager2Fragment

/**
 * @Description 7.项目
 * @Author  weiyi
 * @Date 2022/3/20
 */
open class ProjectFragment : BaseVmPager2Fragment<FragmentProjectBinding, ProjectViewModel>() {

    companion object {
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    override fun attachViewModelClass(): Class<ProjectViewModel> {
        return ProjectViewModel::class.java
    }

    /**
     * 得到绑定对象
     */
    override fun attachViewBinding(): FragmentProjectBinding {
       return FragmentProjectBinding.inflate(layoutInflater)
    }


    /**
     * step 1.初始化liveData.observe
     */
    override fun initObserver() {
        mViewModel.proList.observe(this) {
            it.forEach { chapter ->
                mTitleList.add(chapter.name)
                mFragmentList.add(ProjectListFragment.newInstance(chapter.id))
            }
            autoPagerView()
            if (it.isEmpty()) {
                mLayoutStatusView?.showEmpty()
            } else {
                mLayoutStatusView?.showContent()
            }
        }
    }

    /**
     * step 3.初始化data相关
     */
    override fun loadWebData() {
        mViewModel.getProject()
    }

    /**
     * step 4.设置监听
     */
    override fun initListener() {

    }


    override fun initPagerView() {
        mLayoutStatusView=mBinding.multipleStatusView
        mTabLayout=mBinding.tabLayout
        mViewPager2=mBinding.viewPager
    }

    override fun getFragmentList() {

    }
}