package com.ve.module.locker.common.config

import android.app.Application
import com.ve.lib.common.vutils.AppContextUtils
import com.ve.module.locker.BuildConfig
import com.ve.module.locker.R


/**
 * @Author  weiyi
 * @Date 2022/4/14
 * @Description  current project locker-android
 */
object SettingConstant {


    var mContext: Application = AppContextUtils.mContext
    val isDebug:Boolean=BuildConfig.DEBUG


    const val SP_KEY_LOGIN_STATE_KEY = "locker_login_state"
    const val SP_KEY_LOGIN_DATA_KEY = "locker_login_data"




    val SP_KEY_RECRATE_DATABASE="sp_key_recreate_database"
    val SP_KEY_DATABASE_INIT="sp_key_database_init"
    val SP_KEY_KEY_MANAGER="sp_key_key_manager"

    val SP_KEY_ACCOUNT_SETTING = mContext.getString(R.string.sp_key_account_setting)
    val SP_KEY_STYLE_SETTING = mContext.getString(R.string.sp_key_style_setting)
    val SP_KEY_NEWS_SETTING = mContext.getString(R.string.sp_key_news_setting)
    val SP_KEY_CACHE_SETTING = mContext.getString(R.string.sp_key_cache_setting)
    val SP_KEY_OTHER_SETTING = mContext.getString(R.string.sp_key_other_setting)
    val SP_KEY_ABOUT_SETTING= mContext.getString(R.string.sp_key_about_setting)
    /**
     * 基础设置
     */
    val SP_KEY_NO_PHOTO = "sp_key_no_photo"
    val SP_KEY_SHOW_TOP = "sp_key_no_show_top"
    val SP_KEY_NIGHT_MODE = "sp_key_night_mode"
    val SP_KEY_AUTO_NIGHT_MODE = "sp_key_auto_night_mode"

    val SP_KEY_AUTO_NIGHT_START = "sp_key_auto_night_start"
    val SP_KEY_AUTO_NIGHT_END = "sp_key_auto_night_end"


    /**
     * 辅助功能
     */
    val SP_KEY_BIOMETRICS="sp_key_biometrics"
    val SP_KEY_LONG_COPY="sp_key_long_copy"
    val SP_KEY_AUTO_FILL="sp_key_auto_fill"

    /**
     * 其他设置
     */
    val SP_KEY_TEXT_SIZE = "sp_key_text_size"
    val SP_KEY_THEME_COLOR = "sp_key_theme_color"
    val SP_KEY_NAV_COLOR = "asp_key_nav_color"
    val SP_KEY_CLEAR_CACHE = "sp_key_clean_cache"
    val SP_KEY_SCAN_QR_CODE = "sp_key_scan_qr_code"

    /**
     * 关于
     */
    val SP_KEY_APP_VERSION = "sp_key_app_version"
    val SP_KEY_APP_WEBSITE = "sp_key_app_website"
    val SP_KEY_UPDATE_LOG = "sp_key_update_log"
    val SP_KEY_SOURCE_CODE = "sp_key_source_code"
    val SP_KEY_COPYRIGHT = "sp_key_no_copyright"
    val SP_KEY_ABOUT_US = "sp_key_no_about_us"
}