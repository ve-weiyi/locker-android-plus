package com.ve.module.sunny.ui.place

import com.ve.lib.common.network.base.ApiServiceFactory
import com.ve.module.sunny.logic.database.dao.PlaceDao
import com.ve.module.sunny.logic.http.model.Place
import com.ve.module.sunny.logic.http.network.ApiCaiyun

class PlaceRepository{

    private val apiCaiyun=ApiServiceFactory.getService(ApiCaiyun::class.java,ApiCaiyun.BASE_URL)


    //直接从网络搜索，实际应用中应该先查找本地数据库
    suspend fun getPlaceList( placeName: String)=apiCaiyun.searchPlaces(placeName)

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}