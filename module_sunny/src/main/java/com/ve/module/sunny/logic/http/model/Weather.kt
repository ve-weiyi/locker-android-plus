package com.ve.module.sunny.logic.http.model

/**
 * 用于将Realtime和Daily类封装
 * */
data class Weather(
    val realtime: RealtimeResponse.Realtime,
    val daily: DailyResponse.Daily
)