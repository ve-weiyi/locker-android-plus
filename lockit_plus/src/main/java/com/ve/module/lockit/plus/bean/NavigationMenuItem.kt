package com.ve.module.lockit.plus.bean

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

/**
 * @Author  weiyi
 * @Date 2022/9/17
 * @Description  current project lockit-android
 */
class NavigationMenuItem(
    var menuGroup: Int = 0,
    var menuId: Int = 0,
    var menuIndex: Int = 0,
    var menuTitle: String = "",
    @DrawableRes var menuIconRes: Int = 0,
    var fragmentClass: Class<out Fragment>,
) {

    var mFragment: Fragment? = null

    fun getFragment(): Fragment? {
        if (mFragment == null) {
            mFragment = fragmentClass.newInstance()
        }
        return mFragment
    }
}