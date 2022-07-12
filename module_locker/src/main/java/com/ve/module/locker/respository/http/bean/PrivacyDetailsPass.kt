package com.ve.module.locker.respository.http.bean

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
data class PrivacyDetailsPass(

    var id: Int? = null,

    //(value = "登录账号")
    var account: String? = null,

    //(value = "登录密码")
    var password: String? = null,

    //(value = "网站链接")
    var url: String? = null,

    //(value = "所属app")
    var appName: String? = null,

    //(value = "绑定手机号")
    var phone: String? = null,

    //(value = "备注")
    var remark: String? = null,

    //(value = "是否加密")
    var enableEncrypt: Int? = null,

    ) : LitePalSupport(),Serializable {
    

    companion object {
        const val serialVersionUID = 1L
    }
}