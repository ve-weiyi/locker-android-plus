package com.ve.module.locker.model.db.entity

import com.ve.module.locker.LockerApplication
import org.litepal.annotation.Encrypt
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 *
 *
 *
 *
 *
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyPassDetails(

    var id: Long = 0,

    //(value = "登录账号")
    @Encrypt(algorithm = AES)
    var account: String,

    //(value = "登录密码")
    @Encrypt(algorithm = AES)
    var password: String,

    //(value = "链接")
    @Encrypt(algorithm = AES)
    var url: String? = null,

    //(value = "绑定手机号")
    @Encrypt(algorithm = AES)
    var phone: String? = null,

    //(value = "所属app包名")
    @Encrypt(algorithm = AES)
    var appPackageName: String = LockerApplication.context.packageName,

    //(value = "备注")
    @Encrypt(algorithm = AES)
    var remark: String? =  "未设置备注",

    ) : LitePalSupport(), Serializable {


    companion object {
        const val serialVersionUID = 1L
    }
}