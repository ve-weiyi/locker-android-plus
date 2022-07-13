package com.ve.module.lockit.respository



import com.ve.module.lockit.respository.http.bean.LoginVO
import com.ve.module.lockit.respository.http.api.LockitApiService
import com.ve.module.lockit.respository.http.bean.LockitBaseBean

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object AuthRepository : LockitRepository(){

    private val apiService = LockitApiService.getApiService()

    suspend fun loginlockit(
        username: String?,
        password: String?,
        code: String? = "1234",
    ): LockitBaseBean<LoginVO> {
        return apiService.loginlockit(username, password, code)
    }

    suspend fun registerlockit(
        username: String?,
        password: String?,
        code: String? ,
    ): LockitBaseBean<Any> {
        return apiService.registerlockit(username, password, code)
    }

    suspend fun sendCode(username: String?):LockitBaseBean<Any> {
        return apiService.sendCode(username)
    }
}