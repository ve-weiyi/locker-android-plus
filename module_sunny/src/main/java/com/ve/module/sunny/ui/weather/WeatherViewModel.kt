package com.ve.module.sunny.ui.weather

import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.module.sunny.logic.http.model.Location
import com.ve.module.sunny.logic.http.model.Weather

class WeatherViewModel : BaseViewModel() {
    private val repository by lazy { WeatherRepository() }

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData = MutableLiveData<Weather>()

    fun refreshWeather(lng: String, lat: String) {
        launch(
            block = {
                weatherLiveData.value=repository.refreshWeather(lng, lat)
            }
        )
    }

}