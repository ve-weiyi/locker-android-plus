package com.ve.module.lockit.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.lockit.respository.http.bean.LockitBaseBean
import com.ve.module.lockit.respository.AuthRepository

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockitRegisterViewModel : LockitViewModel() {
    private val authRepository by lazy { AuthRepository }

    val registerState = MutableLiveData<LockitBaseBean<Any>>()

    fun register(
        username: String?,
        password: String?,
        code: String?,
    ) {
        launch(
            block = {
                val result = authRepository.registerlockit(username, password, code)
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