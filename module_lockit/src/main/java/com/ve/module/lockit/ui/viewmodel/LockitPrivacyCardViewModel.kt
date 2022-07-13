package com.ve.module.lockit.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.lockit.respository.database.entity.*
import com.ve.module.lockit.respository.database.vo.PrivacyCardInfo
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockitPrivacyCardViewModel : LockitViewModel() {


    val getPrivacyCardListResult = MutableLiveData<MutableList<PrivacyCard>>()
    fun getPrivacyCardList() {
        launch(
            block = {

            },
            then = {
                getPrivacyCardListResult.value =
                    LitePal.order("name").find(PrivacyCard::class.java)
            }
        )
    }

    fun searchPrivacyCardList(keyWords: String) {
        launch(
            block = {

            },
            then = {
                //查找 名称、账号、备注符合的记录
                val privacyList = LitePal.where(
                    "name like ? or account like ? or owner like ? or remark like ?",
                    "%$keyWords%",
                    "%$keyWords%",
                    "%$keyWords%",
                    "%$keyWords%",
                ).find(PrivacyCard::class.java)

                getPrivacyCardListResult.value = privacyList
            }
        )
    }

    val deletePrivacyCardListResult = MutableLiveData<Int>()
    fun deletePrivacyCardList(privacyList: MutableList<PrivacyCard>) {
        launch(
            block = {

            },
            then = {
                var result = 0
                privacyList.forEach { privacy ->
                    if (privacy.isSaved) {
                        result += privacy.delete()
                    }
                }
                deletePrivacyCardListResult.value = result
            }
        )
    }

    val movePrivacyCardListResult = MutableLiveData<Int>()
    fun movePrivacyCardList(privacyList: List<PrivacyCard>, folder: PrivacyFolder) {
        launch(
            block = {

            },
            then = {
                var result = 0
                privacyList.forEach { privacy ->
                    privacy.privacyFolderId = folder.id
                    if (privacy.isSaved) {
                        privacy.saveOrUpdate()
                        result++
                    }
                }
                movePrivacyCardListResult.value = result
            }
        )
    }

    val addPrivacyCardResult = MutableLiveData<Boolean>()
    fun addPrivacyCard(
        privacy: PrivacyCard,
        folder: PrivacyFolder,
        tagList: MutableList<PrivacyTag>?
    ) {
        launch(
            block = {


            },
            then = {
                val pass = PrivacyCardInfo(privacy, folder, tagList)
                val result = pass.save()
                addPrivacyCardResult.value = result
            }
        )
    }

    val deletePrivacyCardResult = MutableLiveData<Int>()
    fun deletePrivacyCard(privacy: PrivacyCard) {
        launch(
            block = {


            },
            then = {
                val result = privacy.delete()
                deletePrivacyCardListResult.value = result
            }
        )
    }
}