package com.ve.lib.common.exception

/**
 * 接口状态码枚举
 * 1**	信息，服务器收到请求，需要请求者继续执行操作
 * 2**	成功，操作被成功接收并处理
 * 3**	重定向，需要进一步的操作以完成请求
 * 4**	客户端错误，请求包含语法错误或无法完成请求
 * 5**	服务器错误，服务器在处理请求的过程中发生了错误
 * @author yezhqiu
 * @date 2021/06/11
 */
enum class StatusCodeEnum(
    /**
     * 状态码
     */
    val code: Int? = null,

    /**
     * 描述
     */
    val desc: String? = null,

    ) {
    /**
     * 成功
     * 200	OK	请求成功。一般用于GET与POST请求
     */
    SUCCESS(200, "操作成功"),

    /**
     * 没有操作权限
     * 403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    AUTHORIZED(403, "没有操作权限"),

    /**
     * 系统异常
     * 服务器内部错误，无法完成请求
     */
    SYSTEM_ERROR(500, "系统异常"),

    /**
     * 失败
     * 505以上为自定义
     */
    FAIL(510, "操作失败"),

    /**
     * 参数校验失败
     */
    VALID_ERROR(520, "参数格式不正确"),

    /**
     * 用户名已存在
     */
    USERNAME_EXIST(521, "用户名已存在"),

    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST(522, "用户名不存在"),

    /**
     * qq登录错误
     */
    QQ_LOGIN_ERROR(531, "qq登录错误"),

    /**
     * 微博登录错误
     */
    WEIBO_LOGIN_ERROR(532, "微博登录错误");

}