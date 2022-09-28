package com.ve.lib.application.skin.factory

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.ve.lib.application.utils.LogUtil


/**
 * @author waynie
 * @date 2022/9/27
 * @desc lockit-android
 */
object ThemeAttrUtil {

    private val typedValue = TypedValue()

    /**
     * 解析主题，找到资源id，其实就是方案一里面的方法
     * attrId-->resId
     */
    fun getResIdFromTheme(theme: Resources.Theme, attrId: Int): Int {
        val typedValue = TypedValue()
        val success = theme.resolveAttribute(attrId, typedValue, true)
        //typedValue.resourceId 可能为0
        return typedValue.resourceId
    }

    /**
     * 找到插件工程的对应资源id
     * resId--->resId
     * //org.learn.skinchangedemp:layout/activity_main
     * Log.i(TAG, "${resources.getResourceName(R.layout.activity_main)} ")
     * //org.learn.skinchangedemp
     * Log.i(TAG, "${resources.getResourcePackageName(R.layout.activity_main)} ")
     * //activity_main
     * Log.i(TAG, "${resources.getResourceEntryName(R.layout.activity_main)} ")
     * //layout
     * Log.i(TAG, "${resources.getResourceTypeName(R.layout.activity_main)} ")
     * //资源id,  packageName=插件包的包名
     * Log.i(TAG, "${resources.getIdentifier("activity_main", "layout", packageName)}")
     */
    fun getIdFromPkg(context: Context, resId: Int, skinResource: Resources=context.resources, skinPkgName:String=context.packageName): Int {
        //主工程资源id->资源名字、类型->插件包中的资源id
        //R.color.black
        //black
        val resourceEntryName = context.resources.getResourceEntryName(resId)
        //color
        val resourceTypeName = context.resources.getResourceTypeName(resId)
        val result=skinResource.getIdentifier(resourceEntryName, resourceTypeName, skinPkgName) ?: 0
        LogUtil.msg("$resId == $result")
        return result
    }
}