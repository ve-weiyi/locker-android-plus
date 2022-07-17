package com.ve.module.lockit.respository.http.model

/**
 * QQ登录
 * username--openid
 * @author yezhqiu
 * @date 2021/06/14
 * @since 1.0.0
 */
data class QQLoginVO(
    /**
     * openId
     */
    val openId: String,

    /**
     * accessToken
     */
    val accessToken: String
) {

}