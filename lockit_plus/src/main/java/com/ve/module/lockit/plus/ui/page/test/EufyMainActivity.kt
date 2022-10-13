package com.ve.module.lockit.plus.ui.page.test

import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.ve.lib.common.base.model.NaviMenuItem
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.lockit.plus.R
import com.ve.module.lockit.plus.databinding.ActivityEufyCleanMainBinding

class EufyMainActivity : BaseActivity() {

    private var mIndex = 0
    private val mBinding by lazy {
        ActivityEufyCleanMainBinding.inflate(layoutInflater)
    }

    override fun initLayoutView() {
        setContentView(mBinding.root)
    }

    override fun initialize() {
        initMenu()
        showFragment(mIndex)

        lightStatusBar()
    }

    private var mFragmentPageList: MutableList<NaviMenuItem> = mutableListOf()

    private fun initMenu() {
        var pageCount = 0

        mFragmentPageList = mutableListOf(
            NaviMenuItem(
                pageCount++, "首页", HomeDeviceFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_eufy_home,
                0
            ),
            NaviMenuItem(
                pageCount++, "首页2", HomeDeviceNewFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_eufy_home,
                0
            ),
            NaviMenuItem(
                pageCount++, "我的", HomeMeFragment::class.java, NaviMenuItem.getId(pageCount),
                com.ve.lib.application.R.drawable.ic_baseline_person_24,
                0
            ),
            NaviMenuItem(
                pageCount++, "其他", HomeEmptyFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_icon_outline_artical,
                0
            ),
        )

        mFragmentPageList.forEachIndexed { index, menuItem ->

            mBinding.bottomTabView.addMenuItem(menuItem, object : BottomTabView.OnMenuClickListener {

                override fun onItemClick(view: View, item: NaviMenuItem) {
                    showFragment(mFragmentPageList.indexOf(item))
                }
            })
        }

        mBinding.bottomTabView.setBadge(mFragmentPageList.size - 1, true)
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