package com.ve.lib.common.base.repository

import com.ve.lib.common.http.base.BaseResponse
import com.ve.lib.common.http.constant.HttpErrorCode
import com.ve.lib.common.http.exception.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 *
 * Repository 获取网络数据
 * Dao 处理本地数据库数据
 */
open class BaseRepository {

    /**
     * 在仓库层处理 bean
     */
    suspend fun <T> apiCall(call: suspend () -> BaseResponse<T>): T? {
        return withContext(Dispatchers.IO) {
            val response = call.invoke()
            executeResponse(response)
        }
    }

    private fun <T> executeResponse(response: BaseResponse<T>): T? {
        when (response.errorCode) {
            HttpErrorCode.SUCCESS -> {
                return response.data
            }
            else -> {
                throw ApiException(response.errorCode, response.errorMsg)
            }
        }
    }



}