package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.model.db.entity.*
import com.ve.module.locker.model.db.vo.PrivacyPassInfo
import com.ve.module.locker.model.http.model.ConditionVO
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerPrivacyPassViewModel : LockerViewModel() {


    val getPrivacyPassListResult = MutableLiveData<MutableList<PrivacyPass>>()
    fun getPrivacyPassList() {
        launch(
            block = {

            },
            local = {
                getPrivacyPassListResult.value =
                    LitePal.order("name").find(PrivacyPass::class.java)
            }
        )
    }


//    val searchPrivacyPassListResult = MutableLiveData<MutableList<PrivacyPass>>()
    fun searchPrivacyPassList(keyWords: String) {
        launch(
            block = {

            },
            local = {
                //查找 名称、账号、备注符合的记录,由于被加密了，所以存储是密文
                val privacyList = LitePal.where(
                    "name like ? or account like ? or url like ? or remark like ? ",
                    "%$keyWords%",
                    "%$keyWords%",
                    "%$keyWords%",
                    "%$keyWords%",
                ).find(PrivacyPass::class.java)
                LogUtil.msg("key = $keyWords")
                LogUtil.msg(privacyList)
                getPrivacyPassListResult.value=privacyList

//                //查询文件夹和标签
//                val folders = LitePal.where("folderName like ?", keyWords)
//                    .find(PrivacyFolder::class.java)
//                val tags = LitePal.where("tagName like ?", keyWords)
//                    .find(PrivacyTag::class.java)
//
//                LogUtil.msg(privacyList.toString())
//                LogUtil.msg(folders.toString())
//                LogUtil.msg(tags.toString())
//
//                //得到符合条件的标签id
//                val folderIds = folders.map { it.id }
//                val tagIds = tags.map { it.id }
            }
        )
    }

    val deletePrivacyPassListResult = MutableLiveData<Int>()
    fun deletePrivacyPassList(privacyList: MutableList<PrivacyPass>) {
        launch(
            block = {

            },
            local = {
                var result = 0
                privacyList.forEach { privacy ->
                    if (privacy.isSaved) {
                        result += privacy.delete()
                    }
                }
                deletePrivacyPassListResult.value = result
            }
        )
    }

    val movePrivacyPassListResult = MutableLiveData<Int>()
    fun movePrivacyPassList(privacyList: List<PrivacyPass>, folder: PrivacyFolder) {
        launch(
            block = {

            },
            local = {
                var result = 0
                privacyList.forEach { privacy ->
                    privacy.privacyFolderId = folder.id
                    if (privacy.isSaved) {
                        privacy.saveOrUpdate()
                        result++
                    }
                }
                movePrivacyPassListResult.value = result
            }
        )
    }

    val addPrivacyPassResult = MutableLiveData<Boolean>()
    fun addPrivacyPass(
        privacy: PrivacyPass,
        folder: PrivacyFolder,
        tagList: MutableList<PrivacyTag>?
    ) {
        launch(
            block = {


            },
            local = {
                val pass = PrivacyPassInfo(privacy, folder, tagList)
                val result = pass.save()
                addPrivacyPassResult.value = result
            }
        )
    }

    val deletePrivacyPassResult = MutableLiveData<Int>()
    fun deletePrivacyPass(privacy: PrivacyPass) {
        launch(
            block = {


            },
            local = {
                val result = privacy.delete()
                deletePrivacyPassListResult.value = result
            }
        )
    }
}