package com.ve.lib.common.vutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.alibaba.fastjson.JSONObject
import com.ve.lib.common.exception.BizException
import java.io.*
import java.lang.Exception


/**
 * Created by yechao on 2020/1/7.
 * Describe : SpUtilKt
 *
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object SpUtil {
    private const val FILE_NAME = "config"

    /**
     * Context.MODE_PRIVATE
     * 文件创建模式：默认模式，其中创建的文件只能由调用应用程序（或共享相同用户ID的所有应用程序）访问。
     */
    private val sp: SharedPreferences by lazy {
        AppContextUtil.getApp().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * String
     */
    fun setString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String = ""): String {
        return sp.getString(key, defValue)!!
    }

    /**
     * StringSet
     */
    fun setStringSet(key: String, value: Set<String>?) {
        sp.edit().putStringSet(key, value).apply()
    }

    fun getStringSet(key: String): Set<String> {
        return HashSet<String>(sp.getStringSet(key, HashSet<String>()))
    }

    /**
     * Int
     */
    fun setInt(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        return sp.getInt(key, defValue)
    }

    /**
     * Boolean
     */
    fun setBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defValue)
    }

    /**
     * Float
     */
    fun setFloat(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, defValue: Float = 0f): Float {
        return sp.getFloat(key, defValue)
    }

    /**
     * Long
     */
    fun setLong(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long = 0): Long {
        return sp.getLong(key, defValue)
    }

    /**
     * Remove
     */
    fun removeByKey(key: String) {
        sp.edit().remove(key).apply()
    }

    fun removeAll() {
        sp.edit().clear().apply()
    }


    /**
     * 删除全部数据
     */
    fun clearPreference() {
        sp.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun clearPreference(key: String) {
        sp.edit().remove(key).apply()
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    fun getAll(): Map<String, *> {
        return sp.all
    }

    /**
     * 空值会出错
     */
    @SuppressLint("CommitPrefEdits")
    fun <T> setValue(key: String, value: T) {
        if (value == null) {
//            throw BizException("value can not be null")
            LogUtil.msg("value can not be null")
            clearPreference(key)
            return
        }
        when (value) {
            is Long -> setLong(key, value)
            is String -> setString(key, value)
            is Int -> setInt(key, value)
            is Boolean -> setBoolean(key, value)
            is Float -> setFloat(key, value)
            else -> setString(key, JSONObject.toJSONString(value))
        }
    }

    fun <T> getValue(key: String, clazz: Class<T>): T? {
        try {
            var target = JSONObject.parseObject(getString(key), clazz)
            if (target == null) {
                return null
            } else {
                return target
            }
        } catch (e: Exception) {
            LogUtil.msg(e.message!!)
            LogUtil.msg(clazz.newInstance().toString())
            e.printStackTrace()
            return clazz.newInstance()
        } finally {

        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, defaultValue: T): T {
        try {
            val res: Any = when (defaultValue) {
                is Long -> getLong(key, defaultValue)
                is String -> getString(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Boolean -> getBoolean(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                else -> {
                    val res=getValue(key, defaultValue!!::class.java)
                    if(res==null){
                        return defaultValue
                    }else{
                        return res
                    }
                }
            }
            return res as T
        } catch (e: Exception) {
            LogUtil.msg(e.message!!)
            return defaultValue
        } finally {

        }
    }

    @Deprecated(
        "在存储时要求对象实现序列号接口，新的方法将对象保存为JSON形式存储",
        replaceWith = ReplaceWith("this.setObject(key,value)")
    )
    @SuppressLint("CommitPrefEdits")
    fun <T> setSerialize(key: String, value: T) {
        putSharedPreferences(key, value)
    }


    @Deprecated("已废弃", replaceWith = ReplaceWith("this.getObject(key,defaultValue::class.java)"))
    fun <T> getSerialize(key: String, defaultValue: T): T {
        try {
            return getSharedPreferences(key, defaultValue)
        } catch (e: Exception) {
            LogUtil.msg(e.message!!)
            return defaultValue
        } finally {

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun <T> putSharedPreferences(key: String, value: T) = with(sp.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key, serialize(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getSharedPreferences(key: String, default: T): T = with(sp) {
        val res: Any = when (default) {
            is Long -> getLong(key, default)
            is String -> getString(key, default) ?: ""
            is Int -> getInt(key, default)
            is Boolean -> getBoolean(key, default)
            is Float -> getFloat(key, default)
            else -> deSerialization(getString(key, serialize(default)) ?: "")
        }
        return res as T
    }


    /**
     * 序列化对象
     * @param person
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    /**
     * 反序列化对象
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}