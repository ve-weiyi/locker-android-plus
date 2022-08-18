package com.ve.module.lockit.respository.database.entity

import com.chad.library.adapter.base.entity.SectionEntity
import com.ve.lib.common.utils.date.TimeUtil
import com.ve.module.lockit.respository.database.vo.PrivacySimpleInfo
import org.litepal.annotation.Column
import org.litepal.annotation.Encrypt
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyFriend(
    var id: Long = 0,

    var nickname: String= "",

    var name: String= "",

    /**
     * 性别 女 0，男 1
     */
    @Encrypt(algorithm = AES)
    var sex:Int=1,

    @Encrypt(algorithm = AES)
    var birthday:String= TimeUtil.date,

    @Encrypt(algorithm = AES)
    var phone: String = "",

    @Encrypt(algorithm = AES)
    var email: String = "",

    @Encrypt(algorithm = AES)
    var qq: String = "",

    @Encrypt(algorithm = AES)
    var wechat: String = "",

    @Encrypt(algorithm = AES)
    var address: String = "",

    @Encrypt(algorithm = AES)
    var department: String = "",

    @Encrypt(algorithm = AES)
    var remark: String = "备注信息",

    @Column(ignore = true)
    var headerName: String = "",


    ) : LitePalSupport(), Serializable, SectionEntity {
    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

    companion object {
        const val serialVersionUID = 1L
    }

    fun toSimpleInfo(): PrivacySimpleInfo {
        return PrivacySimpleInfo(privacyName = name, privacyDesc = remark)
    }
}