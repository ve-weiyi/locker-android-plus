package com.ve.lib.common.utils.log

import android.util.Log
import com.ve.lib.common.utils.manager.ActivityController
import kotlin.math.min


/**
 * Created by yechao on 2020/1/7.
 * Describe : 日志管理
 *
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object LogUtil {
    private var TAG = "LogUtil"
    private var tag = "LogUtil"
    private var IS_LOG = true
    private const val MAX_LENGTH = 2000

    /**
     * 设置是否开启打印
     */
    fun setIsLog(isLog: Boolean) {
        IS_LOG = isLog
    }

    fun setIsLog(isLog: Boolean, tag: String) {
        TAG = tag
        IS_LOG = isLog
    }


    private fun logMsg(
        tag: String ?= TAG, msg: String, info: Array<String>,
        logFun: (String?, String, Array<String>) -> Int
    ) {
        val start = 0
        val end = min(msg.length, MAX_LENGTH)
        val message = msg.substring(start, end)

        logFun.invoke(tag, message, info)
    }


    @JvmStatic
    fun msg() {
        Log.e(tag, covertMessage())
    }

    @JvmStatic
    fun msg(obj: Any?) {
        Log.e(tag, covertMessage(obj.toString()))
    }

    @JvmStatic
    fun msg(tag: String?, msg: String) {
        Log.e(tag, covertMessage(msg))
    }

    @JvmStatic
    fun d(obj: Any) {
        if (IS_LOG) {
            Log.e(tag, covertMessage(obj.toString()))
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (IS_LOG) {
            Log.e(tag, covertMessage(msg))
        }
    }

    @JvmStatic
    fun e(msg: String) {
        if (IS_LOG) {
            Log.e(tag, covertMessage(msg))
        }
    }

    @JvmStatic
    fun e(tag: String?, msg: String) {
        if (IS_LOG) {
            Log.e(tag, covertMessage(msg))
        }
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     * stacktrace[0].getMethodName() 是 getStackTrace，
     * stacktrace[1].getMethodName() 是 getMethodName，
     * stacktrace[2].getMethodName() 是 autoJumpLogInfo。
     * stacktrace[3].getMethodName() 是 LogUtil.e。
     * stacktrace[4].getMethodName() 才是调用此的函数的函数名 。
     * 每次迭代调用，层数+1
     */
    private fun autoJumpLogInfo(stackCount: Int): Array<String> {
        val infos = arrayOf("", "", "")
        val stackTrace = Thread.currentThread().stackTrace
        infos[0] = stackTrace[stackCount].className.substring(stackTrace[stackCount].className.lastIndexOf(".") + 1)
        infos[1] = stackTrace[stackCount].methodName
        infos[2] = "(" + stackTrace[stackCount].fileName + ":" + stackTrace[stackCount].lineNumber + ")"
        return infos
    }

    /**
     * 转换 message
     */
    private fun covertMessage(msg: String?=null): String {
        if(msg==null){
            return ActivityController.currentActivityName + getMethodName(4)
        }

        val info= autoJumpLogInfo(4)
        return info[1] + info[2] + " --->> " + msg
    }

    @JvmStatic
    open fun error(msg: String?, vararg obj: Any?) {
        if (IS_LOG) {
            Log.e(tag, covertMessage(msg+obj))
        }
    }
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

}