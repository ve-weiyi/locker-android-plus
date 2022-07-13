package com.ve.module.lockit.respository.database

import com.ve.lib.common.utils.CommonUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.respository.database.entity.*
import com.ve.module.lockit.respository.database.vo.PrivacyCardInfo
import com.ve.module.lockit.respository.database.vo.PrivacyPassInfo
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project lockit-android
 */
object AppDataBase {

    fun initDataBase() {
        LitePal.aesKey("1234567890123456");
//        LogUtil.msg(SpUtil.setValue(SettingConstant.SP_KEY_DATABASE_INIT,true))
//        LogUtil.msg(SpUtil.getBoolean(SettingConstant.SP_KEY_DATABASE_INIT))

        if (SpUtil.getValue(LockitSpKey.SP_KEY_DATABASE_INIT, true)) {
            LogUtil.msg("data already init");
            return
        }

        LitePal.deleteDatabase("lockit")

        val folderList = mutableListOf(
            PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
            PrivacyFolder(folderName = "社交", folderCover = CommonUtil.randomColor().toString()),
            PrivacyFolder(folderName = "娱乐", folderCover = CommonUtil.randomColor().toString()),
            PrivacyFolder(folderName = "教育", folderCover = CommonUtil.randomColor().toString()),
            PrivacyFolder(folderName = "论坛", folderCover = CommonUtil.randomColor().toString()),
            PrivacyFolder(folderName = "办公", folderCover = CommonUtil.randomColor().toString()),
        )


        val tagList = mutableListOf(
            PrivacyTag(tagName = "测试标签"),
            PrivacyTag(tagName = "QQ账号"),
            PrivacyTag(tagName = "微信账号"),
            PrivacyTag(tagName = "校园卡"),
            PrivacyTag(tagName = "驾驶证"),
            PrivacyTag(tagName = "中国银行"),
        )


        val privacyPassList = mutableListOf(
            PrivacyPassInfo(
                PrivacyPass(
                    name = "QQ",
                    url = "qq.com",
                    account = "791422171", password = "123456789",
                    appPackageName = "com.tencent.mobileqq",
                    remark = "我的QQ账号。"
                ),

                PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "QQ账号"))
            ),
            PrivacyPassInfo(
                PrivacyPass(
                    name = "QQ",
                    url = "qq.com",
                    account = "919390162", password = "123456789",
                    appPackageName = "com.tencent.mobileqq",
                    remark = "我的QQ账号。"
                ),

                PrivacyFolder(folderName = "默认", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "QQ账号"))
            ),
            PrivacyPassInfo(
                PrivacyPass(
                    name = "邮箱",
                    url = "mail.qq.com",
                    account = "791422171@qq.com", password = "123456789",
                    appPackageName = "com.tencent.androidqqmail",
                    remark = "我的邮箱账号。"
                ),
                PrivacyFolder(folderName = "社交", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "邮箱账号"))
            ),
            PrivacyPassInfo(
                PrivacyPass(
                    name = "微信",
                    url = "wechat.com",
                    account = "wy791422171", password = "123456789",
                    appPackageName = "com.tencent.mm",
                    remark = "我的微信账号。"
                ),
                PrivacyFolder(folderName = "社交", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "微信账号"))
            ),
            PrivacyPassInfo(
                PrivacyPass(
                    name = "博客",
                    url = "ve77.com/blog",
                    account = "791422171", password = "123456789",
                    appPackageName = "",
                    remark = "我的个人博客。"
                ),
                PrivacyFolder(folderName = "社交", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "个人博客"))
            ),
        )

        val privacyCardList = mutableListOf(
            PrivacyCardInfo(
                PrivacyCard(
                    owner = "weiyi",
                    account = "U201814550", password = "1234567",
                    name ="华中科技大学",
                    remark = "华中科技大学校园卡"
                ),
                PrivacyFolder(folderName = "教育", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "校园卡"))
            ),
            PrivacyCardInfo(
                PrivacyCard(
                    owner = "weiyi",
                    account = "452724199987654321", password = "1234567",
                    name ="中华人民共和国",
                    remark = "身份证"
                ),
                PrivacyFolder(folderName = "论坛", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "身份证"))
            ),
            PrivacyCardInfo(
                PrivacyCard(
                    owner = "weiyi",
                    account = "452724199987654321", password = "1234567",
                    name ="中国银行储蓄卡",
                    remark = "中国银行银行卡"
                ),
                PrivacyFolder(folderName = "论坛", folderCover = CommonUtil.randomColor().toString()),
                mutableListOf(PrivacyTag(tagName = "测试标签"), PrivacyTag(tagName = "银行卡"))
            ),
        )

        val privacyFriendsList = mutableListOf<PrivacyFriend>(
            PrivacyFriend(
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
            PrivacyFriend(
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
            PrivacyFriend(
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

        folderList.forEach {
            folder->
            folder.save()
        }

        tagList.forEach {
            tag->
            tag.save()
        }

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


        LogUtil.msg(LitePal.findAll(PrivacyPass::class.java).toString())
        LogUtil.msg(LitePal.findAll(PrivacyCard::class.java).toString())
        LogUtil.msg(LitePal.findAll(PrivacyFriend::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassDetails::class.java).toString())
//        LogUtil.msg(LitePal.findAll(PrivacyPassInfo::class.java).toString())


        SpUtil.setValue(LockitSpKey.SP_KEY_DATABASE_INIT, false);
    }
}