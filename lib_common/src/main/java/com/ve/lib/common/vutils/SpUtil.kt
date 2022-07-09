package com.ve.lib.common.vutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
        AppContextUtils.getApp().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
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


    fun <T>getValue(key: String, defaultValue: T): T {

        try{
            return getSharedPreferences(key,defaultValue)
        }catch (e : Exception){
            LogUtil.msg(e.message!!)
            return defaultValue
        }finally {

        }
    }

    @SuppressLint("CommitPrefEdits")
    fun <T>setValue(key: String, value :T ) {
        putSharedPreferences(key,value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun <T> putSharedPreferences(name: String, value: T) = with(sp.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getSharedPreferences(name: String, default: T): T = with(sp) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default) ?: ""
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialization(getString(name, serialize(default)) ?: "")
        }
        return res as T
    }

    /**
     * 序列化对象

     * @param person
     * *
     * @return
     * *
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
     * *
     * @return
     * *
     * @throws IOException
     * *
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