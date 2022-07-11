package com.ve.module.locker.model.db.entity

import com.chad.library.adapter.base.entity.SectionEntity
import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.AppContextUtils
import com.ve.lib.common.vutils.DateTimeUtil
import com.ve.module.locker.LockerApplication
import com.ve.module.locker.model.db.PrivacyEnum
import com.ve.module.locker.model.db.vo.PrivacySimpleInfo
import com.ve.module.locker.utils.AndroidUtil
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.annotation.Encrypt
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * 网站、应用密码类
 *
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyPass(
    /**
     * id列自增，不可以设置
     */
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    //(value = "签发机构")
    var name: String = "平台",

    //(value = "用户名")
    var account: String ="",

    //(value = "登录密码")
    @Encrypt(algorithm = AES)
    var password: String ="",

    //(value = "网站链接")
    var url: String = "#",

    //(value = "所属app包名")
    var appPackageName: String = LockerApplication.context.packageName,

    //(value = "备注")
    var remark: String = "未设置备注",

    //(varue = "更新时间")
    var updateTime: String = DateTimeUtil.dateAndTime,

    /**
     * 文件夹id
     * 多对一,外键存id
     */
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
        val tagLinks = LitePal.where("privacyId=? and type=?", "$id",PrivacyEnum.PASS.type.toString()).find(TagLink::class.java)

        val tags = mutableListOf<PrivacyTag>()
        tagLinks.forEach { it ->
            tags.add(LitePal.find(PrivacyTag::class.java, it.tagId))
        }
        return tags
    }

    fun getAppInfo(): AndroidUtil.AppInfo? {
        return AndroidUtil.getAppInfo(AppContextUtils.mContext,appPackageName)
    }
    /**
     * 删除操作，先删除tagLinks
     */
    override fun delete(): Int {
        LitePal.deleteAll(TagLink::class.java,"privacyId=? and type=?", "$id",PrivacyEnum.PASS.type.toString())
        return super.delete()
    }

    fun toSimpleInfo(): PrivacySimpleInfo {
        return PrivacySimpleInfo(privacyName = name, CommonUtil.randomColor().toString(),remark, updateTime)
    }

    companion object {
        const val serialVersionUID = 1L
    }
}