package com.ve.lib.auth

import com.mob.MobSDK
import com.ve.lib.application.BaseApplication
import com.ve.lib.application.impl.ModuleApplication

/**
 * @Author  weiyi
 * @Date 2022/7/16
 * @Description  current project lockit-android
 */
class AuthApplication :BaseApplication(),ModuleApplication{

    override fun onModuleCreate(application: BaseApplication?) {
        MobSDK.init(application)
    }
}