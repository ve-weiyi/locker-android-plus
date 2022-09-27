package com.ve.lib.application.skin.factory

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue


/**
 * @author waynie
 * @date 2022/9/27
 * @desc lockit-android
 */
object ThemeAttrUtil {
    private val typedValue = TypedValue()

    /**
     * 解析主题，找到资源id，其实就是方案一里面的方法
     */
    fun getResIdFromTheme(context: Context, attrId: Int): Int {
        val typedValue = TypedValue()
        val success = context.theme.resolveAttribute(attrId, typedValue, true)
        //typedValue.resourceId 可能为0
        return typedValue.resourceId
    }

    /**
     * 找到插件工程的对应资源id
     */
    fun getIdFromPkg(context: Context,redId: Int, resource: Resources?=null,  skinPkgName:String?=null): Int {
        if(skinPkgName==context.packageName)
            return redId
        //主工程资源id->资源名字、类型->插件包中的资源id
        //R.color.black
        //black
        val resourceEntryName = context.resources.getResourceEntryName(redId)
        //color
        val resourceTypeName = context.resources.getResourceTypeName(redId)
        return resource?.getIdentifier(resourceEntryName, resourceTypeName, skinPkgName) ?: 0
    }
}