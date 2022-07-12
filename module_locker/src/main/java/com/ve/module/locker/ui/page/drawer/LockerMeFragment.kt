package com.ve.module.locker.ui.page.drawer

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.event.AppRecreateEvent
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.ImageLoader
import com.ve.lib.common.lifecycle.LocationLifecycle
import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.config.LockerConstant
import com.ve.module.locker.common.config.LockerLifecycle

import com.ve.module.locker.databinding.LockerFragmentMeBinding
import com.ve.module.locker.respository.http.bean.LoginVO
import com.ve.module.locker.ui.page.auth.LockerLoginActivity
import com.ve.module.locker.ui.page.container.LockerWebContainerActivity
import com.ve.module.locker.ui.page.feedback.LockerFeedBackActivity
import com.ve.module.locker.ui.page.setting.AboutSettingFragment
import com.ve.module.locker.ui.page.setting.LockerSettingActivity
import com.ve.module.locker.ui.viewmodel.LockerDrawerViewModel
import com.ve.module.sunny.ui.weather.WeatherActivity
import com.ve.module.sunny.util.SkyUtil
import com.ve.module.sunny.util.SunnyConstant
import org.greenrobot.eventbus.EventBus

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerMeFragment : BaseVmFragment<LockerFragmentMeBinding, LockerDrawerViewModel>() , View.OnClickListener {
    override fun attachViewBinding(): LockerFragmentMeBinding{
        return LockerFragmentMeBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerDrawerViewModel> {
        return LockerDrawerViewModel::class.java
    }
    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>
    var location: Location?=null
    var placeName:String?=null

    override fun initView(savedInstanceState: Bundle?) {
        showHeather(SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_STATE_KEY,false))
        showUserInfo(SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_DATA_KEY,LoginVO()))

    }

    override fun initWebData() {
        super.initWebData()

        if(LocationLifecycle.isPermissionEnable(mContext) &&LocationLifecycle.isLocationProviderEnabled(mContext)){
            val locationLifecycle=LocationLifecycle.instance
            lifecycle.addObserver(locationLifecycle)
            launchOnBackground{
                location=locationLifecycle.getLastLocation(mContext)
                placeName= locationLifecycle.getAddress(location)?.get(0)?.featureName
                location?.apply {
                    mViewModel.refreshWeather(location!!.longitude.toString(),location!!.latitude.toString())
                }
            }

        }
    }

    override fun initObserver() {
        super.initObserver()

        LockerLifecycle.loginState.observe(this) {
            SpUtil.setValue(LockerConstant.SP_KEY_LOGIN_STATE_KEY,it)
            LogUtil.msg("$mViewName --$it---  "+ SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_STATE_KEY,false))
            showHeather(it)
        }

        LockerLifecycle.loginData.observe(this) {
            SpUtil.setValue(LockerConstant.SP_KEY_LOGIN_DATA_KEY,it)
            LogUtil.msg("$mViewName --\n$it---  \n"+ SpUtil.getValue(LockerConstant.SP_KEY_LOGIN_DATA_KEY,LoginVO()))
            showUserInfo(it)
        }

        mViewModel.weatherLiveData.observe(this){
            LogUtil.e(it.daily.toString())
            LogUtil.e(it.realtime.toString())
            mBinding.actionTodayWeather.rightTextView.text="${it.realtime.temperature} °C"
            mBinding.actionTodayWeather.rightImageView.setImageResource(SkyUtil.getSky(it.realtime.skycon).icon)
        }
    }

    override fun initListener() {
        super.initListener()

        mBinding.layoutUnLogin.layoutMain.setOnclickNoRepeatListener  {
            startActivity(mContext, LockerLoginActivity::class.java)
        }

        mBinding.exitLayout.setOnclickNoRepeatListener (this)

        mBinding.actionTodayWeather.setOnclickNoRepeatListener(this)
        mBinding.actionSystemSetting.setOnclickNoRepeatListener(this)
        mBinding.actionNightModel.setOnclickNoRepeatListener(this)
        mBinding.actionFeedback.setOnclickNoRepeatListener(this)
        mBinding.actionShare.setOnclickNoRepeatListener(this)


        mBinding.actionBlog.setOnclickNoRepeatListener(this)
        mBinding.actionGithub.setOnclickNoRepeatListener(this)
        mBinding.actionAbout.setOnclickNoRepeatListener(this)

        startActivityLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                LogUtil.msg(result.toString())
            }
    }

    override fun onClick(v: View?) {
        LogUtil.msg("click view ")
        when(v?.id){
            R.id.exit_layout->{
                LockerLifecycle.loginState.value=false
                LockerLifecycle.loginData.value= LoginVO()
                showMsg("退出登录成功")
            }

            R.id.action_blog -> {
                LockerWebContainerActivity.start(mContext, "我的博客", LockerConstant.blog_url)
            }
            R.id.action_github -> {
                LockerWebContainerActivity.start(mContext, "Github", LockerConstant.github_url)
            }

            R.id.action_feedback -> {
                startActivity(mContext, LockerFeedBackActivity::class.java)
            }
            R.id.action_about -> {
                LockerSettingActivity.start(mContext, AboutSettingFragment::class.java.name,"关于")
            }
            R.id.action_system_setting -> {
                LockerSettingActivity.start(mContext)
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
            R.id.action_today_weather->{
                if(location==null){
                    LocationLifecycle.permissionCheck(requireActivity())
                    initViewData()
                }else{
                    LogUtil.e("---"+location!!.longitude+"---"+location!!.latitude+"---"+placeName)
                    val intent = Intent(requireContext(), WeatherActivity::class.java).apply {
                        putExtra(SunnyConstant.KEY_LOCATION_LNG, location!!.longitude.toString())
                        putExtra(SunnyConstant.KEY_LOCATION_LAT, location!!.latitude.toString())
                        putExtra(SunnyConstant.KEY_PLACE_NAME, placeName)
                    }
                    startActivity(intent)
                }
            }

            R.id.action_share->{
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "locker 一款简单的私密信息管理工具")
                    type = "text/plain"
                    startActivity(Intent.createChooser(this,"分享"))
                }
            }
        }
    }


    private fun showHeather(isLogin: Boolean) {
        if (isLogin) {
            mBinding.layoutUserinfo.layoutMain.visibility = View.VISIBLE
            mBinding.layoutUnLogin.layoutMain.visibility = View.GONE
        } else {
            mBinding.layoutUserinfo.layoutMain.visibility = View.GONE
            mBinding.layoutUnLogin.layoutMain.visibility = View.VISIBLE
        }
    }

    private fun showUserInfo(it :LoginVO){
        mBinding.layoutUserinfo.apply {
            val item = it.userDetailDTO
            ImageLoader.load(mContext, item?.avatar, ivAvatarIcon)
            tvNickname.text = item?.nickname
            itemTvUserIntro.text = item?.intro
        }
    }

}