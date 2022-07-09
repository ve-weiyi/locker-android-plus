package com.ve.module.locker.model.http.model


/**
 * 查询条件
 *
 * @author weiyi
 * @date 2021/07/29
 */

data class ConditionVO(
    var id: Int? = null,
    var folderId:Long?=null,
    var tagId:Long?=null,
    var keyWords:String=""
) {
}