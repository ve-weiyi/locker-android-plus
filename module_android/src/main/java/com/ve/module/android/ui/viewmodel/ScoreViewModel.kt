package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.UserScore
import com.ve.lib.common.base.viewmodel.BaseViewModel


class ScoreViewModel: BaseViewModel(){

    private val repository by lazy { WazRepository() }

    val userScore = MutableLiveData<MutableList<UserScore>>()

    fun getUserScore (page :Int){
        //开线程
        launch(
            block = {
                userScore.value = repository.getUserScore(page).datas
            }
        )
    }
}