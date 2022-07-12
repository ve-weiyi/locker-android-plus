package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.lib.common.base.viewmodel.BaseViewModel


class RegisterViewModel : BaseViewModel() {

    private val repository by lazy { WazRepository() }

    private val _registerState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> = _registerState

    fun register(username: String, password: String, repassword: String) {
        launch(
            block = {
                val loginData = repository.register(username, password, repassword)
                _registerState.value = 0 == loginData.code()
            }
        )
    }

}