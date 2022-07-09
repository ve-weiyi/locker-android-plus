package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.model.db.entity.PrivacyCardInfo
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.model.db.entity.PrivacyTag
import com.ve.module.locker.model.db.vo.PrivacyCard
import com.ve.module.locker.model.http.model.ConditionVO
import com.ve.module.locker.utils.AndroidUtil
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerPrivacyCardViewModel : LockerViewModel() {

    var allAppInfo=MutableLiveData<MutableList<AndroidUtil.AppInfo>>()

    fun getAppInfo(){
        launch(
            block = {

            },
            local = {
//               allAppInfo.value=AndroidUtil.getAllAppInfo()
            }
        )
    }

    val privacyCardInfoList = MutableLiveData<MutableList<PrivacyCardInfo>>()
    fun getPrivacyCardList() {
        launch(
            block = {

            },
            local = {
                privacyCardInfoList.value =
                    LitePal.order("createTime").find(PrivacyCardInfo::class.java)
            }
        )
    }

    fun getPrivacyCardList(condition: ConditionVO) {
        launch(
            block = {

            },
            local = {
                val folders = LitePal.where("folderName like ?", condition.keyWords)
                    .find(PrivacyFolder::class.java)
                val tags =
                    LitePal.where("tagName like ?", condition.keyWords).find(PrivacyTag::class.java)

                LogUtil.msg(folders.toString())
                LogUtil.msg(tags.toString())

                val folderIds = folders.map { it.id }
                val tagIds = tags.map { it.id }

                val privacyCardInfos = LitePal.findAll(PrivacyCardInfo::class.java)
                val result = mutableListOf<PrivacyCardInfo>()
                privacyCardInfos.forEach { card ->

                    if (folderIds.contains(card.privacyFolderId)) {
                        if (!result.contains(card)) {
                            result.add(card)
                            LogUtil.msg(" add 1 $card")
                        }
                    }

                    val tagIdList = card.getPrivacyTags().map { it.id }

                    tagIds.forEach { id ->
                        if (tagIdList.contains(id)) {
                            if (!result.contains(card)) {
                                result.add(card)
                                LogUtil.msg(" add 2 $card")
                            }
                        }
                    }


                    if (card.privacyName.contains(condition.keyWords) || card.privacyDesc.contains(
                            condition.keyWords
                        )
                    ) {
                        if (!result.contains(card)) {
                            result.add(card)
                            LogUtil.msg(" add 3 $card")
                        }
                    }
                }
                LogUtil.msg(result.toString())
                privacyCardInfoList.value=result
            }
        )
    }

    val addPrivacyCardResult = MutableLiveData<String>()
    fun addPrivacyCard(privacyCard: PrivacyCard) {
        launch(
            block = {


            },
            local = {
                val result = privacyCard.save()
                LogUtil.msg(result.toString())
                if (result) {
                    addPrivacyCardResult.value = "保存成功！"
                } else {
                    addPrivacyCardResult.value = "保存失败！"
                }
            }
        )
    }

    val deletePrivacyCardListResult = MutableLiveData<Int>()
    fun deletePrivacyCardList(list: MutableList<PrivacyCardInfo>) {
        launch(
            block = {


            },
            local = {
                var result = 0
                list.forEach { privacyInfo ->
                    val card = PrivacyCard(
                        privacyInfo,
                        privacyInfo.getPrivacyDetails(),
                        privacyInfo.getPrivacyFolder(),
                        privacyInfo.getPrivacyTags()
                    )
                    result += card.delete()
                }
                deletePrivacyCardListResult.value = result
            }
        )
    }

    val deletePrivacyCardResult = MutableLiveData<Int>()
    fun deletePrivacyCard(privacyInfo: PrivacyCardInfo) {
        launch(
            block = {


            },
            local = {
                val card = PrivacyCard(
                    privacyInfo,
                    privacyInfo.getPrivacyDetails(),
                    privacyInfo.getPrivacyFolder(),
                    privacyInfo.getPrivacyTags()
                )

                val result = card.delete()
                deletePrivacyCardResult.value = result
            }
        )
    }

    val updatePrivacyCardResult = MutableLiveData<String>()
    fun updatePrivacyCard(privacyCard: PrivacyCard) {
        launch(
            block = {

            },
            local = {
                val result = privacyCard.save()
                if (result) {
                    updatePrivacyCardResult.value = "删除成功！" + result
                } else {
                    updatePrivacyCardResult.value = "删除失败！" + result
                }
            }
        )
    }

    val movePrivacyCardsResult = MutableLiveData<Int>()
    fun movePrivacyCard(privacyCardInfoList: List<PrivacyCardInfo>, folder: PrivacyFolder) {
        launch(
            block = {

            },
            local = {
                var result = 0
                privacyCardInfoList.forEach { privacyCardInfo ->
                    privacyCardInfo.privacyFolderId = folder.id
                    result += privacyCardInfo.update(privacyCardInfo.id)
                }
                movePrivacyCardsResult.value = result
            }
        )
    }
    /****************************/
}