package org.learn.skinchangedemo.utils

import android.util.Log


/**
 * Created by yechao on 2020/1/7.
 * Describe : 日志管理
 *
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object LogUtil {
    private var TAG = "LogUtil"
    private var IS_LOG = true
    private const val MAX_LENGTH = 2000

    /**
     * 设置是否开启打印
     */
    fun setIsLog(isLog: Boolean) {
        IS_LOG = isLog
    }

    fun setIsLog(isLog: Boolean, tag: String) {
        this.TAG = tag
        IS_LOG = isLog
    }


    @JvmStatic
    fun msg() {
        val info= getStackInfo()
        Log.e(this.TAG, info.covertMessage())
    }

    @JvmStatic
    fun msg(msg: Any?) {
        val info= getStackInfo()
        Log.e(this.TAG, info.covertMessage(msg.toString()))
    }
    @JvmStatic
    fun d(msg: Any?) {
        val info= getStackInfo()
        Log.d(this.TAG, info.covertMessage(msg.toString()))
    }

    @JvmStatic
    fun i(msg: Any?) {
        val info= getStackInfo()
        Log.i(this.TAG, info.covertMessage(msg.toString()))
    }

    @JvmStatic
    fun msg(tag: String?, msg: Any?) {
        val info= getStackInfo()
        Log.e(tag, info.covertMessage(msg.toString()))
    }

    @JvmStatic
    fun d(tag: Any, msg: String) {
        val info= getStackInfo()
        Log.d(tag.toString(), info.covertMessage(msg))
    }


    /**
     * 获取打印信息所在方法名，行号等信息
     * stacktrace[0].getMethodName() 是 VMStack.getThreadStackTrace
     * stacktrace[1].getMethodName() 是 Thread.getStackTrace，
     * stacktrace[2].getMethodName() 是 getMethodName，
     * stacktrace[3].getMethodName() 是 getStackInfo。
     * stacktrace[4].getMethodName() 是调用此函数的名称 LogUtil.d。
     * stacktrace[5].getMethodName() 是调用LogUtil.d的函数名。
     * 每次迭代调用，层数+1
     */
    private fun getStackInfo(stackCount: Int=5): StackTraceInfo {
        val stackTrace = Thread.currentThread().stackTrace

        val info= StackTraceInfo()
        info.className = stackTrace[stackCount].className
        info.simpleClassName = stackTrace[stackCount].className.substringAfterLast(".")
        info.methodName= stackTrace[stackCount].methodName
        info.jumpToLine = "(" + stackTrace[stackCount].fileName + ":" + stackTrace[stackCount].lineNumber + ")"
        return info
    }

    private data class StackTraceInfo(
        var jumpToLine:String="",
        var className:String="",
        var simpleClassName:String="",
        var methodName:String="",
    ){
        /**
         * 转换 message
         */
        fun covertMessage(msg: String?="object is null"): String {
            return "$jumpToLine$methodName --->> $msg"
        }
    }
}