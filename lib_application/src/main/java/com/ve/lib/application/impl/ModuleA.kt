package com.ve.lib.application.impl

import android.app.Application
import com.ve.lib.application.BaseApplication

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/24
 */
class ModuleA :Application(), ModuleApplication {

    override fun onCreate() {
        super.onCreate()
        // 通用的初始化逻辑
    }

    /**
     * fei
     */
    override fun onModuleCreate(application: BaseApplication) {
        // 各个模块特有的第三方库等的初始化逻辑
    }
}