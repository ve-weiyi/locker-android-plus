package com.ve.module.locker.respository.http.bean

import java.io.Serializable


/**
 * 用户信息
 *
 * @author weiyi
 * @date 2022/04/10
 */

data class UserDetailDTO(

    /**
     * 用户账号id
     */
    val id: Int? = null,

    /**
     * 用户信息id
     */
    val userInfoId: Int? = null,

    /**
     * 邮箱号
     */
    val email: String? = null,

    /**
     * 登录方式
     */
    val loginType: Int? = null,

    /**
     * 用户名
     */
    val username: String? = null,

    /**
     * 密码
     */
    val password: String? = null,

    /**
     * 用户角色
     */
    val roleList: List<String>? = null,

    /**
     * 用户昵称
     */
    val nickname: String? = null,

    /**
     * 用户头像
     */
    val avatar: String? = null,

    /**
     * 用户简介
     */
    val intro: String? = null,

    /**
     * 个人网站
     */
    val webSite: String? = null,

    /**
     * 点赞文章集合
     */
    val articleLikeSet: Set<Any>? = null,

    /**
     * 点赞评论集合
     */
    val commentLikeSet: Set<Any>? = null,

    /**
     * 点赞说说集合
     */
    val talkLikeSet: Set<Any>? = null,

    /**
     * 用户登录ip
     */
    val ipAddress: String? = null,

    /**
     * ip来源
     */
    val ipSource: String? = null,

    /**
     * 是否禁用
     */
    val isDisable: Int? = null,

    /**
     * 浏览器
     */
    val browser: String? = null,

    /**
     * 操作系统
     */
    val os: String? = null,

    /**
     * 最近登录时间
     */
    val lastLoginTime: String? = null,
) :Serializable{

}