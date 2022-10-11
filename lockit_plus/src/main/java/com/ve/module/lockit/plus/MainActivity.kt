package com.ve.module.lockit.plus

import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.navigation.NavigationBarView
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.router.ARouterPath
import com.ve.module.android.WazMainFragment
import com.ve.lib.common.base.model.NaviMenuItem
import com.ve.module.lockit.plus.databinding.ActivityMainBinding
import com.ve.module.lockit.plus.ui.page.SkinFragment
import com.ve.module.lockit.plus.ui.page.test.HomeDeviceFragment
import com.ve.module.lockit.plus.ui.page.test.HomeEmptyFragment
import com.ve.module.lockit.plus.ui.page.test.HomeMeFragment
import com.ve.module.lockit.plus.ui.page.test.HomeTestFragment


@Route(path = ARouterPath.MAIN_HOME)
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun attachViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var mIndex = 0
    private lateinit var mFragmentPageList: MutableList<NaviMenuItem>

    override fun initialize() {
        initNavigation()
        showFragment(mIndex)

//        overflowStatusBar(true)
//        setFitsSystemWindows(true)
        setSystemBarTheme(false)
//        PrivacyDialog.Builder(this).show()
    }

    private fun initFragment() {
        var pageCount = 0
        mFragmentPageList = mutableListOf(

            NaviMenuItem(
                pageCount++, "分类", HomeDeviceFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_icon_outline_down4,
                0
            ),
            NaviMenuItem(
                pageCount++, "我的", HomeMeFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_eufy_home,
                0
            ),
            NaviMenuItem(
                pageCount++, "卡片", HomeEmptyFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_eufy_home,
                0
            ),
            NaviMenuItem(
                pageCount++, "首页", WazMainFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_icon_outline_custome,
                0
            ),
            NaviMenuItem(
                pageCount++, "好友", SkinFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_icon_outline_flow,
                0
            ),
        )
    }

    fun initNavigation() {
        initFragment()
        mBinding.bottomNavigation.apply {
            menu.clear()
            //导航栏文字可见;原因：底部导航栏的类别多于三个了，多于三个就会不显示，解决方案如下~~~
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener(onNavigationItemSelectedListener)
            mFragmentPageList.forEach { it ->
                menu.add(it.menuGroup, it.menuId, it.fragmentIndex, it.fragmentTitle)
                menu.findItem(it.menuId).apply {
                    setIcon(it.menuIcon)
                }
                val badge=getOrCreateBadge(it.menuId)
                badge.number=it.fragmentIndex

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
                    showFragment(it.fragmentIndex)
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
            mBinding.bottomNavigation.menu.getItem(index).title = mFragmentPage.fragmentTitle
            mFragmentPage.mFragment = mFragmentPage.getFragment()
            transaction.add(
                R.id.ext_container,
                mFragmentPage.mFragment!!,
                mFragmentPage.fragmentTitle
            )
        } else {
            transaction.show(mFragmentPage.mFragment!!)
        }

        transaction.commit()
    }
}