package com.ve.module.lockit

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonParseException
import com.ve.lib.common.network.exception.ApiException
import com.ve.lib.common.widget.passwordGenerator.PasswordGenerator
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.module.lockit.common.config.LockitConstant
import com.ve.module.lockit.respository.database.AppDataBase
import com.ve.module.lockit.respository.database.entity.*
import com.ve.module.lockit.respository.AuthRepository
import com.ve.module.lockit.respository.http.model.ConditionVO
import com.ve.lib.common.utils.encrypt.AESUtil
import com.ve.module.lockit.utils.PasswordUtils
import com.ve.lib.common.utils.encrypt.RSAUtil
import kotlinx.coroutines.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.litepal.LitePal
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.
 * test是单元测试类.运行在本地开发机上，可以脱离Android运行时环境,速度快.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val apiService = LockitApiService().getApiService()
    private val liteDataBase = LitePal.getDatabase()

    @Test
    fun passLevel(){
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")
        println(PasswordUtils.checkPasswordLevel("86485880"))
        println(PasswordUtils.checkPasswordLevel("FILKRBK"))
        println(PasswordUtils.checkPasswordLevel("8jfd419U6A"))
        println(PasswordUtils.checkPasswordLevel("!@d3&1#**QGz"))
    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ve.module.test", appContext.packageName)
    }

    @Test
    fun simple(){
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")

        var passwordGenerator = PasswordGenerator(12,                           // To specify password length
            includeUpperCaseLetters = true,            // To include upper case Letters
            includeLowerCaseLetters = true,           // To include lower case Letters
            includeSymbols = true,                   // To include special symbols
            includeNumbers = true)                  // To include numbers (0-9)

        var generatedPassword = passwordGenerator.generatePassword()
    }

    @Test
    fun appDataBaseTest() {
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")

        runBlocking {
            AppDataBase.initDataBase()
        }
    }


    @Test
    fun apiServiceTest() {
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")
        runBlocking {
            //val result=apiService.tagDelete(0)

            val tag = PrivacyTag(
                id = 100,
                "测试标签",
                "#FAFAFA",
                "床前明月光"
            )
            //增
            var result = apiService.tagAdd(tag)
            LogUtil.e(result.toString())

            //改
            tag.tagName = "测试标签修改"
            result = apiService.tagUpdate(tag)
            LogUtil.e(result.toString())

            //查
            result = apiService.tagQuery(15)
            LogUtil.e(result.toString())

            //条件查
            var result1 = apiService.tagQueryList(conditionVO = ConditionVO(id = 15))
            LogUtil.e(result1.toString())
        }
    }


    @Test
    fun rsa() {
        RSAUtil.main(null)
        AESUtil.main(null)
    }

    @Test
    fun login() {
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")

        val repository = AuthRepository

        runBlocking {
            val result = repository.loginLockit(LockitConstant.username, LockitConstant.password)
            LogUtil.e(result.data().toString())
        }
    }


    @Test
    fun netTest() {
        LogUtil.e(" androidTest是整合测试。可以运行在设备或虛拟设备上.需要编译打包为APK在设备上运行，可以实时杏看细节.\n")
        LogUtil.e(" test是单元测试类.运行在本地开发机上，可以脱离Android运行时环境,速度快.")


    }


    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
    private fun onError(e: Exception, showErrorToast: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.errorCode) {
                    -1001 -> {
                        if (showErrorToast) ToastUtil.show(e.errorMsg)
                    }
                    // 其他错误
                    else -> {
                        if (showErrorToast) ToastUtil.show(e.errorMsg)
                    }
                }
                LogUtil.e(e.errorMsg)
            }
            // 网络请求失败
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is retrofit2.HttpException -> {
                if (showErrorToast) ToastUtil.show(" 网络请求失败")
                LogUtil.e(" 网络请求失败" + e.message)
            }
            // 数据解析错误
            is JsonParseException -> {
                if (showErrorToast) ToastUtil.show(" 数据解析错误")
                LogUtil.e(" 数据解析错误" + e.message)
            }
            // 其他错误
            else -> {
                if (showErrorToast) ToastUtil.show(e.message ?: return)
                LogUtil.e(e.message ?: return)
            }

        }
    }
}