package com.ve.lib.common.network.exception

/**
 * 自定义异常,网络访问异常
 */
class ApiException(
    var errorCode: Int,
    var errorMsg: String,
    var throwable: Throwable? = null
) : RuntimeException() {

}