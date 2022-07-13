package com.ve.module.lockit.respository.http.bean


/**
 * 角色
 * vo 后端返回给前端的数据类型
 * @author weiyi
 * @date 2022/04/03
 */
data class UserPrivacyInfo(
    //(value = "id", position = 1)
    public val id: Int = -1,

    //(value = "用户id", position = 2)
    public val userId: Int = -1,

    //(value = "隐私名", notes = "标签的备注名", example = "XX的QQ邮箱账号", position = 4)
    public val privacyName: String = "null",

    //( value = "隐私图标", notes = "标签的覆盖图标", example = "https://ve77.cn/blog/favicon.ico", position = 5 )
    public val privacyCover: String = "null",

    //(value = "隐私描述", notes = "标签描述", example = "床前明月光", position = 6)
    public val privacyDesc: String = "null",

    //(value = "创建时间", notes = "标签创建时间,不用填", position = 7)
    public val createTime: String = "null",

    //(value = "更新时间", notes = "标签更新时间,不用填", position = 8)
    public val updateTime: String = "null",

    //(value = "版本号", notes = "标签更新版本号,不用填", position = 9)
    public val version: String = "null",


    //(value = "类型id")
    public val privacyTypeId: Int = -1,

    //(value = "文件夹id")
    public val privacyFolderId: Int = -1,

    //(value = "隐私id")
    public val privacyDetailsId: Int = -1,
) {

}