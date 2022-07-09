package com.ve.module.sunny

import android.annotation.SuppressLint
import android.content.Context
import com.ve.lib.application.BaseApplication

class SunnyWeatherApplication : BaseApplication() {

    companion object {

        const val TOKEN = "" // 填入你申请到的令牌值

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}