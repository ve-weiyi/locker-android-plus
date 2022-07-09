package com.ve.lib.common.config

import com.tencent.mmkv.MMKV

/**
 * Created by yechaoa on 2020/2/4.
 * Describe : 配置文件
 */
object AppConfig {

    const val IS_LOGIN = "isLogin"

    const val COOKIE = "cookie"

    const val vePassName = "ve-weiyi"
    const val vePassWord = "waz791422171"

    const val BUGLY_ID = "f85c418abb"
    const val LOGIN_KEY = "login"
    const val HAS_NETWORK_KEY = "has_network"


    object Fir {
        const val id = "60c1bead23389f4d5ed11816"
        const val api_token = "63c4b007f97cc281e6dbd8a09be87524"
        // http://api.bq04.com/apps/latest/60c1bead23389f4d5ed11816?api_token=63c4b007f97cc281e6dbd8a09be87524
    }


    const val BASE_URL_UPDATA = "http://api.bq04.com/" //检查更新

    const val BASE_URL_Qingyunke = "http://api.qingyunke.com/" //聊天机器人

    /**
     *
     * 静态方法和静态变量会被放在 companion object 当中，成为伴生方法和伴生常量。
     * 而这时候，会发现在 Java 中调用它们的方式会不一样，如下：
     * 如果要使转换后的 Kotlin 代码在 Java 上调用起来和以前的习惯一样，
     * 则需要分别使用 @JvmStatic 和 @JvmField 注解，才能使它们暴露为静态方法或静态字段，如下：
     *
     */
    @JvmField
    val kv: MMKV = MMKV.defaultMMKV()

    const val vip1 = "https://www.administratorw.com/index/qqvod.php?url=" //vip1

    const val vip2 = "https://www.administratorw.com/video.php?url=" //vip2

    const val vip3 = "http://www.82190555.com/video.php?url=" //vip3

    const val vip4 = "http://www.sfsft.com/video.php?url=" //vip4

    const val vip5 = "https://wannengrun.com/" //vip5

    const val tianqi = "https://wis.qq.com/weather/" //天气

}