package com.ve.module.locker.model.respository

import com.ve.module.locker.base.BaseLockerRepository
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.model.http.api.LockerApiService
import com.ve.module.locker.model.http.model.*

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
object  PrivacyFolderRepository : BaseLockerRepository(){

    private val apiService = LockerApiService().getApiService()

    suspend fun folderAdd(privacyFolder: PrivacyFolder): LockerBaseBean<Any> =
        apiService.folderAdd(privacyFolder)

    suspend fun folderDelete(privacyFolderId: Int): LockerBaseBean<Any> =
        apiService.folderDelete(privacyFolderId)

    suspend fun folderUpdate(privacyFolder: PrivacyFolder): LockerBaseBean<Any> =
        apiService.folderUpdate(privacyFolder)

    suspend fun folderQuery(id: Int): LockerBaseBean<Any> =
        apiService.folderQuery(id)

    suspend fun folderQueryList(conditionVO: ConditionVO? = null): LockerBaseBean<MutableList<PrivacyFolder>> {
        if (conditionVO == null) {
            return apiService.folderQueryList(ConditionVO())
        } else {
            return apiService.folderQueryList(conditionVO)
        }
    }

}