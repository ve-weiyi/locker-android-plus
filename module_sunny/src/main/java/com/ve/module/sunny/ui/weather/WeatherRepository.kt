package com.ve.module.sunny.ui.weather

import com.ve.lib.common.network.base.ApiServiceFactory
import com.ve.lib.common.utils.log.LogUtil
import com.ve.module.sunny.logic.http.model.Weather
import com.ve.module.sunny.logic.http.network.ApiCaiyun

class WeatherRepository {
   // private val apiCaiyun= ApiServiceCreator.getApiCaiyun()
    private val apiCaiyun=  ApiServiceFactory.getService(ApiCaiyun::class.java,ApiCaiyun.BASE_URL)

    suspend fun refreshWeather(lng: String, lat: String) : Weather? {
        val dailyResult=apiCaiyun.getDailyWeather(lng, lat)
        val realtimeResult=apiCaiyun.getRealtimeWeather(lng, lat)

        if (dailyResult.status == "ok" && realtimeResult.status == "ok") {
            val daily=dailyResult.result.daily
            val realtime=realtimeResult.result.realtime
            LogUtil.d("---WeatherRepository get weather success")
            return Weather(realtime, daily)
        } else {
            LogUtil.d("---WeatherRepository get weather error")
            return null
        }
    }
}