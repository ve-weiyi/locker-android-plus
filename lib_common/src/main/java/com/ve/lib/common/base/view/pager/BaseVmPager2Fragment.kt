package com.ve.lib.common.base.view.pager

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.base.adapter.ViewPager2Adapter
import com.ve.lib.common.utils.color.ColorUtil

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/19
 */
abstract class BaseVmPager2Fragment<VB : ViewBinding, VM : BaseViewModel>() :
    BaseVmFragment<VB, VM>(), IPagerView {


    override var mTitleList: MutableList<String> = mutableListOf<String>()
    override var mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>()

    override lateinit var mViewPager2Adapter: ViewPager2Adapter
    override lateinit var mTabLayout: TabLayout
    override lateinit var mViewPager2: ViewPager2

    override lateinit var mediator: TabLayoutMediator

    override var activeColor: Int = ColorUtil.randomColor()
    override var normalColor: Int = Color.GRAY

    override var activeTextSize:Float =16F
    override var normalTextSize:Float =14F

    /**
     * 颜色混合比例
     */
    protected var ratio:Float =0.2F


    open fun getPagerAdapter(): ViewPager2Adapter {
        return ViewPager2Adapter(this.requireActivity(), mTitleList, mFragmentList)
    }

    override fun initView() {
        initPagerView()
    }

    open fun autoPagerView() {
        getFragmentList()
        
        mViewPager2Adapter = getPagerAdapter()
        //禁用预加载
        mViewPager2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT;
        //适配器
        mViewPager2.adapter = mViewPager2Adapter
        //懒加载页面总数，即默认加载左右2个页面
        mViewPager2.offscreenPageLimit = 2
        //页面切换监听
        mViewPager2.registerOnPageChangeCallback(pageChangeCallback)

        mediator = TabLayoutMediator(
            mTabLayout,
            mViewPager2,
            tabConfigurationStrategy
        )
        mediator.attach()
    }

    abstract fun initPagerView()
    abstract fun getFragmentList()


    override fun onDestroy() {
        super.onDestroy()
        mViewPager2.unregisterOnPageChangeCallback(pageChangeCallback);
    }

    private val tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy =
        object : TabLayoutMediator.TabConfigurationStrategy {
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                //这里可以自定义TabView
                val tabView = TextView(requireContext())

                val states = arrayOfNulls<IntArray>(2)
                states[0] = intArrayOf(R.attr.state_selected)
                states[1] = intArrayOf()

                val colors = intArrayOf(activeColor, normalColor)
                val colorStateList = ColorStateList(states, colors)
                tabView.text = mTitleList[position]
                tabView.textSize = normalTextSize
                tabView.setTextColor(colorStateList)

                tab.customView = tabView
            }

        }
    private val pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //可以来设置选中时tab的大小
                val tabCount: Int = mTabLayout.tabCount
                for (i in 0 until tabCount) {
                    val tab: TabLayout.Tab = mTabLayout.getTabAt(i)!!
                    val tabView = tab.customView as TextView
                    if (tab.position == position) {
                        tabView.textSize = activeTextSize
                        tabView.typeface = Typeface.DEFAULT_BOLD
                    } else {
                        tabView.textSize = normalTextSize
                        tabView.typeface = Typeface.DEFAULT
                    }
                }
            }
        }
}