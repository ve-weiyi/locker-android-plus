package com.ve.module.lockit.respository.http.model;


import lombok.Builder;
import lombok.Data;

/**
 * 用户信息vo
 *
 * @author yezhiqiu
 * @date 2021/08/03
 */

@Builder
@Data
public class UserInfoVO {

    /**
     * 用户昵称
     */
    public String nickname;

    /**
     * 个人头像
     */
    public String avatar;

    /**
     * 用户简介
     */
    public String intro;

    /**
     * 个人网站
     */
    public String webSite;
}
