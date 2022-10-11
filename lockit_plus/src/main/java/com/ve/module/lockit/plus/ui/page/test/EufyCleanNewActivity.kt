package com.ve.module.lockit.plus.ui.page.test

import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.ve.lib.common.base.model.NaviMenuItem
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.lockit.plus.R
import com.ve.module.lockit.plus.databinding.ActivityEufyCleanNewBinding

class EufyCleanNewActivity : BaseActivity<ActivityEufyCleanNewBinding>() {

    override fun attachViewBinding(): ActivityEufyCleanNewBinding{
        return ActivityEufyCleanNewBinding.inflate(layoutInflater)
    }

    private var mIndex = 0
    private lateinit var mFragmentPageList: MutableList<NaviMenuItem>

    override fun initialize() {
        initFragment()
        showFragment(mIndex)

        setSystemBarTheme(false)

        mBinding.bottomNavigationMe.itemIcon.setImageResource(com.ve.lib.application.R.drawable.ic_baseline_person_24)
        mBinding.bottomNavigationMe.itemTitle.text="我的"

        mBinding.bottomNavigationMe.apply {
            layoutMain.setOnClickListener {
                showFragment(1)
                itemFrame.visibility= View.VISIBLE
                mBinding.bottomNavigationHome.itemFrame.visibility=View.GONE
            }
        }

        mBinding.bottomNavigationHome.apply {
            layoutMain.setOnClickListener {
                showFragment(0)
                itemFrame.visibility= View.VISIBLE
                mBinding.bottomNavigationMe.itemFrame.visibility=View.GONE
            }
        }
    }

    private fun initFragment() {
        var pageCount = 0
        mFragmentPageList = mutableListOf(

            NaviMenuItem(
                pageCount++, "分类", HomeDeviceFragment::class.java, NaviMenuItem.getId(pageCount),
                R.drawable.ic_eufy_home,
                0
            ),
            NaviMenuItem(
                pageCount++, "我的", HomeMeFragment::class.java, NaviMenuItem.getId(pageCount),
                com.ve.lib.application.R.drawable.ic_baseline_person_24,
                0
            ),
        )
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