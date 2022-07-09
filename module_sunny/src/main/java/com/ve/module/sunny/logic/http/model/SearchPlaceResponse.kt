package com.ve.module.sunny.logic.http.model

import com.google.gson.annotations.SerializedName

/**
 * 搜索城市返回的城市列表数据格式 ，PlaceResponse对 status，Place进行封装
 * */
data class SearchPlaceResponse(val status: String, val places: List<Place>)

data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

data class Location(val lng: String, val lat: String)



