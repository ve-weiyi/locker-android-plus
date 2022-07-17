package com.ve.module.lockit.respository


import com.ve.module.lockit.respository.http.api.LockitApiService
import com.ve.module.lockit.respository.http.model.QQLoginVO
import com.ve.module.lockit.respository.http.model.UserInfoVO

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object AuthRepository : LockitRepository() {

    private val apiService = LockitApiService.getApiService()

    suspend fun loginLockit(
        username: String?,
        password: String?,
        code: String? = "1234",
    ) = apiService.loginLockit(username, password, code)

    suspend fun loginLockitQQ(qqLoginVO: QQLoginVO
    ) = apiService.loginLockitQQ(qqLoginVO)


    suspend fun registerLockit(username: String, password: String, code: String? )
    = apiService.registerLockit(username, password, code)


    suspend fun sendCode(username: String?) = apiService.sendCode(username)

    suspend fun updateUserInfo(userInfoVO: UserInfoVO)= apiService.updateUserInfo(userInfoVO)
}