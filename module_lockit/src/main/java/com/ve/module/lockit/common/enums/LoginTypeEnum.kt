package com.ve.module.lockit.common.enums

/**
 * @Author weiyi
 * @Date 2022/7/10
 * @Description current project lockit-android
 */
enum class LoginTypeEnum(

    /**
     * 类型
     */
    val type: Int,


    /**
     * 描述
     */
    val desc: String

) {
    /**
     * 密码类
     */
    ACCOUNT(1,"邮箱"),

    QQ(2,"QQ"),

    Weibo(3,"微博");

}