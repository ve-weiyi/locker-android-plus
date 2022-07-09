package com.ve.module.locker.model.db.entity

import com.chad.library.adapter.base.entity.SectionEntity
import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.DateTimeUtil
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
data class PrivacyCardInfo(
    /**
     * id列自增，不可以设置
     */
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    //(varue = "隐私名", notes = "标签的备注名", example = "XX的QQ邮箱账号", position = 4)
    public var privacyName: String = "",

    //( varue = "隐私图标", notes = "标签的覆盖图标", example = "https://ve77.cn/blog/favicon.ico", position = 5 )
    public var privacyCover: String = CommonUtil.randomColor().toString(),

    //(varue = "隐私描述", notes = "标签描述", example = "床前明月光", position = 6)
    public var privacyDesc: String = "",

    //(varue = "文件夹id")
    @Column(index = true)
    public var privacyFolderId: Long = 1,

    //(varue = "隐私id")
    @Column(index = true)
    public var privacyDetailsId: Long = 1,

    //(varue = "创建时间", notes = "标签创建时间,不用填", position = 7)
    public var createTime: String = DateTimeUtil.dateAndTime,

    //(varue = "更新时间", notes = "标签更新时间,不用填", position = 8)
    public var updateTime: String = DateTimeUtil.dateAndTime,

    @Column(ignore = true)
    var headerName: String = "",

//    /**
//     * 文件夹id
//     * 多对一,外键存id
//     */
//    @Column(ignore = true)
//    public var privacyFolder: PrivacyFolder? = null,
//
//    /**
//     * 隐私标签列表
//     * 多对多,额外表存映射
//     */
//    @Column(ignore = true)
//    public var privacyTags: List<PrivacyTag>? = null,
//
//    /**
//     * 一对一，主键关联
//     */
//    @Column(ignore = true)
//    public var privacyDetails: DetailsCard?=null,
) : LitePalSupport(), SectionEntity, Serializable {

    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

    companion object {
        private const val serialVersionUID = 1L
    }

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
        val tagAndCards = LitePal.where("privacyId=?", "$id").find(TagAndCard::class.java)
        val tags = mutableListOf<PrivacyTag>()
        tagAndCards.forEach { it ->
            tags.add(LitePal.find(PrivacyTag::class.java, it.tagId))
        }
        return tags
    }

    /**
     * 一对一，主键关联
     */
    public fun getPrivacyDetails(): PrivacyCardDetails {
        return LitePal.find(PrivacyCardDetails::class.java, privacyDetailsId)
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
            LitePal.deleteAll(TagAndCard::class.java, "privacyId=?", "$id")
            tags.forEach { tag ->
                TagAndCard(tagId = tag.id, privacyId = id).save()
            }
        }
        return true
    }

    /**
     * 一对一，主键关联
     */
    public fun setPrivacyDetails(detailsCard: PrivacyCardDetails): Boolean {
        if (!detailsCard.isSaved) {
            //找不到数据，添加
            detailsCard.id = this.id
            detailsCard.save()
            privacyDetailsId = LitePal.findLast(PrivacyCardDetails::class.java).id
            return false
        } else {

            privacyDetailsId = detailsCard.id
            return true
        }
    }

    /**
     * tag 和 folder 不需要修改
     * tagAndCard 和 details info 需要删除
     */
    @Synchronized
    override fun delete(): Int {
        val res1 = LitePal.deleteAll(TagAndCard::class.java, "privacyId=?", "${id}")
        val res2 = LitePal.delete(PrivacyCardDetails::class.java,getPrivacyDetails().id)
        val res3 = LitePal.delete(PrivacyCardInfo::class.java,id)
        return res3
    }

    fun toSimpleInfo():PrivacySimpleInfo{
        return PrivacySimpleInfo(privacyName, privacyCover, privacyDesc, createTime, updateTime)
    }
}