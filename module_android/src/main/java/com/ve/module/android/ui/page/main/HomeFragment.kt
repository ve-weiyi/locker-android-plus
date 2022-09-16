package com.ve.module.android.ui.page.main

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.databinding.FragmentHomeBinding
import com.ve.module.android.databinding.ItemHomeBannerBinding
import com.ve.module.android.repository.bean.Article
import com.ve.module.android.repository.bean.BannerBean
import com.ve.module.android.ui.adapter.ArticleAdapter
import com.ve.module.android.ui.adapter.BannerImageTitleNumAdapter
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.viewmodel.HomeViewModel
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.utils.ui.DisplayUtil
import com.ve.lib.common.utils.view.ToastUtil
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.transformer.AlphaPageTransformer
import com.youth.banner.transformer.ScaleInTransformer
import kotlin.math.roundToInt

/**
 * @Description 1.首页
 * @Author  weiyi
 * @Date 2022/3/20
 */
class HomeFragment() : BaseVmListFragment<FragmentHomeBinding, HomeViewModel, Article>() {

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()

        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun attachViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<Article, *> {
        return ArticleAdapter()
    }

    private val articleData = mutableListOf<Article>()
    private lateinit var bannersData: ArrayList<BannerBean>

    /**
     * banner com.ve.lib.view
     */
    private lateinit var mHomeHeaderView: ItemHomeBannerBinding

    override fun initObserver() {
        super.initObserver()
        mViewModel.bannerBeanList.observe(this) { bannerList ->
            mHomeHeaderView.banner.apply {
                //动态设置高度
                val layoutParams = mHomeHeaderView.banner.layoutParams
                layoutParams.height = (DisplayUtil.getScreenWidth() / 1.99).roundToInt()

                adapter = BannerImageTitleNumAdapter(bannerList).enableNum(false)

                //生命周期  以下效果需要在adapter之后调用
                addBannerLifecycleObserver(this@HomeFragment)
                //画廊效果
                setBannerGalleryEffect(10, 1)
                //魅族效果
                setBannerGalleryMZ(12)
                setPageTransformer(ScaleInTransformer())
                addPageTransformer(AlphaPageTransformer())
                //设置指示器
                indicator = CircleIndicator(requireContext())

                //adapter = BannerImageAdapter(bannerList)
                //removeIndicator()

                setOnBannerListener(OnBannerListener<BannerBean>() { data, position ->
                    val item = data
                    ArticleDetailActivity.start(activity, item.id, item.title, item.url)
                })

                start()
            }
        }

        mViewModel.articleList.observe(this) {
            showAtAdapter(mCurrentPage == 0, it)
        }

        mViewModel.collectState.observe(this) {
            if (it) {
                ToastUtil.show("收藏成功")
                mListAdapter.data[mPosition].collect = true
                //由于加入header，所以需要通知的位置是pos+1
                mListAdapter.notifyItemChanged(mPosition+1)
            }
        }

        mViewModel.unCollectState.observe(this) {
            if (it) {
                ToastUtil.show("取消成功")
                mListAdapter.data[mPosition].collect = false
                mListAdapter.notifyItemChanged(mPosition+1)
            }
        }
    }

    override fun initListView() {
        //加入banner
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        mFloatingActionBtn=mBinding.fragmentRefreshLayout.floatingActionBtn
    }

    override fun initView() {
        super.initView()
        mHomeHeaderView = ItemHomeBannerBinding.inflate(layoutInflater)
        mListAdapter.addHeaderView(mHomeHeaderView.root)
    }

    override fun initWebData() {
        super.initWebData()
        //初始化页面之前先获取横幅和文章
        mViewModel.getTopAndHomeArticles()
        mViewModel.getBanner()
    }

    override fun getRefreshData() {
        mViewModel.getTopAndHomeArticles()
        mViewModel.getBanner()
    }

    override fun getMoreData() {
        mViewModel.getArticleList(mCurrentPage)
    }

    override fun onItemClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        super.onItemClickEvent(datas, view, position)
        val item = datas[position]
        ArticleDetailActivity.start(activity, item.id, item.title, item.link)
    }

    override fun onItemChildClickEvent(datas: MutableList<Article>, view: View, position: Int) {
        super.onItemChildClickEvent(datas, view, position)
        val data = mListAdapter.data
        //因为加了一个header，所以得到的文章位置是pos+1
        if (data[position].collect) {
            mViewModel.unCollect(data[position].id)
        } else {
            mViewModel.collect(data[position].id)
        }
    }

}