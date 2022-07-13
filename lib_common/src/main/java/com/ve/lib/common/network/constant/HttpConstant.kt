package com.ve.lib.common.network.constant

/**
 * @author chenxz
 * @date 2018/11/21
 * @desc HttpConstant
 */
object HttpConstant {

    const val DEFAULT_TIMEOUT: Long = 10L

    const val CALL_TIMEOUT: Long = 10L
    const val CONNECT_TIMEOUT: Long = 20L
    const val IO_TIMEOUT: Long = 20L

    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

    const val TOKEN_KEY = "token"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"
}

object HttpErrorCode {
    const val SUCCESS = 0
}