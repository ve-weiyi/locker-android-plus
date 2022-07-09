package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.sunny.logic.http.model.Place
import com.ve.module.sunny.logic.http.model.Weather
import com.ve.module.sunny.ui.place.PlaceRepository
import com.ve.module.sunny.ui.weather.WeatherRepository

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerDrawerViewModel:LockerViewModel() {
    private val repository by lazy { WeatherRepository() }
    private val placeRepository by lazy { PlaceRepository() }

    val weatherLiveData = MutableLiveData<Weather>()

    fun refreshWeather(lng: String, lat: String) {
        launch(
            block = {
                weatherLiveData.value=repository.refreshWeather(lng, lat)
            }
        )
    }

    fun savePlace(place: Place) = placeRepository.savePlace(place)
}