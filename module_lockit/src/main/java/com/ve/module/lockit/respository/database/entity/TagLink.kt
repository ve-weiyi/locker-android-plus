package com.ve.module.lockit.respository.database.entity

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project lockit-android
 */
data class TagLink(
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    var tagId: Long,

    var privacyId: Long,

    /**
     * 隐私类型，1 密码，2 卡片，3 好友
     */
    var type:Int,

) : LitePalSupport() {
}