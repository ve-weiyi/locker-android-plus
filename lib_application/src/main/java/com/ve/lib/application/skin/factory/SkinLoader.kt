package com.ve.lib.application.skin.factory

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.util.Log
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.ve.lib.application.utils.LogUtil

/**
 * SkinLoader.loadResource
 * SkinLoader.changeSkin
 */
class SkinLoader {

    companion object {
        const val TAG = "SkinLoader"
        val instance = SkinLoader()
    }

    private var resource: Resources? = null
    private var skinPkgName: String? = null
    @StyleRes
    private var selectedThemeRes = 0

    private var themeCache= HashMap<Int, Resources.Theme>()
    private val skinFactoryMap: Map<String, SkinFactory>? = null

    fun getSelectedThemeRes(): Int {
        return selectedThemeRes
    }

    /**
     * 改变主题
     */
    fun changeTheme(context: Context,@StyleRes themeRes: Int) {
        var theme = getOrCreateTheme(context,themeRes)
        if (this.selectedThemeRes != themeRes) {
            this.selectedThemeRes = themeRes
            for (factory in skinFactoryMap!!.values) {
                factory.changeSkin(theme)
            }
        }
    }

    private fun getOrCreateTheme(context: Context,@StyleRes themeRes: Int): Theme {
        var theme = themeCache[themeRes]
        if (theme == null) {
            if(resource==null){
                loadResource(context,null)
            }
            theme = this.resource!!.newTheme()
            theme.applyStyle(ThemeAttrUtil.getIdFromPkg(context,themeRes,resource,skinPkgName), true)
            themeCache[themeRes] = theme
        }
        return theme!!
    }


    fun loadResource(context: Context, skinPath: String?) {
        try {
            LogUtil.msg(skinPath)
             //加载本应用的资源
            if (skinPath == null) {
                skinPkgName = context.packageName
                resource = context.theme.resources
            } else {

                val packageArchiveInfo = context.packageManager.getPackageArchiveInfo(
                    skinPath,
                    PackageManager.GET_ACTIVITIES
                )
                if (packageArchiveInfo == null) {
                    Log.e(TAG, "loadResource: app load fail")
                    return
                }
                skinPkgName = packageArchiveInfo.packageName

                val assetManager = AssetManager::class.java.newInstance()
                val method = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
                method.invoke(assetManager, skinPath)
                //resource = context.theme.resources
                resource = Resources(assetManager, context.resources.displayMetrics, context.resources.configuration)
            }

        } catch (e: Exception) {
            Log.e(TAG, "loadResource: ", e)
        }
    }


    fun getColor(context: Context, redId: Int): Int {
        val identifier = ThemeAttrUtil.getIdFromPkg(context, redId,resource,skinPkgName)
        if (resource == null || identifier <= 0) {
            return ContextCompat.getColor(context, redId)
        }
        return resource!!.getColor(identifier, null)
    }

    fun getString(context: Context, redId: Int): String {
        //找到插件工程的对应资源id
        val identifier = ThemeAttrUtil.getIdFromPkg(context, redId,resource,skinPkgName)
        //获取失败时，返回本应用的Id
        if (resource == null || identifier <= 0) {
            return context.getString(redId)
        }
        //获取插件工程的资源
        return resource!!.getString(identifier)
    }


    fun reset() {
        resource = null
        skinPkgName = null
    }

}