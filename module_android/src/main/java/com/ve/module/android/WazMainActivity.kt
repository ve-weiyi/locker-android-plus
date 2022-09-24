package com.ve.module.android

import android.content.Intent
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.ve.lib.common.base.model.NavigationMenuItem
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.event.AppRecreateEvent
import com.ve.lib.common.event.DrawerOpenEvent
import com.ve.module.android.databinding.WazActivityMainBinding
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.page.fragment.ShareArticleFragment
import com.ve.module.android.ui.page.main.*
import com.ve.module.android.ui.page.search.SearchActivity
import com.ve.module.android.ui.viewmodel.WanAndroidViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WazMainActivity : BaseVmActivity<WazActivityMainBinding, WanAndroidViewModel>() {

    private var mIndex = 0
    private lateinit var mFragmentPageList: MutableList<NavigationMenuItem>


    override fun attachViewBinding(): WazActivityMainBinding {
        return WazActivityMainBinding.inflate(layoutInflater)
    }

    private fun initFragment() {
        var pageCount = 0
        mFragmentPageList = mutableListOf(
            NavigationMenuItem(
                pageCount++,
                "首页",
                HomeFragment::class.java,
                com.ve.lib.common.R.id.home_navigation_0,
                com.ve.lib.application.R.drawable.ic_home_black_24dp
            ),
            NavigationMenuItem(
                pageCount++,
                "广场",
                SquareFragment::class.java,
                com.ve.lib.common.R.id.home_navigation_1,
                com.ve.lib.application.R.drawable.ic_square_black_24dp
            ),
            NavigationMenuItem(
                pageCount++,
                "公众号",
                WeChatFragment::class.java,
                com.ve.lib.common.R.id.home_navigation_2,
                com.ve.lib.common.R.drawable.ic_wechat_black_24dp
            ),
            NavigationMenuItem(
                pageCount++,
                "体系",
                TreeFragment::class.java,
                com.ve.lib.common.R.id.home_navigation_3,
                com.ve.lib.application.R.drawable.ic_apps_black_24dp
            ),
            NavigationMenuItem(
                pageCount++,
                "项目",
                ProjectFragment::class.java,
                com.ve.lib.common.R.id.home_navigation_4,
                com.ve.lib.application.R.drawable.ic_project_black_24dp
            ),
        )
    }

    /**
     *【译】Android材质组件的动手实践：Bottom Navigation
     * https://www.jianshu.com/p/47941acd4c9c
     */
    fun initNavigation() {
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

    override fun initView() {
        initFragment()
        initNavigation()

        initToolbar(
            mBinding.extToolbar.toolbar,
            mFragmentPageList[mIndex].fragmentTitle,
            homeAsUpEnabled = false
        )

        setSupportActionBar(mBinding.extToolbar.toolbar)
        //设置返回键可用
        supportActionBar?.setHomeButtonEnabled(false)
        //左侧添加一个默认的返回图标
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.extToolbar.toolbar.apply {
            title = "玩安卓"
            navigationIcon = resources.getDrawable(com.ve.lib.application.R.drawable.ic_baseline_menu_24, null);
            setNavigationOnClickListener {
                mBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        showFragment(mIndex)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mIndex == 1) {
            menuInflater.inflate(R.menu.menu_add, menu)
        } else {
            menuInflater.inflate(R.menu.menu_search, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                showMsg("module.module.android.R.id.home")
                return true
            }
            R.id.action_search -> {
                Intent(mContext, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
            R.id.action_add -> {
                CommonActivity.start(mContext, "分享文章", ShareArticleFragment::class.java.name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachViewModelClass(): Class<WanAndroidViewModel> {
        return WanAndroidViewModel::class.java
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
            mBinding.bottomNavigation.menu[index].title = mFragmentPage.fragmentTitle
            mFragmentPage.mFragment = mFragmentPage.getFragment()
            transaction.add(
                R.id.ext_container,
                mFragmentPage.mFragment!!,
                mFragmentPage.fragmentTitle
            )
        } else {
            transaction.show(mFragmentPage.mFragment!!)
        }
        val toolbar = mBinding.extToolbar.toolbar
        toolbar.title = mFragmentPage.fragmentTitle

        transaction.commit()
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
     * 夜间模式刷新
     */
    override fun recreate() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            mFragmentPageList.forEach { mFragmentPage ->
                mFragmentPage.mFragment?.let {
                    fragmentTransaction.remove(it)
                }
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun appRecreateEvent(event: AppRecreateEvent) {
        recreate()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun openDrawer(event: DrawerOpenEvent) {
        mBinding.drawerLayout.openDrawer(Gravity.LEFT)
    }

}