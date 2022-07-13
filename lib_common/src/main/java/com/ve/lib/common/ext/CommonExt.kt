package com.ve.lib.common.ext

import com.ve.lib.common.vutils.LogUtil

/**
 * @Author  weiyi
 * @Date 2022/7/13
 * @Description  current project locker-android
 */


/**
 * stacktrace[0].getMethodName() 是 getThreadStackTrace
 * stacktrace[1].getMethodName() 是 getStackTrace，
 * stacktrace[2].getMethodName() 是 getMethodName，
 * stacktrace[3].getMethodName() 才是调用 getMethodName 的函数的函数名。
 */
fun getMethodName(deep: Int=3): String? {
    val stacktrace = Thread.currentThread().stackTrace
    val e = stacktrace[deep]
    return " "+e.methodName
}

