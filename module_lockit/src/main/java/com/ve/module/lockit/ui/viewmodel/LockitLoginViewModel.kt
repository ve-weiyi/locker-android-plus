package com.ve.module.lockit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.network.exception.ApiException
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.lockit.respository.AuthRepository
import com.ve.module.lockit.respository.http.bean.LoginDTO
import com.ve.module.lockit.respository.http.model.QQLoginVO
import com.ve.module.lockit.respository.http.model.UserInfoVO

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockitLoginViewModel:LockitViewModel() {

    /**
     * 网络获取数据，不允许其他途径改变
     */
    private val _loginState = MutableLiveData<Boolean>()
    private var _loginData = MutableLiveData<LoginDTO>()
    /**
     * 页面可观察到的数据，只能通过网络改变
     */
    val loginState: LiveData<Boolean> = _loginState
    val loginData: LiveData<LoginDTO> = _loginData

    fun login(username: String?, password: String?) {
        val job = launch(
            block = {
                LogUtil.msg(username+" "+password)
                val loginVO = AuthRepository.loginLockit(username, password)
                LogUtil.msg(loginVO)
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
    
    fun qqLogin(qqLoginVO: QQLoginVO){
        launch(
            block = {
//                val loginVO = AuthRepository.loginLockit(qqLoginVO.openId,qqLoginVO.accessToken)
                val loginVO = AuthRepository.loginLockitQQ(qqLoginVO)

                LogUtil.msg(loginVO)
                if(loginVO.data()==null){
                    //注册
                }else{
                    _loginData.value= loginVO.data()
                }
            }
        )
    }
    
    fun updateUserInfo(userInfoVO: UserInfoVO){
        launch(
            block = {
                AuthRepository.updateUserInfo(userInfoVO)
            }
        )
    }
}