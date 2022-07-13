package com.ve.module.lockit.respository.http.bean

import com.ve.lib.common.network.exception.ApiException
import com.ve.lib.common.vutils.ToastUtil

/**
 * @author admin
 * @date 2018/11/21
 * @desc
 */
/**
 * Created by yechaoa on 2020/2/4.
 * Describe :
 */
data class LockitBaseBean<T>(val flag: Boolean, private val code: Int, private val data: T, val message: String) {

    fun code(): Int {
        if (code == 0) {
            return code
        } else {
            throw ApiException(code, message ?: "")
        }
    }

    fun data(): T? {
        if(!flag){
            ToastUtil.showCenter(message)
            return null
        }
        return data
    }


}