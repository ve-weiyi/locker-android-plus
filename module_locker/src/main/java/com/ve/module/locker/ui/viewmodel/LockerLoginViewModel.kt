package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.network.exception.ApiException
import com.ve.module.locker.respository.AuthRepository
import com.ve.module.locker.respository.http.bean.LoginVO

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerLoginViewModel:LockerViewModel() {

    private val loginRepository by lazy { AuthRepository }

    /**
     * 网络获取数据，不允许其他途径改变
     */
    private val _loginState = MutableLiveData<Boolean>()
    private var _loginData = MutableLiveData<LoginVO>()
    /**
     * 页面可观察到的数据，只能通过网络改变
     */
    val loginState: LiveData<Boolean> = _loginState
    val loginData: LiveData<LoginVO> = _loginData

    fun login(username: String?, password: String?) {
        val job = launch(
            block = {
                val loginVO = loginRepository.loginLocker(username, password)
                _loginState.value = loginVO.flag
                _loginData.value= loginVO.data()
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