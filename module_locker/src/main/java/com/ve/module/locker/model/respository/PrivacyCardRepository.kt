package com.ve.module.locker.model.respository

import com.ve.module.locker.base.BaseLockerRepository
import com.ve.module.locker.model.http.model.PrivacyDetailsCard
import com.ve.module.locker.model.http.model.UserPrivacyInfoCardResponse
import com.ve.module.locker.model.http.api.LockerApiService
import com.ve.module.locker.model.http.model.LockerBaseBean

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object  PrivacyCardRepository : BaseLockerRepository(){

    private val apiService= LockerApiService().getApiService()
    

    suspend fun privacyInfoList(): LockerBaseBean<MutableList<PrivacyDetailsCard>>
    = apiService.privacyInfoCardList()

    suspend fun privacyInfoAdd(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockerBaseBean<Any>
    = apiService.privacyInfoCardAdd(userPrivacyInfoCardVO)

    suspend fun privacyInfoDelete(privacyInfoId :Int): LockerBaseBean<Any>
    = apiService.privacyInfoCardDelete(privacyInfoId)

    suspend fun privacyInfoUpdate(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockerBaseBean<Any>
    = apiService.privacyInfoCardUpdate(userPrivacyInfoCardVO)

    suspend fun privacyInfoUser(): LockerBaseBean<MutableList<UserPrivacyInfoCardResponse>>
    = apiService.privacyInfoCardUser()

    suspend fun privacyInfoParsing(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockerBaseBean<Any>
    = apiService.privacyInfoCardParsing(userPrivacyInfoCardVO)
}