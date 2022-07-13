package com.ve.module.lockit.respository.database.entity

import com.chad.library.adapter.base.entity.SectionEntity
import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.TimeUtil
import com.ve.module.lockit.respository.database.PrivacyEnum
import com.ve.module.lockit.respository.database.vo.PrivacySimpleInfo
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.annotation.Encrypt
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * 卡片证件类
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyCard(
    /**
     * id列自增，不可以设置
     */
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    //(value = "签发机构")
    var name: String = "中国银行",

    //(value = "用户名")
    var account: String = "unknown",

    @Encrypt(algorithm = AES)
    var password: String = "",

    //(varue = "拥有人")
    var owner: String = "unknown",

    //(value = "备注")
    var remark: String = "未设置备注",

    //(varue = "更新时间")
    var updateTime: String = TimeUtil.dateAndTime,

    //(varue = "文件夹id")
    @Column(index = true)
    var privacyFolderId: Long = 1,

    @Column(ignore = true)
    var headerName: String = "",
) : LitePalSupport(), SectionEntity, Serializable {


    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

    /**
     * 文件夹id
     * 多对一,外键存id
     */
    fun getPrivacyFolder(): PrivacyFolder {
        return LitePal.find(PrivacyFolder::class.java, privacyFolderId)
    }

    /**
     * 隐私标签列表
     * 多对多,额外表存映射
     */
    fun getPrivacyTags(): MutableList<PrivacyTag> {
        val tagLinks = LitePal.where("privacyId=? and type=?", "$id", PrivacyEnum.CARD.type.toString())
            .find(TagLink::class.java)

        val tags = mutableListOf<PrivacyTag>()
        tagLinks.forEach { it ->
            tags.add(LitePal.find(PrivacyTag::class.java, it.tagId))
        }
        return tags
    }

    /**
     * 删除操作，先删除tagLinks
     */
    override fun delete(): Int {
        LitePal.deleteAll(TagLink::class.java,"privacyId=? and type=?", "$id",PrivacyEnum.CARD.type.toString())
        return super.delete()
    }

    fun toSimpleInfo(): PrivacySimpleInfo {
        return PrivacySimpleInfo(privacyName = name, CommonUtil.randomColor().toString(),remark, updateTime)
    }

    companion object {
        const val serialVersionUID = 1L
    }

}