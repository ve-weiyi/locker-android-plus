package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.CoinInfo
import com.ve.lib.common.base.viewmodel.BaseViewModel

class RankViewModel : BaseViewModel(){

    private val repository by lazy { WazRepository() }
    val rankList = MutableLiveData<MutableList<CoinInfo>>()

    fun getUserScore (page :Int){
        //开线程
        launch(
            block = {
                rankList.value = repository.getRankList(page).datas
            }
        )
    }

}