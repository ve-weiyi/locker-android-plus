package com.ve.module.sunny.ui.weather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.sunny.R
import com.ve.module.sunny.databinding.ActivityWeatherBinding
import com.ve.module.sunny.databinding.ForecastBinding
import com.ve.module.sunny.databinding.LifeIndexBinding
import com.ve.module.sunny.databinding.NowBinding
import com.ve.module.sunny.logic.http.model.Weather
import com.ve.module.sunny.util.SkyUtil
import com.ve.module.sunny.util.SunnyConstant
import java.text.SimpleDateFormat
import java.util.*


/**
 * 坑：include布局一定要命名，nowBinding= NowBinding.inflate(layoutInflater)不能修改当前布局中的数据
 * 坑：setContentView()在 binding初始化前时，对布局操作无效
 * 两种操作都可能导致布局加载失败
 * */
class WeatherActivity : BaseVmActivity<ActivityWeatherBinding, WeatherViewModel>() {

    private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding


    override fun attachViewModelClass(): Class<WeatherViewModel> {
        return WeatherViewModel::class.java
    }

    override fun attachViewBinding(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun initObserver() {
        mViewModel.weatherLiveData.observe(this, Observer { result ->
            if (result != null) {
                Toast.makeText(this, mViewModel.placeName, Toast.LENGTH_SHORT).show()
                showWeatherInfo(result)
            } else {
                Toast.makeText(this, "无法成功获取天气信息"+mViewModel.placeName, Toast.LENGTH_SHORT).show()
            }
            mBinding.swipeRefresh.isRefreshing = false
        })
    }

    override fun initView(savedInstanceState: Bundle?) {

        nowBinding = mBinding.weatherNow
        forecastBinding = mBinding.weatherForecast
        lifeIndexBinding = mBinding.weatherLifeIndex
    }

    override fun initWebData() {

        if (mViewModel.locationLng.isEmpty()) {
            mViewModel.locationLng = intent.getStringExtra(SunnyConstant.KEY_LOCATION_LNG) ?: ""
        }
        if (mViewModel.locationLat.isEmpty()) {
            mViewModel.locationLat = intent.getStringExtra(SunnyConstant.KEY_LOCATION_LAT) ?: ""
        }
        if (mViewModel.placeName.isEmpty()) {
            mViewModel.placeName = intent.getStringExtra(SunnyConstant.KEY_PLACE_NAME) ?: ""
        }
        LogUtil.d("--WeatherActivity show -- " + mViewModel.locationLng + " " + mViewModel.locationLat + " " + mViewModel.placeName)
        mBinding.swipeRefresh.setColorSchemeResources(com.ve.lib.application.R.color.colorPrimary)
        refreshWeather()
    }

    override fun initListener() {
        mBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        //返回主页面
        nowBinding.navBtn.setOnClickListener {
            LogUtil.d("----return weather ")
            finish()
        }
        //返回主页面
        nowBinding.nowSwipe.setOnClickListener {
            LogUtil.d("----swap weather ")
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        mBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                //侧滑栏关闭时，也关闭输入法
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather() {
        mViewModel.refreshWeather(mViewModel.locationLng, mViewModel.locationLat)
        mBinding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {

        nowBinding.placeName.text = mViewModel.placeName

        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} °C"
        nowBinding.currentTemp.text = currentTempText
        nowBinding.currentSky.text = SkyUtil.getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        nowBinding.currentAQI.text = currentPM25Text
        nowBinding.nowLayout.setBackgroundResource(SkyUtil.getSky(realtime.skycon).bg)


        LogUtil.d("--showWeatherInfo-- " + nowBinding.placeName.text + nowBinding.currentTemp.text)
        // 填充forecast.xml布局中的数据
        forecastBinding.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, forecastBinding.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = SkyUtil.getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastBinding.forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc

        mBinding.weatherLayout.visibility = View.VISIBLE
    }


}