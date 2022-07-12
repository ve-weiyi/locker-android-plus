package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.bean.UserShare
import com.ve.lib.common.base.viewmodel.BaseViewModel

class ShareViewModel: BaseViewModel() {

    private val repository by lazy { WazRepository() }

    val userShare = MutableLiveData<UserShare>()

    fun getUserShare (page :Int){
        //开线程
        launch(
            block = {
                userShare.value =repository.getUserShare(page)
            }
        )
    }
}