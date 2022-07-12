package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.LoginData
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.http.exception.ApiException

import com.ve.lib.common.vutils.LogUtil

class LoginViewModel : BaseViewModel() {

    private val loginRepository by lazy { WazRepository() }

    private val _loginState = MutableLiveData<Boolean>()

    private var _loginData = MutableLiveData<LoginData>()

    val loginState: LiveData<Boolean> = _loginState
    val loginData: LiveData<LoginData> = _loginData

    fun login(username: String?, password: String?) {
        val job = launch(
            block = {
                val userBean = loginRepository.login(username, password)
                _loginState.value = (0 == userBean.code())
                _loginData.value= userBean.data()
                LogUtil.d("block =${userBean.code()}")
                LogUtil.d("block =${userBean.data()}")
            },
            error = {
                //(其实在BaseViewModel处理就行了，这里作为演示)
                if (it is ApiException) {
                    // -1001 代表登录失效，需要重新登录
                    if (-1001 == it.errorCode) {
                        _loginState.value = false
                    }
                }
            },
            cancel = {

            },
            showErrorToast = false
        )

        //取消操作 即返回的launch对象
        //cancelJob(job)
    }
}