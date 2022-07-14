package com.ve.module.lockit.respository.http.bean

import java.io.Serializable


/**
 * QQ登录
 *
 * @author yezhqiu
 * @date 2021/06/14
 * @since 1.0.0
 */
data class LoginVO (
    val token: String,
    val userInfoDTO: UserInfoDTO
) :Serializable{

    constructor() : this("", UserInfoDTO()) {

    }

    companion object{
        //不反序列化的话,一般不需要写serialVersionUID
        @JvmStatic
        val serialVersionUID = -4921809665590591150L
    }
}