package com.ve.module.lockit.respository.http.api

import com.ve.module.lockit.respository.database.entity.*
import com.ve.module.lockit.respository.database.entity.PrivacyTag
import com.ve.module.lockit.respository.http.bean.*
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
        const val BASE_URL = "http://192.168.100.26:8084/"

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
    @POST("/api/login")
    @FormUrlEncoded
    suspend fun loginlockit(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("code") code: String?="1234",
    ): LockitBaseBean<LoginVO>

    /**
     *
     * 注册账号
     */
    @POST("/api/register")
    @FormUrlEncoded
    suspend fun registerlockit(
        @Field("username") username: String?,
        @Field("password") password: String?,
        @Field("code") code: String?,
    ): LockitBaseBean<Any>
    /**
     *
     * 发送邮箱验证码
     */
    @GET("/api/users/code")
    suspend fun sendCode(@Query("username") username: String?): LockitBaseBean<Any>


    //-----------------------【 隐私标签 】----------------------

    /**
     * 增
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/tag/add/json")
    suspend fun tagAdd(@Body privacyTag: PrivacyTag): LockitBaseBean<Any>

    /**
     * 删
     * GET、DELETE 请求参数使用 @Query
     */
    @DELETE("/api/admin/privacy/tag/delete/json")
    suspend fun tagDelete(@Query("id") id: Int): LockitBaseBean<Any>

    /**
     * 改
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/tag/update/json")
    suspend fun tagUpdate(@Body privacyTag: PrivacyTag): LockitBaseBean<Any>

    /**
     * 查
     * POST 请求只有一个json格式的 @Body
     */
    @GET("/api/admin/privacy/tag/{id}/json")
    suspend fun tagQuery(@Path("id") id: Int): LockitBaseBean<Any>

    /**
     * 条件查
     */
    @POST("/api/admin/privacy/tag/list/json")
    suspend fun tagQueryList(@Body conditionVO: ConditionVO): LockitBaseBean<MutableList<PrivacyTag>>



    //-----------------------【 隐私文件夹 】----------------------

    /**
     * 增
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/folder/add/json")
    suspend fun folderAdd(@Body privacyFolder: PrivacyFolder): LockitBaseBean<Any>

    /**
     * 删
     * GET、DELETE 请求参数使用 @Query
     */
    @DELETE("/api/admin/privacy/folder/delete/json")
    suspend fun folderDelete(@Query("id") id: Int): LockitBaseBean<Any>

    /**
     * 改
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/folder/update/json")
    suspend fun folderUpdate(@Body privacyFolder: PrivacyFolder): LockitBaseBean<Any>

    /**
     * 查
     * POST 请求只有一个json格式的 @Body
     */
    @GET("/api/admin/privacy/folder/{id}/json")
    suspend fun folderQuery(@Path("id") id: Int): LockitBaseBean<Any>

    /**
     * 条件查
     */
    @POST("/api/admin/privacy/folder/list/json")
    suspend fun folderQueryList(@Body conditionVO: ConditionVO): LockitBaseBean<MutableList<PrivacyFolder>>



    //-----------------------【 隐私类型 】----------------------

    /**
     * 增
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/type/add/json")
    suspend fun typeAdd(@Body privacyType: Type): LockitBaseBean<Any>

    /**
     * 删
     * GET、DELETE 请求参数使用 @Query
     */
    @DELETE("/api/admin/privacy/type/delete/json")
    suspend fun typeDelete(@Query("id") id: Int): LockitBaseBean<Any>

    /**
     * 改
     * POST 请求只有一个json格式的 @Body
     */
    @POST("/api/admin/privacy/type/update/json")
    suspend fun typeUpdate(@Body privacyType: Type): LockitBaseBean<Any>

    /**
     * 查
     * POST 请求只有一个json格式的 @Body
     */
    @GET("/api/admin/privacy/type/{id}/json")
    suspend fun typeQuery(@Path("id") id: Int): LockitBaseBean<Any>

    /**
     * 条件查
     */
    @POST("/api/admin/privacy/type/list/json")
    suspend fun typeQueryList(@Body conditionVO: ConditionVO): LockitBaseBean<MutableList<Type>>


    //-----------------------【 隐私密码信息 】----------------------
    @GET("/api/user/privacy/pass/list")
    suspend fun privacyInfoPassList(): LockitBaseBean<MutableList<PrivacyDetailsPass>>

    @POST("/api/user/privacy/pass/add")
    suspend fun privacyInfoPassAdd(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockitBaseBean<Any>

    @POST("/api/user/privacy/pass/delete")
    suspend fun privacyInfoPassDelete(privacyInfoPassId :Int): LockitBaseBean<Any>

    @POST("/api/user/privacy/pass/update")
    suspend fun privacyInfoPassUpdate(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockitBaseBean<Any>

    @GET("/api/user/privacy/pass/info")
    suspend fun privacyInfoPassUser(): LockitBaseBean<MutableList<UserPrivacyInfoPassVO>>

    @POST("/api/user/privacy/card/parsing")
    suspend fun privacyInfoPassParsing(userPrivacyInfoPassVO: UserPrivacyInfoPassVO): LockitBaseBean<Any>

    //-----------------------【 隐私卡片信息 】----------------------
    @GET("/api/user/privacy/card/list")
    suspend fun privacyInfoCardList(): LockitBaseBean<MutableList<PrivacyDetailsCard>>

    @POST("/api/user/privacy/card/add")
    suspend fun privacyInfoCardAdd(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockitBaseBean<Any>

    @POST("/api/user/privacy/card/delete")
    suspend fun privacyInfoCardDelete(privacyInfoCardId :Int): LockitBaseBean<Any>

    @POST("/api/user/privacy/card/update")
    suspend fun privacyInfoCardUpdate(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockitBaseBean<Any>

    @GET("/api/user/privacy/card/info")
    suspend fun privacyInfoCardUser(): LockitBaseBean<MutableList<UserPrivacyInfoCardResponse>>

    @POST("/api/user/privacy/card/parsing")
    suspend fun privacyInfoCardParsing(userPrivacyInfoCardVO: UserPrivacyInfoCardResponse): LockitBaseBean<Any>

}