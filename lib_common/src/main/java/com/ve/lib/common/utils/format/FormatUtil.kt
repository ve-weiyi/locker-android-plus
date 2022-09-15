package com.ve.lib.common.utils.format

import android.text.TextUtils
import com.google.gson.Gson
import org.json.JSONException
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @Author weiyi
 * @Date 2022/7/13
 * @Description current project lockit-android
 * Android fastjson封装工具
 * https://www.jianshu.com/p/52ba5efb031f
 *  当数据量较小的时候（1～100），建议使用 Gson；
 *  当数据量较大的时候，建议使用Jackson；
 *  在大数据量的时候，虽然FastJson优势上来了，但是因为有漏洞，不得不放弃。
 */
object FormatUtil {

    val gson by lazy { Gson() }
    /**
     * 对象转化为json fastjson 使用方式
     *
     * @return
     */
    fun objectToJson(`object`: Any?): String {
        if (`object` == null) {
            return ""
        }
        try {
            return gson.toJson(`object`)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * json转化为对象  fastjson 使用方式
     *
     * @return
     */
    fun <T> jsonToObject(jsonData: String?, clazz: Class<T>?): T? {
        var t: T? = null
        if (TextUtils.isEmpty(jsonData)) {
            return null
        }
        try {
            t = gson.fromJson(jsonData, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }


    /**
     * 数据格式化
     */
    fun numFormat(value: Double?): String {
        return if (value == null) "0.00" else DecimalFormat("0.00").apply {
            roundingMode = RoundingMode.HALF_UP
        }.format(value)
    }



}