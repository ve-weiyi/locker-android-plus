package com.ve.lib.common.base.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

/**
 * @Author  weiyi
 * @Date 2022/9/17
 * @Description  current project lockit-android
 */
class NavigationMenuItem(
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
}