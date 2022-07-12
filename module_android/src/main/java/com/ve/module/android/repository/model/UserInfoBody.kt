package com.ve.module.android.repository.model

// 用户个人信息
data class UserInfoBody(
    val coinCount: Int, // 总积分
    val level: Int,
    val nickname: String,
    val rank: String, // 当前排名
    val userId: Int,
    val username: String,
)

data class User(
    var admin: Boolean,
    var chapterTops: List<Any>,
    var coinCount: Int,
    var collectIds: List<Int>,
    var email: String,
    var icon: String,
    var id: Int,
    var nickname: String,
    var password: String,
    var publicName: String,
    var token: String,
    var type: Int,
    var username: String,
)

// 个人积分实体
data class UserScore(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String,
)

// 我的分享
data class UserShare(
    val coinInfo: CoinInfo,
    val shareArticles: ArticleResponseBody,
)