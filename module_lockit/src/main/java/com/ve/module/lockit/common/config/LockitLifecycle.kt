package com.ve.module.lockit.common.config

import androidx.lifecycle.MutableLiveData
import com.ve.module.lockit.respository.http.bean.LoginVO

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object LockitLifecycle {

    val loginState= MutableLiveData<Boolean>()

    val loginData= MutableLiveData<LoginVO>()

}