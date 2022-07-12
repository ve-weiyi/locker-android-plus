package com.ve.module.android.ui.page.fragment

import com.ve.module.android.ui.adapter.BannerImageTitleNumAdapter
import com.ve.module.android.databinding.FragmentBlank2Binding
import com.ve.module.android.repository.bean.BannerBean
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.page.main.child.ProjectListFragment
import com.ve.module.android.ui.viewmodel.HomeViewModel
import com.ve.lib.common.base.view.pager.BaseVmPager2Fragment
import com.ve.lib.common.vutils.DisplayUtil
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.transformer.AlphaPageTransformer
import com.youth.banner.transformer.ScaleInTransformer
import kotlin.math.roundToInt

class Blank2Fragment : BaseVmPager2Fragment<FragmentBlank2Binding, HomeViewModel>() {

    companion object {
        fun getInstance() =Blank2Fragment()

        fun newInstance() = Blank2Fragment()
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.bannerBeanList.observe(this) { bannerList ->
            //动态设置高度
            val layoutParams = mBinding.banner.layoutParams
            layoutParams.height = (DisplayUtil.getScreenWidth() / 1.99).roundToInt()
            mBinding.banner.apply {
                //生命周期
                addBannerLifecycleObserver(requireActivity())
                //画廊效果
                setBannerGalleryEffect(10, 1)
                //魅族效果
                setBannerGalleryMZ(12)
                setPageTransformer(ScaleInTransformer())
                addPageTransformer(AlphaPageTransformer())
                //设置指示器
                indicator = CircleIndicator(requireContext())
                //adapter = BannerImageAdapter(bannerList)
                adapter= BannerImageTitleNumAdapter(bannerList)
                removeIndicator()
                start()

                setOnBannerListener( OnBannerListener<BannerBean>(){ data, position ->
                    val item = data
                    ArticleDetailActivity.start(activity, item.id, item.title, item.url)
                })
            }
        }

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

    override fun initPagerView() {
        mViewPager2=mBinding.viewPager
        mTabLayout=mBinding.tabLayout
    }

    override fun getFragmentList() {

    }

    override fun initWebData() {
        super.initWebData()
        mViewModel.getBanner()
        mViewModel.getProject()
    }
    override fun attachViewBinding(): FragmentBlank2Binding {
        return FragmentBlank2Binding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }


}