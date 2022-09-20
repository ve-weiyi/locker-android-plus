package com.ve.module.android.ui.page.user

import android.content.DialogInterface
import android.content.Intent
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.navigation.NavigationView
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.WazFragmentDrawerBinding
import com.ve.lib.common.event.AppRecreateEvent
import com.ve.lib.common.event.LoginEvent
import com.ve.module.android.repository.bean.UserInfoBody
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.page.activity.RankActivity
import com.ve.module.android.ui.page.activity.ScoreActivity
import com.ve.module.android.ui.page.activity.ShareActivity
import com.ve.module.android.ui.page.setting.SettingsActivity
import com.ve.module.android.ui.page.todo.TodoActivity
import com.ve.module.android.ui.viewmodel.WanAndroidViewModel
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.lib.common.utils.system.PreferenceUtil
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.utils.system.LogUtil
import com.ve.module.android.ui.page.fragment.CollectFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
/**
 * weiyi
 */
class DrawerFragment : BaseVmFragment<WazFragmentDrawerBinding, WanAndroidViewModel>() {

    companion object{
        fun newInstance()= DrawerFragment()
    }
    override fun attachViewBinding(): WazFragmentDrawerBinding {
        return WazFragmentDrawerBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<WanAndroidViewModel> {
        return WanAndroidViewModel::class.java
    }

    /**
     * local username
     */
    private var username: String by PreferenceUtil(Constant.USERNAME_KEY, "vee")
    /**
     * username TextView
     */
    private var nav_username: TextView? = null
    /**
     * user_id TextView  用户id
     */
    private var nav_user_id: TextView? = null
    /**
     * user_grade TextView  用户等级
     */
    private var nav_user_grade: TextView? = null
    /**
     * user_rank TextView  用户排名
     */
    private var nav_user_rank: TextView? = null
    /**
     * score TextView
     */
    private var nav_score: TextView? = null
    /**
     * rank ImageView
     */
    private var nav_rank: ImageView? = null
    private var nav_header_icon: ImageView?=null

    private var isLogin=true

    override fun initView() {
        nav_username=mBinding.tvUsername
        nav_user_id=mBinding.tvUserId
        nav_user_rank=mBinding.tvUserRank
        nav_user_grade=mBinding.tvUserGrade

        nav_rank=mBinding.ivRank
        nav_header_icon=mBinding.navHeaderIcon

        nav_score=  mBinding.drawerNavView.menu.findItem(R.id.nav_score).actionView as TextView
        nav_score?.gravity = Gravity.CENTER_VERTICAL

        nav_header_icon?.setImageDrawable(requireActivity().getDrawable(R.drawable.tiger64))

        nav_username?.run {
            text = if (!isLogin) "去登录" else username
            setOnClickListener {
                LogUtil.msg(mViewName+"go login")
                if (!isLogin) {
                    Intent(requireContext(), LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }
        nav_rank!!.setOnClickListener {
            LogUtil.msg(mViewName+"go rank")
            startActivity(Intent(activity, RankActivity::class.java))
        }
        mBinding.drawerNavView.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
    }

    override fun loadWebData() {
        super.loadWebData()
        mViewModel.getUserInfo()
    }
    override fun initObserver() {
        super.initObserver()
        mViewModel._logout.observe(this){
            showLogoutSuccess(it==0)
        }
        mViewModel.userInfo.observe(this) {
            LogUtil.d(it.toString())
            showUserInfo(it)
        }
    }
    /**
     * 显示用户信息，包括积分、等级、排名
     */
    fun showUserInfo(bean: UserInfoBody) {
        nav_username?.text=username
        nav_user_id?.text = bean.userId.toString()
        nav_user_grade?.text = (bean.coinCount / 100 + 1).toString()
        nav_user_rank?.text = bean.rank.toString()
        nav_score!!.text = bean.coinCount.toString()
    }
    fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                // CookieManager().clearAllCookies()
                PreferenceUtil.clearPreference()
                uiThread {
                    showMsg("登录成功")
                    username = nav_username?.text.toString().trim()
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        LogUtil.msg(mViewName+" get login event")
        if (event.isLogin) {
            //登录事件
            nav_username?.text = username
            mBinding.drawerNavView.menu.findItem(R.id.nav_logout).isVisible = true
            mViewModel.getUserInfo()
        } else {
            // 退出登录事件，重置用户信息
            nav_username?.text = "去登录"
            mBinding.drawerNavView.menu.findItem(R.id.nav_logout).isVisible = false
            nav_user_id?.text = "----"
            nav_user_grade?.text = "--"
            nav_user_rank?.text = "--"
            nav_score!!.text = ""
        }
    }

    /**
     * DrawerNavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            LogUtil.msg("click"+item.title)
            when (item.itemId) {
                R.id.nav_score -> {
                    Intent(requireContext(), ScoreActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_collect -> {
                    CommonActivity.start(requireContext(),"我的收藏", CollectFragment::class.java.name)
                }
                R.id.nav_share -> {
                    Intent(requireContext(), ShareActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_setting -> {
                    Intent(requireContext(), SettingsActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_night_mode -> {
                    if (SettingUtil.getIsNightMode()) {
                        SettingUtil.setIsNightMode(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        SettingUtil.setIsNightMode(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    EventBus.getDefault().post(AppRecreateEvent())
                }
                R.id.nav_todo -> {
                    Intent(requireContext(), TodoActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
            true
        }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(requireContext(),"正在登录",
            DialogInterface.OnClickListener { _, _ ->
                mViewModel.userLogout()
            }).show()
    }

}