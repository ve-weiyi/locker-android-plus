package com.ve.lib.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.ve.lib.common.vutils.AppContextUtils
import com.ve.lib.common.vutils.LogUtil
import java.io.*
import java.lang.Exception


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
abstract class BaseSharePreference {

    var file_name = "locker_sp_file"
    private lateinit var  sp : SharedPreferences

    init {
        init()
    }


    fun init(){
        file_name=attachFileName()
        sp=attachSharedPreferences(AppContextUtils.mContext)
    }

    abstract fun attachFileName():String

    private fun attachSharedPreferences(context: Context): SharedPreferences {
        /**
         * Context.MODE_PRIVATE
         * 文件创建模式：默认模式，其中创建的文件只能由调用应用程序（或共享相同用户ID的所有应用程序）访问。
         */
        return context.getSharedPreferences(file_name, Context.MODE_PRIVATE)
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
        }catch (e :Exception){
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