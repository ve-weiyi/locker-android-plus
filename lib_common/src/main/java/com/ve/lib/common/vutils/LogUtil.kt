package com.ve.lib.common.vutils

import android.util.Log
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

    @JvmStatic
    fun msg(tag: String?,msg: String) {
        logMsg(
            tag=tag,
            msg=msg,
            info = autoJumpLogInfo(4),
            logFun = { tag,msg,info->
                Log.e(tag, info[1] + info[2] + " --->> " + msg)
            }
        )
    }

    private fun logMsg(tag: String?=null,msg: String,info :Array<String>,logFun: (String,String, Array<String> ) -> Int) {
        val start = 0
        val end = min(msg.length, MAX_LENGTH)
        val message=msg.substring(start, end)

        if (tag != null) {
            logFun.invoke(tag,message,info)
        }else{
            logFun.invoke(TAG,message,info)
        }
    }

    @JvmStatic
    fun msg(obj: Any) {
        logMsg(
            msg=obj.toString(),
            info = autoJumpLogInfo(4),
            logFun = { tag,msg,info->
                Log.e(tag, info[1] + info[2] + " --->> " + msg)
            }
        )
    }


    @JvmStatic
    fun i(msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.e(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }

    @JvmStatic
    fun i(tag: String?, msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.e(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }
    @JvmStatic
    fun d(msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.d(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }
    @JvmStatic
    fun d(tag: String, msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.d(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }
    @JvmStatic
    fun e(msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.e(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }
    @JvmStatic
    fun e(tag: String?, msg: String) {
        if (IS_LOG) {
            logMsg(
                msg=msg,
                info = autoJumpLogInfo(4),
                logFun = { tag,msg,info->
                    Log.e(tag, info[1] + info[2] + " --->> " + msg)
                }
            )
        }
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     */
    private fun autoJumpLogInfo(stackCount :Int):Array<String>{
        val infos = arrayOf("", "", "")
        val elements = Thread.currentThread().stackTrace
        infos[0] = elements[stackCount].className.substring(elements[stackCount].className.lastIndexOf(".") + 1)
        infos[1] = elements[stackCount].methodName
        infos[2] = "(" + elements[stackCount].fileName + ":" + elements[stackCount].lineNumber + ")"
        return infos
    }

    @JvmStatic
    open fun error(msg: String?, vararg obj: Any?) {
        logMsg(
            msg=msg+obj,
            info = autoJumpLogInfo(4),
            logFun = { tag,msg,info->
                Log.e(tag, info[1] + info[2] + " --->> " + msg)
            }
        )
    }


}