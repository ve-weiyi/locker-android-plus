package com.ve.module.android.ui.page.main

import com.ve.module.android.databinding.FragmentWechatBinding
import com.ve.module.android.ui.page.main.child.KnowledgeFragment
import com.ve.module.android.ui.viewmodel.WeChatViewModel
import com.ve.lib.common.base.view.pager.BaseVmPager2Fragment

/**
 * @Description 8.公众号
 * @Author  weiyi
 * @Date 2022/3/20
 */
open class WeChatFragment : BaseVmPager2Fragment<FragmentWechatBinding, WeChatViewModel>() {

    companion object {
        fun getInstance(): WeChatFragment = WeChatFragment()
    }

    override fun attachViewModelClass(): Class<WeChatViewModel> {
        return WeChatViewModel::class.java
    }

    override fun attachViewBinding(): FragmentWechatBinding {
       return FragmentWechatBinding.inflate(layoutInflater)
    }

    //override var activeColor: Int=Color.WHITE
    /**
     * step 1.初始化liveData.observe
     */
    override fun initObserver() {

        mViewModel.wxChapters.observe(this) {
            it.forEach { chapter ->
                mTitleList.add(chapter.name)
                mFragmentList.add(KnowledgeFragment.newInstance(chapter.id))
            }
            autoPagerView()
          //  mViewPager2.isUserInputEnabled = false; //true:滑动，false：禁止滑动
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
    override fun initWebData() {
        mViewModel.getWxChapters()
    }

    /**
     * step 4.设置监听
     */
    override fun initListener() {

    }

    override fun initColor() {
        super.initColor()
    }

    override fun initPagerView() {
        mLayoutStatusView=mBinding.multipleStatusView
        mViewPager2=mBinding.viewPager
        mTabLayout=mBinding.tabLayout
    }

    override fun getFragmentList() {

    }


}