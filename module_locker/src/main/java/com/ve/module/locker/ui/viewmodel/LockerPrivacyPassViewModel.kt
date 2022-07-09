package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.model.db.entity.*
import com.ve.module.locker.model.db.vo.PrivacyPass
import com.ve.module.locker.model.http.model.ConditionVO
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerPrivacyPassViewModel : LockerViewModel() {

    val privacyPassInfoList = MutableLiveData<MutableList<PrivacyPassInfo>>()
    fun getPrivacyPassList() {
        launch(
            block = {

            },
            local = {
                privacyPassInfoList.value =
                    LitePal.order("createTime").find(PrivacyPassInfo::class.java)
            }
        )
    }

    fun getPrivacyPassList(condition: ConditionVO) {
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

                val privacyPassInfos = LitePal.findAll(PrivacyPassInfo::class.java)
                val result = mutableListOf<PrivacyPassInfo>()
                privacyPassInfos.forEach { pass ->

                    if (folderIds.contains(pass.privacyFolderId)) {
                        if (!result.contains(pass)) {
                            result.add(pass)
                            LogUtil.msg(" add 1 $pass")
                        }
                    }

                    val tagIdList = pass.getPrivacyTags().map { it.id }

                    tagIds.forEach { id ->
                        if (tagIdList.contains(id)) {
                            if (!result.contains(pass)) {
                                result.add(pass)
                                LogUtil.msg(" add 2 $pass")
                            }
                        }
                    }


                    if (pass.privacyName.contains(condition.keyWords) || pass.privacyDesc.contains(
                            condition.keyWords
                        )
                    ) {
                        if (!result.contains(pass)) {
                            result.add(pass)
                            LogUtil.msg(" add 3 $pass")
                        }
                    }
                }
                LogUtil.msg(result.toString())
                privacyPassInfoList.value=result
            }
        )
    }

    val addPrivacyPassResult = MutableLiveData<String>()
    fun addPrivacyPass(privacyPass: PrivacyPass) {
        launch(
            block = {


            },
            local = {
                val result = privacyPass.save()
                LogUtil.msg(result.toString())
                if (result) {
                    addPrivacyPassResult.value = "保存成功！"
                } else {
                    addPrivacyPassResult.value = "保存失败！"
                }
            }
        )
    }

    val deletePrivacyPassListResult = MutableLiveData<Int>()
    fun deletePrivacyPassList(list: MutableList<PrivacyPassInfo>) {
        launch(
            block = {


            },
            local = {
                var result = 0
                list.forEach { privacyInfo ->
                    val pass = PrivacyPass(
                        privacyInfo,
                        privacyInfo.getPrivacyDetails(),
                        privacyInfo.getPrivacyFolder(),
                        privacyInfo.getPrivacyTags()
                    )
                    result += pass.delete()
                }
                deletePrivacyPassListResult.value = result
            }
        )
    }

    val deletePrivacyPassResult = MutableLiveData<Int>()
    fun deletePrivacyPass(privacyInfo: PrivacyPassInfo) {
        launch(
            block = {


            },
            local = {
                val pass = PrivacyPass(
                    privacyInfo,
                    privacyInfo.getPrivacyDetails(),
                    privacyInfo.getPrivacyFolder(),
                    privacyInfo.getPrivacyTags()
                )

                val result = pass.delete()
                deletePrivacyPassResult.value = result
            }
        )
    }

    val updatePrivacyPassResult = MutableLiveData<String>()
    fun updatePrivacyPass(privacyPass: PrivacyPass) {
        launch(
            block = {

            },
            local = {
                val result = privacyPass.save()
                if (result) {
                    updatePrivacyPassResult.value = "操作成功！" + result
                } else {
                    updatePrivacyPassResult.value = "操作失败！" + result
                }
            }
        )
    }

    val movePrivacyPassResult = MutableLiveData<Int>()
    fun movePrivacyPass(privacyPassInfoList: List<PrivacyPassInfo>, folder: PrivacyFolder) {
        launch(
            block = {

            },
            local = {
                var result = 0
                privacyPassInfoList.forEach { privacyPassInfo ->
                    privacyPassInfo.privacyFolderId = folder.id
                    result += privacyPassInfo.update(privacyPassInfo.id)
                }
                movePrivacyPassResult.value = result
            }
        )
    }
}