package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.locker.model.http.model.LockerBaseBean
import com.ve.module.locker.model.respository.AuthRepository

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerRegisterViewModel : LockerViewModel() {
    private val authRepository by lazy { AuthRepository }

    val registerState = MutableLiveData<LockerBaseBean<Any>>()

    fun register(
        username: String?,
        password: String?,
        code: String?,
    ) {
        launch(
            block = {
                val result = authRepository.registerLocker(username, password, code)
                registerState.value=result
            }
        )
    }

    val sendCodeState = MutableLiveData<Boolean>()
    fun sendCode(username: String?,){
        launch(
            block = {
                val result = authRepository.sendCode(username)
                sendCodeState.value=result.flag
            }
        )
    }
}