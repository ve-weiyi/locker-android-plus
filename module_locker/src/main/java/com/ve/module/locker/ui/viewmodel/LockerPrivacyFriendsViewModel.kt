package com.ve.module.locker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.locker.respository.database.entity.PrivacyFriend
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/7
 */
class LockerPrivacyFriendsViewModel : LockerViewModel() {

    val privacyFriendsInfoList = MutableLiveData<MutableList<PrivacyFriend>>()
    fun getPrivacyFriendsList(keyWords: String?=null) {
        launch(
            block = {

            },
            local = {
                if (keyWords== null) {
                    privacyFriendsInfoList.value = LitePal.findAll(PrivacyFriend::class.java)
                } else {
                    privacyFriendsInfoList.value = LitePal.where(
                        "name like ? or nickname like ?",
                        "%$keyWords%",
                        "%$keyWords%",
                    ).find(PrivacyFriend::class.java)
                }
            }
        )
    }

    val saveOrUpdateResult = MutableLiveData<Boolean>()
    fun saveOrUpdatePrivacyFriends(privacyFriends: PrivacyFriend) {
        launch(
            block = {


            },
            local = {
                val result = privacyFriends.saveOrUpdate("id= ?", privacyFriends.id.toString())
                saveOrUpdateResult .value = result
            }
        )
    }

    val deletePrivacyFriendsResult = MutableLiveData<Int>()
    fun deletePrivacyFriends(privacyInfo: PrivacyFriend) {
        launch(
            block = {


            },
            local = {
                val result = privacyInfo.delete()
                deletePrivacyFriendsResult.value = result
            }
        )
    }

    fun deletePrivacyFriends(privacyInfos: MutableList<PrivacyFriend>) {
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