package com.ve.module.locker.model.db.dao

import com.ve.module.locker.model.db.entity.*
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project locker-android
 */
object PrivacyInfoDao {
//
//    suspend fun savePrivacyInfo(privacyInfo: PrivacyInfoCard): Boolean {
//        LitePal.beginTransaction()
//        /**
//         * 对tag and privacy 的修改单独进行
//         */
//        val tagList = privacyInfo.privacyTags
//        saveTagAndCard(tagList, privacyId = privacyInfo.id)
//        val folder = privacyInfo.privacyFolder
//        val details = privacyInfo.privacyDetails
//        privacyInfo.privacyFolderId = folder?.id
//        privacyInfo.privacyDetailsId = details?.id
//        val result = privacyInfo.save()
//        LitePal.endTransaction()
//        return result
//    }
//
//    suspend fun savePrivacyInfo(privacyInfo: PrivacyInfoPass): Boolean {
//        LitePal.beginTransaction()
//        val tagList = privacyInfo.privacyTags
//        saveTagAndPass(tagList, privacyId = privacyInfo.id)
//        val folder = privacyInfo.privacyFolder
//        val details = privacyInfo.privacyDetails
//        privacyInfo.privacyFolderId = folder?.id
//        privacyInfo.privacyDetailsId = details?.id
//        val result = privacyInfo.save()
//        LitePal.endTransaction()
//        return result
//    }

    suspend fun saveTagAndCard(tagList: List<PrivacyTag>?, privacyId: Long) {
        if(tagList===null)
            return
        LitePal.deleteAll(TagAndCard::class.java,"privacyId=${privacyId}")
        tagList.forEach { tag ->
            // val res= LitePal.where("privacyId=${privacyId}").find(TagAndCard::class.java)
            TagAndCard(tagId = tag.id, privacyId = privacyId).save()
        }
    }

    suspend fun saveTagAndPass(tagList: List<PrivacyTag>?, privacyId: Long) {
        if(tagList===null)
            return
        LitePal.deleteAll(TagAndPass::class.java,"privacyId=${privacyId}")
        tagList.forEach { tag ->
            // val res= LitePal.where("privacyId=${privacyId}").find(TagAndCard::class.java)
            TagAndPass(tagId = tag.id, privacyId = privacyId).save()
        }
    }
}