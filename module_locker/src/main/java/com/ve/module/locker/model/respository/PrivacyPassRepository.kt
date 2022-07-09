package com.ve.module.locker.model.respository

import com.ve.module.locker.model.http.model.PrivacyDetailsPass
import com.ve.module.locker.model.http.model.UserPrivacyInfoPassVO
import com.ve.module.locker.model.http.api.LockerApiService
import com.ve.module.locker.model.http.model.LockerBaseBean

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object  PrivacyPassRepository {

    val apiService= LockerApiService().getApiService()
    

    suspend fun privacyInfoList(): LockerBaseBean<MutableList<PrivacyDetailsPass>>
    = apiService.privacyInfoPassList()

    suspend fun privacyInfoAdd(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockerBaseBean<Any>
    = apiService.privacyInfoPassAdd(userPrivacyInfoPassVO)

    suspend fun privacyInfoDelete(privacyInfoId :Int): LockerBaseBean<Any>
    = apiService.privacyInfoPassDelete(privacyInfoId)

    suspend fun privacyInfoUpdate(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockerBaseBean<Any>
    = apiService.privacyInfoPassUpdate(userPrivacyInfoPassVO)

    suspend fun privacyInfoUser(): LockerBaseBean<MutableList<UserPrivacyInfoPassVO>>
    = apiService.privacyInfoPassUser()

    suspend fun privacyInfoParsing(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockerBaseBean<Any>
    = apiService.privacyInfoPassParsing(userPrivacyInfoPassVO)
}