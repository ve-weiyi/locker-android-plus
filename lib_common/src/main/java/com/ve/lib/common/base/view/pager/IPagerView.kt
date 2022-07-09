package com.ve.lib.common.base.view.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ve.lib.common.base.adapter.ViewPager2Adapter

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/19
 */
interface IPagerView {

    var mTitleList: MutableList<String>
    var mFragmentList: MutableList<Fragment>

    var mViewPager2Adapter: ViewPager2Adapter

    var mTabLayout: TabLayout
    var mViewPager2: ViewPager2

    var mediator: TabLayoutMediator

    /**
     * 选中颜色
     */
    var activeColor: Int
    /**
     * 未选中颜色
     */
    var normalColor: Int

    var activeTextSize:Float
    var normalTextSize:Float

}