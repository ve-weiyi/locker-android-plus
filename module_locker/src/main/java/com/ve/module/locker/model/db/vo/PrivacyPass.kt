package com.ve.module.locker.model.db.vo

import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.model.db.entity.*
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/16
 * @Description  current project locker-android
 */
data class PrivacyPass(

    public val privacyInfo: PrivacyPassInfo,

    /**
     * 一对一，主键关联
     */

    public val privacyDetails: PrivacyPassDetails,

    /**
     * 文件夹id
     * 多对一,外键存id
     */

    public val privacyFolder: PrivacyFolder = LitePal.findFirst(PrivacyFolder::class.java),
    /**
     * 隐私标签列表
     * 多对多,额外表存映射
     */
    public var privacyTags: List<PrivacyTag>? = null,
) {

    @Synchronized
    fun save(): Boolean {
        val res1 = privacyFolder.saveOrUpdate("folderName=?",privacyFolder.folderName)
        val res2 = privacyDetails.saveOrUpdate("id=?",privacyDetails.id.toString())

        LogUtil.msg(privacyDetails.toString());
        privacyInfo.privacyFolderId = privacyFolder.id
        privacyInfo.privacyDetailsId = privacyDetails.id

        val res3 = privacyInfo.saveOrUpdate("id=?",privacyInfo.id.toString())

        //先删除这条隐私下的所有的标签，再添加
        val res4 = LitePal.deleteAll(TagAndPass::class.java, "privacyId=?", "${privacyInfo.id}")
        privacyTags?.forEach { privacyTag
            ->
            privacyTag.saveOrUpdate("tagName=?",privacyTag.tagName)
            val tagAndPass = TagAndPass(tagId = privacyTag.id, privacyId = privacyInfo.id)
            tagAndPass.save()
        }

        return res1 && res2 && res3
    }

    /**
     * tag 和 folder 不需要修改
     * tagAndPass 和 details info 需要删除
     */
    @Synchronized
    fun delete(): Int {
        val res1 = LitePal.deleteAll(TagAndPass::class.java, "privacyId=?", "${privacyInfo.id}")
        val res2 = LitePal.delete(PrivacyPassDetails::class.java,privacyDetails.id)
        val res3 = LitePal.delete(PrivacyPassInfo::class.java,privacyInfo.id)
        return res3
    }

    companion object{
        fun getAll():List<PrivacyPass>{
            val privacyPassInfos=LitePal.findAll(PrivacyPassInfo::class.java)
            val passs= mutableListOf<PrivacyPass>()
            privacyPassInfos.forEach {
                    pass->
                passs.add(
                    PrivacyPass(pass,pass.getPrivacyDetails(),pass.getPrivacyFolder(),pass.getPrivacyTags())
                )
            }
            return passs
        }
    }
}