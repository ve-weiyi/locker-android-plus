package com.ve.module.sunny.logic.http.network;

import com.ve.module.sunny.logic.http.model.DailyResponse
import com.ve.module.sunny.logic.http.model.HourlyData
import com.ve.module.sunny.logic.http.model.SearchPlaceResponse
import com.ve.module.sunny.logic.http.model.RealtimeResponse
import com.ve.module.sunny.util.SunnyConstant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by ve 2021/5/4.
 * Describe :
 */
interface ApiCaiyun {

    companion object {
        const val BASE_URL = "https://api.caiyunapp.com/"
    }

    //返回值为call类型时可以对返回值进行处理

    //url=https://api.caiyunapp.com/v2/place?query="query"&token="SUNNY_TOKEN"&lang=zh_CN
    // @Path用于注解{ }，@Query用于注解 ？query=" "

    //-----------------------【查找地点】----------------------
    @GET("v2/place?token=${SunnyConstant.CAIYUN_TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(@Query("query") query: String): SearchPlaceResponse

    //-----------------------【查找天气】----------------------
    @GET("v2.5/${SunnyConstant.CAIYUN_TOKEN}/{lng},{lat}/realtime.json")
    suspend fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): RealtimeResponse

    @GET("v2.5/${SunnyConstant.CAIYUN_TOKEN}/{lng},{lat}/daily.json")
    suspend fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): DailyResponse

    @GET("v2.5/${SunnyConstant.CAIYUN_TOKEN}/{lng},{lat}/hourly.json?hourlysteps=12")
    suspend fun getHourlyWeather(
        @Path("lng") lng: String?,
        @Path("lat") lat: String?
    ): HourlyData
}
