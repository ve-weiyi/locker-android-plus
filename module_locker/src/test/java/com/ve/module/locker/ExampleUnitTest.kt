package com.ve.module.locker

import com.ve.lib.common.vutils.DateTimeUtil
import com.ve.module.locker.utils.PasswordUtils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.
 * test是单元测试类.运行在本地开发机上，可以脱离Android运行时环境,速度快.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun logMsg(){
        val str="452724199911171333"
        println(PasswordUtils.hidePassword(str))
        println(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n" +
                " * test是单元测试类.运行在本地开发机上，可以脱离Android运行时环境,速度快.")
    }

    @Test
    fun test2(){
        println(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n" +
                " * test是单元测试类.运行在本地开发机上，可以脱离Android运行时环境,速度快.")
        val date=Date()
        println(date)
        println(DateTimeUtil.formatDate(date))
        println(DateTimeUtil.formatDate(date.toString()))
    }
}