package com.ve.module.locker.model.db.entity

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * @Author  weiyi
 * @Date 2022/4/15
 * @Description  current project locker-android
 */
data class TagAndPass(
    @Column(unique = true, defaultValue = "unknown")
    var id: Long = 0,

    var tagId: Long,

    var privacyId: Long,

    ) : LitePalSupport() {
}