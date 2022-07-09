package com.ve.module.locker.model.db.entity

import org.litepal.annotation.Encrypt
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyCardDetails(
    var id: Long = 0,

    //(varue = "拥有人")
    @Encrypt(algorithm = AES)
    var owner: String,

    //(varue = "卡号")
    @Encrypt(algorithm = AES)
    var number: String,

    @Encrypt(algorithm = AES)
    var password: String? = null,

    //(varue = "绑定手机号")
    @Encrypt(algorithm = AES)
    var phone: String? = null,

    //(varue = "绑定地址")
    @Encrypt(algorithm = AES)
    var address: String? = null,

    //(varue = "备注")
    @Encrypt(algorithm = AES)
    var remark: String? = "未设置备注",

) : LitePalSupport(), Serializable {

    companion object {
        const val serialVersionUID = 1L
    }

}