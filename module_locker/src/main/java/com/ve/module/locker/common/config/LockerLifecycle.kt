package com.ve.module.locker.common.config

import androidx.lifecycle.MutableLiveData
import com.ve.module.locker.respository.http.bean.LoginVO

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object LockerLifecycle {

    val loginState= MutableLiveData<Boolean>()

    val loginData= MutableLiveData<LoginVO>()

}