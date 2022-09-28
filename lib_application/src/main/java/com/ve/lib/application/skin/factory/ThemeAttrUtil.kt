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
     */
    fun getIdFromPkg(context: Context, resId: Int, resource: Resources=context.resources, skinPkgName:String=context.packageName): Int {
        //主工程资源id->资源名字、类型->插件包中的资源id
        //R.color.black
        //black
        val resourceEntryName = context.resources.getResourceEntryName(resId)
        //color
        val resourceTypeName = context.resources.getResourceTypeName(resId)
        val result=resource.getIdentifier(resourceEntryName, resourceTypeName, skinPkgName) ?: 0
        LogUtil.msg("$resId == $result")
        return result
    }
}