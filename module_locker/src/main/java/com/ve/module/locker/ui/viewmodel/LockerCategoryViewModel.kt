package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.locker.model.db.entity.PrivacyCardInfo
import com.ve.module.locker.model.respository.PrivacyFolderRepository
import com.ve.module.locker.model.respository.PrivacyTagRepository
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.model.db.entity.PrivacyPassInfo
import com.ve.module.locker.model.db.entity.PrivacyTag
import com.ve.module.locker.model.db.vo.PrivacySimpleInfo
import com.ve.module.locker.model.http.model.ConditionVO
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerCategoryViewModel : LockerViewModel() {


//    val localTagList= MutableLiveData<MutableList<PrivacyTag>>()
//

    private val privacyTagRepository = PrivacyTagRepository
    val tagList = MutableLiveData<MutableList<PrivacyTag>>()

    fun getTagList() {
        launch(
            block = {
//                val result = privacyTagRepository.tagQueryList().data()
//                //没有网络被阻塞了
//                LitePal.saveAll(result)
//                tagList.value = result
            },
            local = {
                tagList.value = LitePal.findAll(PrivacyTag::class.java)
            }
        )
    }

    val tagAddMsg = MutableLiveData<String>()
    fun saveTag(privacyTag: PrivacyTag) {
        launch(
            block = {

            },
            local = {
                val result = privacyTag.save()
                if (result) {
                    tagAddMsg.value = "保存成功！"
                } else {
                    tagAddMsg.value = "保存失败！"
                }
            }
        )
    }

    val tagDeleteMsg = MutableLiveData<String>()
    fun tagDelete(privacyTagId: Int) {
        launch(
            block = {

            },
            local = {
                val result = LitePal.delete(PrivacyTag::class.java, privacyTagId.toLong())
                if (result > 0) {
                    tagDeleteMsg.value = "删除成功！" + result
                } else {
                    tagDeleteMsg.value = "删除失败！" + result
                }
            }
        )
    }

    val tagUpdateMsg = MutableLiveData<String>()
    fun tagUpdate(privacyTag: PrivacyTag) {
        launch(
            block = {

            },
            local = {
                val result = privacyTag.update(privacyTag.id.toLong())
                if (result > 0) {
                    tagUpdateMsg.value = "保存成功！" + result
                } else {
                    tagUpdateMsg.value = "保存失败！" + result
                }
            }
        )
    }

    /******************************* folder *******************************/

    private val privacyFolderRepository = PrivacyFolderRepository

    val folderList: MutableLiveData<MutableList<PrivacyFolder>> =
        MutableLiveData<MutableList<PrivacyFolder>>()

    fun folderList() {
        launch(
            block = {

            },
            local = {
                folderList.value = LitePal.findAll(PrivacyFolder::class.java)
            }
        )
    }

    val folderAddMsg = MutableLiveData<String>()
    fun folderAdd(privacyFolder: PrivacyFolder) {
        launch(
            block = {

            },
            local = {
                val result = privacyFolder.save()
                if (result) {
                    folderAddMsg.value = "操作成功！" + result
                } else {
                    folderAddMsg.value = "操作失败！" + result
                }
            }
        )
    }

    val folderDeleteMsg = MutableLiveData<String>()
    fun folderDelete(privacyFolderId: Int) {
        launch(
            block = {

            },
            local = {
                val result = LitePal.delete(PrivacyFolder::class.java, privacyFolderId.toLong())
                if (result > 0) {
                    folderDeleteMsg.value = "操作成功！" + result
                } else {
                    folderDeleteMsg.value = "操作失败！" + result
                }
            }
        )
    }

    val folderUpdateMsg = MutableLiveData<String>()
    fun folderUpdate(privacyFolder: PrivacyFolder) {
        launch(
            block = {

            },
            local = {
                val result = privacyFolder.update(privacyFolder.id.toLong())
                if (result > 0) {
                    folderUpdateMsg.value = "操作成功！" + result
                } else {
                    folderUpdateMsg.value = "操作失败！" + result
                }
            }
        )
    }

    val resultGroupList = MutableLiveData<MutableList<Pair<String, MutableList<PrivacySimpleInfo>>>>()
    fun getGroupList(conditionVO: ConditionVO) {
        launch(
            block = {

            },
            local = {
                val result = mutableListOf<Pair<String, MutableList<PrivacySimpleInfo>>>()

                val passGroup = mutableListOf<PrivacySimpleInfo>()
                val cardGroup = mutableListOf<PrivacySimpleInfo>()

                if (conditionVO.folderId != null) {
                    val passList =
                        LitePal.where("privacyFolderId = ?", conditionVO.folderId.toString())
                            .find(PrivacyPassInfo::class.java)
                    val cardList =
                        LitePal.where("privacyFolderId = ?", conditionVO.folderId.toString())
                            .find(PrivacyCardInfo::class.java)

                    passList.forEach { pass ->
                        passGroup.add(pass.toSimpleInfo())
                    }
                    cardList.forEach { card ->
                        cardGroup.add(card.toSimpleInfo())
                    }
                }
                if (conditionVO.tagId != null) {
                    val passList =
                        LitePal.where("privacyTagId = ?", conditionVO.tagId.toString())
                            .find(PrivacyPassInfo::class.java)
                    val cardList =
                        LitePal.where("privacyTagId = ?", conditionVO.tagId.toString())
                            .find(PrivacyCardInfo::class.java)

                    passList.forEach { pass ->
                        passGroup.add(pass.toSimpleInfo())
                    }
                    cardList.forEach { card ->
                        cardGroup.add(card.toSimpleInfo())
                    }

                }

                result.add(Pair("密码",passGroup))
                result.add(Pair("卡片",cardGroup))
                resultGroupList.value=result
            }
        )
    }
}