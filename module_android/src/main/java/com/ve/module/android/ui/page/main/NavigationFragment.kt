package com.ve.module.android.ui.page.main

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.ve.module.android.R
import com.ve.module.android.databinding.FragmentNavigationBinding
import com.ve.module.android.repository.bean.Article
import com.ve.module.android.repository.bean.Navigation
import com.ve.module.android.ui.page.activity.ArticleDetailActivity
import com.ve.module.android.ui.viewmodel.TreeViewModel
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.color.ColorUtil.randomColor
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView

/**
 * @Description 3.导航
 * @Author  weiyi
 * @Date 2022/3/20
 */
class NavigationFragment : BaseVmFragment<FragmentNavigationBinding, TreeViewModel>() {
    companion object {
        const val title = "导航"
        fun getInstance(): NavigationFragment = NavigationFragment()
    }

    private lateinit var mTabAdapter: MyTabAdapter
    private lateinit var mNavigationList: MutableList<Navigation>
    private lateinit var mArticles: MutableList<Article>



    override fun attachViewModelClass(): Class<TreeViewModel> {
        return TreeViewModel::class.java
    }

    override fun attachViewBinding(): FragmentNavigationBinding {
        return FragmentNavigationBinding.inflate(layoutInflater)
    }

    override fun initObserver() {
        mViewModel.naviList.observe(this) {
            mNavigationList = it
            mTabAdapter = MyTabAdapter(mNavigationList)
            mBinding.multipleStatusView
            mBinding.verticalTabLayout.setTabAdapter(mTabAdapter)
            /**
             * 默认选中第一个
             */
            mArticles = mNavigationList[0].articles
            setFlowLayout(mArticles)
        }
    }

    override fun initView() {
        mLayoutStatusView = mBinding.multipleStatusView
    }

    override fun initWebData() {
        mViewModel.getNavi()
    }

    /**
     * 填充FlowLayout数据
     */
    private fun setFlowLayout(articles: MutableList<Article>) {
        mBinding.flowLayout.adapter = object : TagAdapter<Article>(articles) {
            override fun getView(parent: FlowLayout, position: Int, s: Article): View {
                val tvTag = LayoutInflater.from(activity).inflate(
                    R.layout.item_navi, mBinding.flowLayout, false
                ) as TextView
                tvTag.text = s.title
                tvTag.setTextColor(randomColor())
                return tvTag
            }
        }
    }
    override fun initListener() {
        super.initListener()
        mBinding.verticalTabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView?, position: Int) {
                mArticles = mNavigationList[position].articles
                setFlowLayout(mArticles)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {

            }
        })

        mBinding.flowLayout.setOnTagClickListener { _, position, _ ->
            val intent = Intent(requireContext(), ArticleDetailActivity::class.java).apply {
                putExtra(ArticleDetailActivity.ARTICLE_URL_KEY, mArticles[position].link)
                putExtra(ArticleDetailActivity.ARTICLE_TITLE_KEY, mArticles[position].title)
            }
            startActivity(intent)
            return@setOnTagClickListener true
        }
    }

    inner class MyTabAdapter(private val navigationList: MutableList<Navigation>) : TabAdapter {

        override fun getCount(): Int {
            return navigationList.size
        }

        override fun getBadge(position: Int): ITabView.TabBadge? {
            return null
        }

        override fun getIcon(position: Int): ITabView.TabIcon? {
            return null
        }

        override fun getTitle(position: Int): ITabView.TabTitle {
            return ITabView.TabTitle.Builder()
                .setContent(navigationList[position].name)
                .setTextColor(Color.parseColor("#FF9800"), Color.parseColor("#757575"))
                .setTextSize(16)
                .build()
        }

        override fun getBackground(position: Int): Int {
            return 0
        }
    }
}