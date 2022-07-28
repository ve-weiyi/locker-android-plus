package com.ve.module.lockit.respository.http.api

import com.ve.module.lockit.respository.http.bean.*
import com.ve.module.lockit.respository.http.model.QQLoginVO
import com.ve.module.lockit.respository.http.model.UserInfoVO
import retrofit2.http.*

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
interface LockitService {

    companion object {
        /**
         * 服务器地址
         */
//        const val BASE_URL = "https://ve77.cn:8084/"
        /**
         * 本地测试地址 只能填服务器的地址+端口，并且以/结尾
         * 本地调试不要用证书
         */
        const val BASE_URL = "http://192.168.2.101:8084/"

        //模拟器连接地址
//        const val BASE_URL = "http://10.0.2.2:8084/"
    }

    //-----------------------【 登录 】----------------------
    /**
     * 登录
     * http://www.wanandroid.com/user/login
     * @param username
     * @param password
     */
    @POST("/api/user/login")
    @FormUrlEncoded
    suspend fun loginLockit(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("code") code: String?="1234",
    ): LockitBaseBean<LoginDTO>


    @POST("/api/user/oauth/qq")
    suspend fun loginLockitQQ(
        @Body qqLoginVO: QQLoginVO
    ): LockitBaseBean<LoginDTO>


    /**
     *
     * 注册账号
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/user/register")
    @FormUrlEncoded
    suspend fun registerLockit(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("code") code: String?="1234",
    ): LockitBaseBean<Any>

    /**
     * @Query GET、DELETE 请求参数使用
     * @Path("id")  替换路径中的{id}
     * 发送邮箱验证码
     */
    @GET("/api/user/code")
    suspend fun sendCode(@Query("username") username: String?): LockitBaseBean<Any>


    //-----------------------【 隐私标签 】----------------------

    @PUT("/user/info")
    suspend fun updateUserInfo(
        @Body userInfoDTO: UserInfoVO
    ): LockitBaseBean<Any>

    @GET("/api")
    suspend fun report(): LockitBaseBean<Any>

}