package com.ve.module.sunny.logic.database.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.ve.lib.application.BaseApplication
import com.ve.module.sunny.logic.http.model.Place

/**
 * 访问和存储数据
 */
object PlaceDao {

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() =
        BaseApplication.mContext.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

}