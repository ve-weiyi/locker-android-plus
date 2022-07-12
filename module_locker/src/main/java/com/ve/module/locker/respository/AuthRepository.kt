package com.ve.module.locker.respository



import com.ve.module.locker.respository.http.bean.LoginVO
import com.ve.module.locker.respository.http.api.LockerApiService
import com.ve.module.locker.respository.http.bean.LockerBaseBean

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object AuthRepository : BaseLockerRepository(){

    private val apiService = LockerApiService.getApiService()

    suspend fun loginLocker(
        username: String?,
        password: String?,
        code: String? = "1234",
    ): LockerBaseBean<LoginVO> {
        return apiService.loginLocker(username, password, code)
    }

    suspend fun registerLocker(
        username: String?,
        password: String?,
        code: String? ,
    ): LockerBaseBean<Any> {
        return apiService.registerLocker(username, password, code)
    }

    suspend fun sendCode(username: String?):LockerBaseBean<Any> {
        return apiService.sendCode(username)
    }
}