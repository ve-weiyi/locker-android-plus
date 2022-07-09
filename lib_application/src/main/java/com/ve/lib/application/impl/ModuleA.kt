package com.ve.lib.application.impl

import com.ve.lib.application.impl.ApplicationImpl
import com.ve.lib.application.BaseApplication

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/24
 */
class ModuleA : ApplicationImpl {
    override fun onCreate(application: BaseApplication) {
        // 各个模块特有的第三方库等的初始化逻辑
    }
}