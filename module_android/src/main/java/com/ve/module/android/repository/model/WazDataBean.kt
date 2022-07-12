package com.ve.module.android.repository.model

import com.squareup.moshi.Json
import java.io.Serializable


/**
 * Created by ve on 2021/2/5.
 * Describe :
 */

// 通用的带有列表数据的实体
data class BaseListResponseBody<T>(
    @Json(name = "curPage") val curPage: Int,
    @Json(name = "datas") val datas: MutableList<T>,
    @Json(name = "offset") val offset: Int,
    @Json(name = "over") val over: Boolean,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "total") val total: Int,
)

data class BannerBean(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String,
)
/**
 * 项目
 */
data class Project(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
)

/**
 *  导航
 */
data class Navigation(
    val articles: MutableList<Article>,
    val cid: Int,
    val name: String,
)


data class Tree(
    val children: ArrayList<Children>,
    var isShow: Boolean,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterd: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
) : Serializable {
    data class Children(
        val children: ArrayList<Any>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int,
    ) : Serializable
}
// 热门搜索
data class Hotkey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int,
)

//常用网站
data class Friend(
    @Json(name = "icon") val icon: String,
    @Json(name = "id") val id: Int,
    @Json(name = "link") val link: String,
    @Json(name = "name") val name: String,
    @Json(name = "order") val order: Int,
    @Json(name = "visible") val visible: Int
)

// 公众号列表实体
data class WxChapters(
    val children: MutableList<String>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
)

// 登录数据
data class LoginData(
    val chapterTops: MutableList<String>,
    val collectIds: MutableList<String>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val token: String,
    val type: Int,
    val username: String,
)

// 排行榜实体
data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String,
)

// 我的分享
data class ShareResponseBody(
    val coinInfo: CoinInfo,
    val shareArticles: ArticleResponseBody
)