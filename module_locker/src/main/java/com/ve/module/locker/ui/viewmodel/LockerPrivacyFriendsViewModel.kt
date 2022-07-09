package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.locker.model.db.entity.PrivacyFriendsInfo
import com.ve.module.locker.model.http.model.ConditionVO
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerPrivacyFriendsViewModel : LockerViewModel() {

    val privacyFriendsInfoList = MutableLiveData<MutableList<PrivacyFriendsInfo>>()
    fun getPrivacyFriendsList(conditionVO: ConditionVO? = null) {
        launch(
            block = {

            },
            local = {
                if (conditionVO == null) {
                    privacyFriendsInfoList.value = LitePal.findAll(PrivacyFriendsInfo::class.java)
                } else {
                    privacyFriendsInfoList.value = LitePal.where(
                        "name like ? or nickname like ?",
                        conditionVO.keyWords,
                        conditionVO.keyWords
                    ).find(PrivacyFriendsInfo::class.java)
                }

            }
        )
    }

    val reslutSaveOrUpdate = MutableLiveData<Boolean>()
    fun saveOrUpdatePrivacyFriends(privacyFriends: PrivacyFriendsInfo) {
        launch(
            block = {


            },
            local = {
                val result = privacyFriends.saveOrUpdate("id= ?", privacyFriends.id.toString())
                reslutSaveOrUpdate.value = result
            }
        )
    }

    val deletePrivacyFriendsResult = MutableLiveData<Int>()
    fun deletePrivacyFriends(privacyInfo: PrivacyFriendsInfo) {
        launch(
            block = {


            },
            local = {
                val result = privacyInfo.delete()
                deletePrivacyFriendsResult.value = result
            }
        )
    }

    fun deletePrivacyFriends(privacyInfos: MutableList<PrivacyFriendsInfo>) {
        launch(
            block = {


            },
            local = {
                var result: Int = 0
                privacyInfos.forEach { privacyInfo ->
                    result += privacyInfo.delete()
                }
                deletePrivacyFriendsResult.value = result
            }
        )
    }

}