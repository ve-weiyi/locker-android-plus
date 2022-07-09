package com.ve.lib.common.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by yechaoa on 2021/2/4.
 * Describe :
 */
class ViewPagerAdapter : FragmentStatePagerAdapter {

    private var mTitles: MutableList<String> = ArrayList()
    private var mFragments: MutableList<Fragment> = ArrayList()

    constructor(fm: FragmentManager, titles: MutableList<String> ,fragments: MutableList<Fragment>) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        mTitles.addAll(titles)
        mFragments.addAll(fragments)
    }

    constructor(fm: FragmentManager) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

    fun addFragment(fragment: Fragment,title :String) {
        mFragments.add(fragment)
        mTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }

}
