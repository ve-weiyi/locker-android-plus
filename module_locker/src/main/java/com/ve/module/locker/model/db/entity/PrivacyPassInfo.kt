package com.ve.module.locker.model.db.entity

import com.chad.library.adapter.base.entity.SectionEntity
import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.DateTimeUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.model.db.vo.PrivacySimpleInfo
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project locker-android
 */
data class PrivacyPassInfo(
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    //(varue = "隐私名", notes = "标签的备注名", example = "XX的QQ邮箱账号", position = 4)
    public var privacyName: String = "",

    //( varue = "隐私图标", notes = "标签的覆盖图标", example = "https://ve77.cn/blog/favicon.ico", position = 5 )
    public var privacyCover: String = CommonUtil.randomColor().toString(),

    //(varue = "隐私描述", notes = "标签描述", example = "床前明月光", position = 6)
    public var privacyDesc: String = "",

    //(varue = "创建时间", notes = "标签创建时间,不用填", position = 7)
    public var createTime: String = DateTimeUtil.dateAndTime,

    //(varue = "更新时间", notes = "标签更新时间,不用填", position = 8)
    public var updateTime: String = DateTimeUtil.dateAndTime,
    //(varue = "文件夹id")
    @Column(index = true)
    public var privacyFolderId: Long =1 ,

    //(varue = "隐私id")
    @Column(index = true)
    public var privacyDetailsId: Long =1,


    @Column(ignore = true)
    var headerName: String = "",

//    /**
//     * 文件夹id
//     * 多对一,外键存id
//     */
//    @Column(ignore = true)
//    public val privacyFolder: PrivacyFolder? = null,
//
//    /**
//     * 隐私标签列表
//     * 多对多,额外表存映射
//     */
//    @Column(ignore = true)
//    public var privacyTags: List<PrivacyTag>? = null,
//
//    /**
//     * 一对一，主键关联. details 表的id即是info的id
//     */
//    @Column(ignore = true)
//    public val privacyDetails: DetailsPass?=null,
) : LitePalSupport(), Serializable,SectionEntity {

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
        val tagAndPasss = LitePal.where("privacyId=?", "$id").find(TagAndPass::class.java)
        val tags = mutableListOf<PrivacyTag>()
        tagAndPasss.forEach { it ->
            tags.add(LitePal.find(PrivacyTag::class.java, it.tagId))
        }
        return tags
    }

    /**
     * 一对一，主键关联
     */
    public fun getPrivacyDetails(): PrivacyPassDetails {
        LogUtil.msg(LitePal.find(PrivacyPassDetails::class.java, privacyDetailsId).toString())
        return LitePal.find(PrivacyPassDetails::class.java, privacyDetailsId)
    }


    /**
     * 文件夹id
     * 多对一,外键存id
     */
    fun setPrivacyFolder(privacyFolder: PrivacyFolder?): Boolean {
        privacyFolder?.apply {
            if (!privacyFolder.isSaved) {
                //找不到数据，添加
                privacyFolder.save()
                privacyFolderId = LitePal.findLast(PrivacyFolder::class.java).id
                return false
            } else {
                privacyFolderId = privacyFolder.id
                return true
            }
        }
        //设置默认文件夹
        val first = LitePal.findFirst(PrivacyFolder::class.java)
        privacyFolderId = first.id
        return true
    }

    /**
     * 隐私标签列表
     * 多对多,额外表存映射
     */
    fun setPrivacyTags(tags: List<PrivacyTag>?): Boolean {
        tags?.apply {
            LitePal.deleteAll(TagAndPass::class.java, "privacyId=?", "$id")
            tags.forEach { tag ->
                TagAndPass(tagId = tag.id, privacyId = id).save()
            }
        }
        return true
    }

    /**
     * 一对一，主键关联
     */
    public fun setPrivacyDetails(detailsPass: PrivacyPassDetails): Boolean {
        if (!detailsPass.isSaved) {
            //找不到数据，添加
            detailsPass.id = this.id
            detailsPass.save()
            privacyDetailsId = LitePal.findLast(PrivacyPassDetails::class.java).id
            return false
        } else {

            privacyDetailsId = detailsPass.id
            return true
        }
    }
    
    companion object {
        private const val serialVersionUID = 1L
    }

    fun toSimpleInfo(): PrivacySimpleInfo {
        return PrivacySimpleInfo(privacyName, privacyCover, privacyDesc, createTime, updateTime)
    }
}