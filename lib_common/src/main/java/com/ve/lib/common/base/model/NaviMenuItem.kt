package com.ve.lib.common.base.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.ve.lib.common.R

/**
 * @Author  weiyi
 * @Date 2022/9/17
 * @Description  current project lockit-android
 */
class NaviMenuItem(
    var fragmentIndex: Int,
    var fragmentTitle: String = "",
    var fragmentClass: Class<out Fragment>,
    @IdRes var menuId: Int,
    @DrawableRes var menuIcon: Int = com.ve.lib.application.R.drawable.ic_home_black_24dp,
    var menuGroup: Int = 0,
) {

    var mFragment: Fragment? = null

    fun getFragment(): Fragment? {
        if (mFragment == null) {
            mFragment = fragmentClass.newInstance()
        }
        return mFragment
    }

    companion object{
        private var ids= mutableListOf(
            R.id.home_navigation_0,
            R.id.home_navigation_1,
            R.id.home_navigation_2,
            R.id.home_navigation_3,
            R.id.home_navigation_4,
            R.id.home_navigation_5,
        )

        fun getId(count:Int): Int {
            return ids[count]
        }
    }
}