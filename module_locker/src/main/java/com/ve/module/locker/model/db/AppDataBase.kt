package com.ve.module.locker.model.db

import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.common.config.SettingConstant
import com.ve.module.locker.model.db.dao.PrivacyInfoDao
import com.ve.module.locker.model.db.entity.*
import com.ve.module.locker.model.db.vo.PrivacyCard
import com.ve.module.locker.model.db.vo.PrivacyPass
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project locker-android
 */
object AppDataBase {

    val dao = PrivacyInfoDao
    fun initDataBase() {
        LitePal.aesKey("1234567890123456");
//        LogUtil.msg(SpUtil.setValue(SettingConstant.SP_KEY_DATABASE_INIT,true))
//        LogUtil.msg(SpUtil.getBoolean(SettingConstant.SP_KEY_DATABASE_INIT))

        if(SpUtil.getValue(SettingConstant.SP_KEY_DATABASE_INIT,true)){
            LogUtil.msg("data already init");
            return
        }

        LitePal.deleteDatabase("locker")

        val privacyPassList = mutableListOf<PrivacyPass>(
            PrivacyPass(
                PrivacyPassInfo(privacyName = "qq账号", privacyDesc = "我的的QQ账号。"),
                PrivacyPassDetails(
                    account = "791422171", password = "123456789",
                    appPackageName = "com.tencent.mobileqq", url = "https://im.qq.com/index",
                    phone = "15623356029", remark = "开发者QQ账号，遇到问题可以反馈"
                ),
                PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "QQ账号"))
            ),
            PrivacyPass(
                PrivacyPassInfo(privacyName = "邮箱账号", privacyDesc = "我的邮箱账号。"),
                PrivacyPassDetails(
                    account = "791422171@qq.com",
                    password = "123456789",
                    appPackageName = "com.tencent.androidqqmail"
                ),
                PrivacyFolder(folderName = "社交", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "邮箱账号"))
            ),
            PrivacyPass(
                PrivacyPassInfo(
                    privacyName = "微信账号。",
                    privacyDesc = "我的微信账号。"
                ),
                PrivacyPassDetails(
                    account = "wy791422171",
                    url = "https://github.com/7914-ve/locker-android",
                    password = "1234567",
                    appPackageName = "com.tencent.mm"
                ),
                PrivacyFolder(folderName = "教育", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "源码"))
            ),
            PrivacyPass(
                PrivacyPassInfo(
                    privacyName = "博客地址",
                    privacyDesc = "我的博客地址。"
                ),
                PrivacyPassDetails(
                    account = "https://ve77.com/blog",
                    url = "https://ve77.com/blog",
                    password = "1234567"
                ),
                PrivacyFolder(folderName = "娱乐", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "博客"))
            ),
        )

        val privacyCardList = mutableListOf<PrivacyCard>(
            PrivacyCard(
                PrivacyCardInfo(privacyName = "测试校园卡", privacyDesc = "华中科技大学校园卡。"),
                PrivacyCardDetails(owner = "weiyi", number = "U201814550", password = "1234567"),
                PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "校园卡"))
            ),
            PrivacyCard(
                PrivacyCardInfo(privacyName = "测试身份证", privacyDesc = "个人身份证"),
                PrivacyCardDetails(
                    owner = "weiyi1",
                    number = "452724***********1",
                    password = "1234567"
                ),
                PrivacyFolder(folderName = "论坛", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "身份证"))
            ),
            PrivacyCard(
                PrivacyCardInfo(privacyName = "测试驾驶证", privacyDesc = "床前明月光，疑是地上霜。"),
                PrivacyCardDetails(owner = "weiyi1", number = "U201814550", password = "1234567"),
                PrivacyFolder(folderName = "办公", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "驾驶证"))
            ),
            PrivacyCard(
                PrivacyCardInfo(privacyName = "测试银行卡", privacyDesc = "举头望明月，低头思故乡。"),
                PrivacyCardDetails(owner = "weiyi1", number = "U201814550", password = "1234567"),
                PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "银行卡"))
            )
        )

        val privacyFriendsList = mutableListOf<PrivacyFriendsInfo>(
            PrivacyFriendsInfo(
                nickname = "周同学",
                name = "周翀",
                sex = 1,
                birthday = "2000-05-01",
                phone = "15623356029",
                qq = "791422171",
                email = "791422171@qq.com",
                wechat = "wy791422171",
                address = "华中科技大学",
                department = "计算机科学与技术学院",
                remark = "爱好学习，自强不息。"
            ),
            PrivacyFriendsInfo(
                nickname = "杜同学",
                name = "杜凤龙",
                sex = 0,
                birthday = "2000-05-01",
                phone = "15623356029",
                qq = "791422171",
                email = "791422171@qq.com",
                wechat = "wy791422171",
                address = "华中科技大学",
                department = "计算机科学与技术学院",
                remark = "爱好学习，自强不息"
            ),
            PrivacyFriendsInfo(
                nickname = "韦同学",
                name = "韦仪",
                sex = 1,
                birthday = "2000-05-01",
                phone = "15623356029",
                qq = "791422171",
                email = "791422171@qq.com",
                wechat = "wy791422171",
                address = "华中科技大学",
                department = "计算机科学与技术学院",
                remark = "学生"
            ),
        )
        privacyPassList.forEach { pass ->
            pass.save()
        }
        privacyCardList.forEach { card ->
            card.save()
        }
        privacyFriendsList.forEach { friends ->
            friends.save()
        }

        LogUtil.msg(LitePal.findAll(PrivacyTag::class.java).toString())
        LogUtil.msg(LitePal.findAll(PrivacyFolder::class.java).toString())
        LogUtil.msg(LitePal.findAll(PrivacyCardDetails::class.java).toString())
        LogUtil.msg(LitePal.findAll(PrivacyCardInfo::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassDetails::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassInfo::class.java).toString())

//        privacyPassList.forEach { pass ->
//            pass.delete()
//        }
//        privacyCardList.forEach { card ->
//            card.delete()
//        }
//
//        LogUtil.msg(LitePal.findAll(PrivacyTag::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyFolder::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyCardDetails::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyCardInfo::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassDetails::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassInfo::class.java).toString())


        val cards = PrivacyCard.getAll()
        LogUtil.msg(cards.toString())

        val passwords = PrivacyPass.getAll()
        LogUtil.msg(passwords.toString())

        SpUtil.setValue(SettingConstant.SP_KEY_DATABASE_INIT,false);
    }
}