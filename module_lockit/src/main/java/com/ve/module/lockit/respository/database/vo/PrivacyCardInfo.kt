package com.ve.module.lockit.respository.database.vo

import com.ve.module.lockit.respository.database.PrivacyEnum
import com.ve.module.lockit.respository.database.entity.*
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/16
 * @Description  current project lockit-android
 */
data class PrivacyCardInfo(



    val privacyDetails: PrivacyCard,

    /**
     * 文件夹id
     * 多对一,外键存id
     */

    val privacyFolder: PrivacyFolder = LitePal.findFirst(PrivacyFolder::class.java),
    /**
     * 隐私标签列表
     * 多对多,额外表存映射
     */
    var privacyTags: List<PrivacyTag>? = null,

    ) {

    @Synchronized
    fun save(): Boolean {
        /**
         * 先保存文件夹
         */
        val res1 = privacyFolder.saveOrUpdate("folderName=?", privacyFolder.folderName)

        /**
         * 再保存隐私信息
         */
        privacyDetails.privacyFolderId = privacyFolder.id
        val res2 = privacyDetails.saveOrUpdate("id=?", privacyDetails.id.toString())

        /**
         * 先删除这条隐私下的所有的标签，再添加
         */
        val res3 = LitePal.deleteAll(
            TagLink::class.java,
            "privacyId=? and type=?",
            "${privacyDetails.id}",
            "$type"
        )

        privacyTags?.forEach { privacyTag
            ->
            //保存标签
            privacyTag.saveOrUpdate("tagName=?", privacyTag.tagName)

            //添加标签和隐私的联系
            val tagLinks = TagLink(tagId = privacyTag.id, privacyId = privacyDetails.id, type = type)
            tagLinks.save()
        }

        return res1 && res2
    }

    /**
     * tag 和 folder 不需要修改
     * tagLinks 和 details info 需要删除
     */
    @Synchronized
    fun delete(): Int {
        val res1 = LitePal.deleteAll(
            TagLink::class.java,
            "privacyId=? and type=?",
            "${privacyDetails.id}",
            "$type"
        )
        val res2 = LitePal.delete(PrivacyCard::class.java, privacyDetails.id)
        return res1 + res2
    }


    companion object {

        val type: Int = PrivacyEnum.CARD.type

        fun getAll(): List<PrivacyCardInfo> {
            val privacyCardInfos = LitePal.findAll(PrivacyCard::class.java)

            val cards = mutableListOf<PrivacyCardInfo>()
            privacyCardInfos.forEach { card ->
                cards.add(
                    PrivacyCardInfo(
                        card,
                        card.getPrivacyFolder(),
                        card.getPrivacyTags()
                    )
                )
            }
            return cards
        }
    }
}