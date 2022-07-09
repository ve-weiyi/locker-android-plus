package com.ve.module.locker.ui.page.drawer

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.event.AppRecreateEvent
import com.ve.lib.common.ext.setOnclickNoRepeatListener

import com.ve.lib.common.utils.SettingUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.config.LockerConstant
import com.ve.module.locker.databinding.LockerFragmentDrawerActionBinding
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.container.LockerWebContainerActivity
import com.ve.module.locker.ui.page.feedback.LockerFeedBackActivity
import com.ve.module.locker.ui.page.category.list.LockerListFolderFragment
import com.ve.module.locker.ui.page.category.list.LockerListTagFragment
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
 * @Date 2022/4/8
 */
class LockerDrawerActionFragment :
    BaseVmFragment<LockerFragmentDrawerActionBinding, LockerDrawerViewModel>(),
    View.OnClickListener {

    override fun attachViewBinding(): LockerFragmentDrawerActionBinding {
        return LockerFragmentDrawerActionBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerDrawerViewModel> {
        return LockerDrawerViewModel::class.java
    }

    private lateinit var startActivityLaunch: ActivityResultLauncher<Intent>
    lateinit var location: Location
    lateinit var placeName:String
    override fun initView(savedInstanceState: Bundle?) {

        mBinding.actionTag.setOnclickNoRepeatListener(this)
        mBinding.actionFolder.setOnclickNoRepeatListener(this)

        mBinding.actionSystemSetting.setOnclickNoRepeatListener(this)
        mBinding.actionNightModel.setOnclickNoRepeatListener(this)
        mBinding.actionFeedback.setOnclickNoRepeatListener(this)
        mBinding.actionBlog.setOnclickNoRepeatListener(this)
        mBinding.actionGithub.setOnclickNoRepeatListener(this)
        mBinding.actionAbout.setOnclickNoRepeatListener(this)
        mBinding.actionAutoFill.setOnclickNoRepeatListener(this)
        mBinding.actionShare.setOnClickListener(this)
//        mBinding.actionTodayWeather.setOnclickNoRepeatListener(this)
        startActivityLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                LogUtil.msg(result.toString())
            }


    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.weatherLiveData.observe(this){
            LogUtil.e(it.daily.toString())
            LogUtil.e(it.realtime.toString())
            mBinding.actionTodayWeather.rightTextView.text="${it.realtime.temperature} °C"
            mBinding.actionTodayWeather.rightImageView.setImageResource(SkyUtil.getSky(it.realtime.skycon).icon)
        }
    }
    override fun initWebData() {
        super.initWebData()
//        mViewModel.refreshWeather(location.longitude.toString(),location.latitude.toString())
    }

    override fun onClick(v: View?) {
        LogUtil.msg("click view ")
        when (v?.id) {
            R.id.action_tag -> {
                LockerContainerActivity.start(
                    mContext,
                    LockerListTagFragment::class.java.name,
                    "标签管理"
                )
            }
            R.id.action_folder -> {
                LockerContainerActivity.start(
                    mContext,
                    LockerListFolderFragment::class.java.name,
                    "文件夹管理"
                )
            }
            R.id.action_auto_fill -> {
                //打开自动填充服务设置界面
                val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE)
                intent.data = Uri.parse("package:com.android.settings")
                startActivityLaunch.launch(intent)
            }
            R.id.action_blog -> {
                LockerWebContainerActivity.start(mContext, "我的博客", LockerConstant.blog_url)
            }
            R.id.action_github -> {
                LockerWebContainerActivity.start(mContext, "我的博客", LockerConstant.github_url)
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
                LogUtil.e("---"+location.longitude+"---"+location.latitude+"---"+placeName)
                val intent = Intent(requireContext(), WeatherActivity::class.java).apply {
                    putExtra(SunnyConstant.KEY_LOCATION_LNG, location.longitude.toString())
                    putExtra(SunnyConstant.KEY_LOCATION_LAT, location.latitude.toString())
                    putExtra(SunnyConstant.KEY_PLACE_NAME, placeName)
                }
                startActivity(intent)
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
}