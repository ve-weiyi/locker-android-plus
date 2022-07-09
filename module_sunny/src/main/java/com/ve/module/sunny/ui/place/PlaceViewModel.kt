package com.ve.module.sunny.ui.place

import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.module.sunny.logic.http.model.Place

class PlaceViewModel : BaseViewModel() {
    /**
     * MutableLiveData是一种可变的liveData，可以对数据进行感知，当数据发生变化时通知Activity
     * map用于对数据进行转换
     * switchMap：当viewModel中liveData是调用另外的方法获取的，就可以借助该方法将对象转换成另一个可以观察的liveData对象
     * */
    private val repository by lazy { PlaceRepository() }


    val placeList = MutableLiveData<List<Place>>()

    fun getPlaceList(placeName :String){
        launch(
            block = {
                placeList.value=repository.getPlaceList(placeName).places
            }
        )
    }

    fun savePlace(place: Place) = repository.savePlace(place)

    fun getSavedPlace() = repository.getSavedPlace()

    fun isPlaceSaved() = repository.isPlaceSaved()
}