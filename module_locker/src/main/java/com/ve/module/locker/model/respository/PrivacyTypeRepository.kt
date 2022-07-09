package com.ve.module.locker.model.respository

import com.ve.module.locker.base.BaseLockerRepository
import com.ve.module.locker.model.http.model.Type
import com.ve.module.locker.model.http.api.LockerApiService
import com.ve.module.locker.model.http.model.*

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object  PrivacyTypeRepository : BaseLockerRepository(){

    private val apiService = LockerApiService().getApiService()

    suspend fun typeAdd(privacyType: Type): LockerBaseBean<Any> =
        apiService.typeAdd(privacyType)

    suspend fun typeDelete(privacyTypeId: Int): LockerBaseBean<Any> =
        apiService.typeDelete(privacyTypeId)

    suspend fun typeUpdate(privacyType: Type): LockerBaseBean<Any> =
        apiService.typeUpdate(privacyType)

    suspend fun typeQuery(id: Int): LockerBaseBean<Any> =
        apiService.typeQuery(id)

    suspend fun typeQueryList(conditionVO: ConditionVO? = null): LockerBaseBean<MutableList<Type>> {
        if (conditionVO == null) {
            return apiService.typeQueryList(ConditionVO())
        } else {
            return apiService.typeQueryList(conditionVO)
        }
    }


}