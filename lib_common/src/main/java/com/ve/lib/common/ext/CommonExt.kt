package com.ve.lib.common.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author  weiyi
 * @Date 2022/7/13
 * @Description  current project lockit-android
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

/**
 * 格式化当前日期
 */
fun formatCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

/**
 * String 转 Calendar
 */
fun String.stringToCalendar(): Calendar {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}
