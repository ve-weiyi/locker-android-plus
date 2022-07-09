package com.ve.module.sunny.logic.http.model

import com.google.gson.annotations.SerializedName

/**
 * 搜索某经纬度返回的实时天气信息数据格式
 * */
data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)

}