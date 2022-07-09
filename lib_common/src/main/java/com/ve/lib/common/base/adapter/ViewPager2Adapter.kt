package com.ve.lib.common.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by weiyi 2022/3/19
 * Describe : viewPager2Adapter ，默认实现懒加载
 * 只需要在pageFragment中onResume实现
 * if(!hasLoadData){
 *  lazeLoad()
 *  hasLoadData=true
 * }
 *
 * onDestroy(){
 * hasLoadData=false
 * }
 */
class ViewPager2Adapter : FragmentStateAdapter {

    private var mTitles: MutableList<String> = ArrayList()
    private var mFragments: MutableList<Fragment> = ArrayList()

    constructor(fa: FragmentActivity, titles: MutableList<String> ,fragments: MutableList<Fragment>) : super(fa) {
        mTitles.addAll(titles)
        mFragments.addAll(fragments)
    }

    constructor(fa: FragmentActivity) : super(fa)

    fun addFragment(fragment: Fragment,title :String) {
        mFragments.add(fragment)
        mTitles.add(title)
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

}
