package com.ve.module.lockit.ui.page.drawer

import android.content.Intent
import android.location.Location
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.event.AppRecreateEvent
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.data.ImageLoader
import com.ve.lib.common.lifecycle.LocationLifecycle
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.system.SpUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.common.config.LockitConstant
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.common.event.RefreshDataEvent

import com.ve.module.lockit.databinding.LockitFragmentMeBinding
import com.ve.module.lockit.respository.http.bean.LoginDTO
import com.ve.module.lockit.ui.page.auth.LockitLoginActivity
import com.ve.module.lockit.ui.page.container.LockitWebContainerActivity
import com.ve.module.lockit.ui.page.feedback.LockitFeedBackActivity
import com.ve.module.lockit.ui.page.setting.AboutSettingFragment
import com.ve.module.lockit.ui.page.setting.LockitSettingActivity
import com.ve.module.lockit.ui.viewmodel.LockitDrawerViewModel
import com.ve.module.sunny.ui.weather.WeatherActivity
import com.ve.module.sunny.util.SkyUtil
import com.ve.module.sunny.util.SunnyConstant
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockitMeFragment : BaseVmFragment<LockitFragmentMeBinding, LockitDrawerViewModel>(),
    View.OnClickListener {
    override fun attachViewBinding(): LockitFragmentMeBinding {
        return LockitFragmentMeBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitDrawerViewModel> {
        return LockitDrawerViewModel::class.java
    }

    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>
    var location: Location? = null
    var placeName: String? = null

    override fun initView() {
        showUserInfo(SpUtil.getValue(LockitSpKey.SP_KEY_LOGIN_DATA_KEY, LoginDTO()::class.java))
    }

    override fun initWebData() {
        super.initWebData()

        if (LocationLifecycle.isPermissionEnable(mContext) && LocationLifecycle.isLocationProviderEnabled(
                mContext
            )
        ) {
            val locationLifecycle = LocationLifecycle.instance
            lifecycle.addObserver(locationLifecycle)
            launchOnBackground {
                location = locationLifecycle.getLastLocation(mContext)
                placeName = locationLifecycle.getAddress(location)?.get(0)?.featureName
                location?.apply {
                    mViewModel.refreshWeather(
                        location!!.longitude.toString(),
                        location!!.latitude.toString()
                    )
                }
            }

        }
    }

    override fun initObserver() {
        super.initObserver()

        mViewModel.weatherLiveData.observe(this) {
            LogUtil.e(it.daily.toString())
            LogUtil.e(it.realtime.toString())
            mBinding.actionTodayWeather.rightTextView.text = "${it.realtime.temperature} °C"
            mBinding.actionTodayWeather.rightImageView.setImageResource(SkyUtil.getSky(it.realtime.skycon).icon)
        }
    }

    override fun initListener() {
        super.initListener()

        mBinding.layoutUnLogin.layoutMain.setOnclickNoRepeatListener {
            startActivity(mContext, LockitLoginActivity::class.java)
        }

        mBinding.exitLayout.setOnclickNoRepeatListener(this)

        mBinding.actionTodayWeather.setOnclickNoRepeatListener(this)
        mBinding.actionSystemSetting.setOnclickNoRepeatListener(this)
        mBinding.actionNightModel.setOnclickNoRepeatListener(this)
        mBinding.actionFeedback.setOnclickNoRepeatListener(this)
        mBinding.actionShare.setOnclickNoRepeatListener(this)


        mBinding.actionBlog.setOnclickNoRepeatListener(this)
        mBinding.actionGithub.setOnclickNoRepeatListener(this)
        mBinding.actionAbout.setOnclickNoRepeatListener(this)

        startActivityLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                LogUtil.msg(result.toString())
            }
    }

    override fun onClick(v: View?) {
        LogUtil.msg("click view ")
        when (v?.id) {
            R.id.exit_layout -> {
                SpUtil.clearPreference(LockitSpKey.SP_KEY_LOGIN_DATA_KEY)
                EventBus.getDefault().post(RefreshDataEvent(LoginDTO::class.java.name,null))
                showMsg("退出登录成功")
            }

            R.id.action_blog -> {
                LockitWebContainerActivity.start(mContext, "我的博客", LockitConstant.blog_url)
            }
            R.id.action_github -> {
                LockitWebContainerActivity.start(mContext, "Github", LockitConstant.github_url)
            }

            R.id.action_feedback -> {
                startActivity(mContext, LockitFeedBackActivity::class.java)
            }
            R.id.action_about -> {
                LockitSettingActivity.start(mContext, AboutSettingFragment::class.java.name, "关于")
            }
            R.id.action_system_setting -> {
                LockitSettingActivity.start(mContext)
            }
            R.id.action_night_model -> {
                if (SettingUtil.getIsNightMode()) {
                    SettingUtil.setIsNightMode(false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    SettingUtil.setIsNightMode(true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                EventBus.getDefault().post(AppRecreateEvent())
            }
            R.id.action_today_weather -> {
                if (location == null) {
                    LocationLifecycle.permissionCheck(requireActivity())
                    initData()
                } else {
                    LogUtil.e("---" + location!!.longitude + "---" + location!!.latitude + "---" + placeName)
                    val intent = Intent(requireContext(), WeatherActivity::class.java).apply {
                        putExtra(SunnyConstant.KEY_LOCATION_LNG, location!!.longitude.toString())
                        putExtra(SunnyConstant.KEY_LOCATION_LAT, location!!.latitude.toString())
                        putExtra(SunnyConstant.KEY_PLACE_NAME, placeName)
                    }
                    startActivity(intent)
                }
            }

            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "lockit 一款简单的私密信息管理工具")
                    type = "text/plain"
                    startActivity(Intent.createChooser(this, "分享"))
                }
            }
        }
    }


    private fun showUserInfo(it: LoginDTO?){
        LogUtil.d(it.toString())
        if(it==null){
            mBinding.layoutUserinfo.layoutMain.visibility = View.GONE
            mBinding.layoutUnLogin.layoutMain.visibility = View.VISIBLE
        }else{

            mBinding.layoutUserinfo.layoutMain.visibility = View.VISIBLE
            mBinding.layoutUnLogin.layoutMain.visibility = View.GONE

            mBinding.layoutUserinfo.apply {
                val item = it.userInfoDTO
                ImageLoader.load(mContext, item?.avatar, ivAvatarIcon)
                tvNickname.text = item?.nickname
                itemTvUserIntro.text = item?.intro
            }
        }
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (LoginDTO::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            if (event.data is LoginDTO) {
                showUserInfo(event.data)
            }else{
                showUserInfo(null)
            }
            hasLoadData = false
        }
    }
}