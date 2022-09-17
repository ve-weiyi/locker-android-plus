package com.ve.module.lockit.plus

import android.os.Bundle
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.navigation.NavigationBarView
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.router.ARouterPath
import com.ve.module.android.WazMainFragment
import com.ve.module.lockit.plus.bean.NavigationMenuItem
import com.ve.module.lockit.plus.databinding.ActivityMainBinding
import com.ve.module.lockit.plus.ui.page.drawer.DrawerFragment


@Route(path = ARouterPath.MAIN_HOME)
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun attachViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var mIndex = 0
    private lateinit var mFragmentPageList: MutableList<NavigationMenuItem>

    override fun initialize(saveInstanceState: Bundle?) {
        initFragment()
        initNavigation()
        showFragment(mIndex)
    }

    private fun initFragment() {
        var pageCount = 0
        mFragmentPageList = mutableListOf(
            NavigationMenuItem(
                0, R.id.home_navigation_0, pageCount++, "首页",
                R.drawable.ic_icon_outline_custome,
                WazMainFragment::class.java
            ),
            NavigationMenuItem(
                0, R.id.home_navigation_1, pageCount++, "卡片",
                R.drawable.ic_icon_outline_about,
                WazMainFragment::class.java
            ),
            NavigationMenuItem(
                0, R.id.home_navigation_2, pageCount++, "好友",
                R.drawable.ic_icon_outline_flow,
                DrawerFragment::class.java
            ),
            NavigationMenuItem(
                0, R.id.home_navigation_3, pageCount++, "分类",
                R.drawable.ic_icon_outline_down4,
                DrawerFragment::class.java
            ),
            NavigationMenuItem(
                0, R.id.home_navigation_4, pageCount++, "我的",
                R.drawable.ic_icon_outline_about,
                DrawerFragment::class.java
            )
        )
    }

    fun initNavigation() {
        mBinding.bottomNavigation.apply {
            menu.clear()
            //导航栏文字可见;原因：底部导航栏的类别多于三个了，多于三个就会不显示，解决方案如下~~~
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener(onNavigationItemSelectedListener)
            mFragmentPageList.forEach { it ->
                menu.add(it.menuGroup, it.menuId, it.menuIndex, it.menuTitle)
                menu.findItem(it.menuId).apply {
                    setIcon(it.menuIconRes)
                }
            }
        }
    }

    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
        NavigationBarView.OnItemSelectedListener { item ->
            mFragmentPageList.forEach { it ->
                if (item.itemId == it.menuId) {
                    showFragment(it.menuIndex)
                    return@OnItemSelectedListener true
                }
            }
            return@OnItemSelectedListener false
        }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mFragmentPageList.forEach { mFragmentPage ->
            mFragmentPage.mFragment?.let {
                transaction.hide(it)
            }
        }
    }

    /**
     * 展示Fragment
     * @param index
     */
    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index

        val mFragmentPage = mFragmentPageList[index]
        if (mFragmentPage.mFragment == null) {
            mBinding.bottomNavigation.menu[index].title = mFragmentPage.menuTitle
            mFragmentPage.mFragment = mFragmentPage.getFragment()
            transaction.add(
                R.id.ext_container,
                mFragmentPage.mFragment!!,
                mFragmentPage.menuTitle
            )
        } else {
            transaction.show(mFragmentPage.mFragment!!)
        }

        transaction.commit()
    }
}