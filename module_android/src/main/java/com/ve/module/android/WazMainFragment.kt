package com.ve.module.android

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ve.module.android.databinding.WazFragmentMainBinding
import com.ve.module.android.repository.bean.BannerBean
import com.ve.module.android.ui.adapter.BannerImageTitleNumAdapter
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.page.fragment.ShareArticleFragment
import com.ve.module.android.ui.page.search.SearchActivity
import com.ve.module.android.ui.page.main.*
import com.ve.module.android.ui.viewmodel.WanAndroidViewModel
import com.ve.lib.common.base.view.pager.BaseVmPager2Fragment
import com.ve.lib.common.event.DrawerOpenEvent
import com.ve.lib.common.utils.ui.DisplayUtil
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.transformer.AlphaPageTransformer
import com.youth.banner.transformer.ScaleInTransformer
import org.greenrobot.eventbus.EventBus
import kotlin.math.roundToInt


class WazMainFragment : BaseVmPager2Fragment<WazFragmentMainBinding, WanAndroidViewModel>() {

    companion object {
        fun newInstance() = WazMainFragment();
    }

    override fun attachViewBinding(): WazFragmentMainBinding {
        return WazFragmentMainBinding.inflate(layoutInflater)
    }

    override fun initObserver() {
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
    }

    override fun initPagerView() {
        var appCompatActivity=activity  as AppCompatActivity
        appCompatActivity.apply {
            mBinding.toolbar.title=""
            setSupportActionBar(mBinding.toolbar)
            //设置返回键可用
            supportActionBar?.setHomeButtonEnabled(false)
            //左侧添加一个默认的返回图标
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        mBinding.tvTitle.text="玩安卓"
        mBinding.ivAvatar.apply {
            setOnClickListener{
                EventBus.getDefault().post(DrawerOpenEvent(mViewName!!))
            }
        }

        setHasOptionsMenu(true)
        mTabLayout=mBinding.tabContainer.tabLayout
        mViewPager2=mBinding.tabContainer.viewPager
        autoPagerView()
    }

    override fun getFragmentList() {
        mTitleList = listOf("首页", "广场", "公众号", "导航","体系","项目") as MutableList<String>
        mFragmentList.add(HomeFragment.getInstance())
        mFragmentList.add(SquareFragment.getInstance())
        mFragmentList.add(WeChatFragment.getInstance())
        mFragmentList.add(NavigationFragment.getInstance())
        mFragmentList.add(TreeFragment.getInstance())
        mFragmentList.add(ProjectFragment.getInstance())
    }


    override fun loadWebData() {
        mViewModel.getBanner()
    }

    override fun attachViewModelClass(): Class<WanAndroidViewModel> {
        return WanAndroidViewModel::class.java
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        inflater.inflate(R.menu.menu_search,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                showMsg("module.module.android.R.id.home")
                return true
            }
            R.id.action_search->{
                Intent(requireContext(), SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
            R.id.action_add -> {
                CommonActivity.start(requireContext(),"分享文章",ShareArticleFragment::class.java.name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
