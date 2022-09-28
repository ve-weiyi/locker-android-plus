package com.ve.lib.application.skin.factory

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.ve.lib.application.R
import com.ve.lib.application.utils.LogUtil
import java.lang.RuntimeException

/**
 * SkinEngine.loadResource
 * SkinEngine.changeSkin
 */
object SkinEngine {

    const val TAG = "SkinEngine"

    private lateinit var mContext:Application
    private lateinit var resource: Resources
    private lateinit var skinPkgName: String

    @StyleRes
    private var selectedThemeRes = 0

    private var themeCache = HashMap<Int, Resources.Theme>()
    private val skinFactoryMap = HashMap<String, SkinFactory>()

    /**
     * 初始化
     */
    fun init(context: Application, @StyleRes themeRes: Int) {
        mContext=context
        skinPkgName = context.packageName
        resource = context.theme.resources
        selectedThemeRes=themeRes
    }

    
    fun getSelectedThemeRes(): Int {
        return selectedThemeRes
    }

    fun registerSkinFactory(factory: SkinFactory) {
        skinFactoryMap.put(factory.key, factory)
    }

    fun applyTheme(){

    }

    /**
     * 改变主题
     */
    fun changeTheme(context: Context, @StyleRes themeRes: Int) {
        LogUtil.msg(this.selectedThemeRes.toString() + "=" + themeRes)

        if (this.selectedThemeRes != themeRes) {
            this.selectedThemeRes = themeRes
            LogUtil.msg(this.selectedThemeRes.toString() + "=" + themeRes)

            val theme = getOrCreateTheme(context, themeRes)
            for (factory in skinFactoryMap.values) {
                factory.changeSkin(theme)
            }
        }
    }

    private fun getOrCreateTheme(context: Context, @StyleRes themeRes: Int): Theme {
        var theme = themeCache[themeRes]
        if (theme == null) {
            theme = this.resource.newTheme()
            theme.applyStyle(ThemeAttrUtil.getIdFromPkg(context, themeRes, resource, skinPkgName), true)
            themeCache[themeRes] = theme
        }
        return theme!!
    }
    
    fun loadResource(context: Context, skinPath: String) {
        try {
            LogUtil.msg(skinPath)

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

        } catch (e: Exception) {
            Log.e(TAG, "loadResource: ", e)
        }
    }


    fun getColor(redId: Int): Int {
        val identifier = ThemeAttrUtil.getIdFromPkg(mContext, redId, resource, skinPkgName)
        if (identifier <= 0) {
            return ContextCompat.getColor(mContext, redId)
        }
        return resource.getColor(identifier, null)
    }

    fun getString(redId: Int): String {
        //找到插件工程的对应资源id
        val identifier = ThemeAttrUtil.getIdFromPkg(mContext, redId, resource, skinPkgName)
        //获取失败时，返回本应用的Id
        if (identifier <= 0) {
            return mContext.getString(redId)
        }
        //获取插件工程的资源
        return resource.getString(identifier)
    }

    fun getDrawable(redId: Int): Drawable? {
        //找到插件工程的对应资源id
        val identifier = ThemeAttrUtil.getIdFromPkg(mContext, redId, resource, skinPkgName)
        //获取失败时，返回本应用的Id
        if (identifier <= 0) {
            return mContext.getDrawable(redId)
        }
        //获取插件工程的资源
        return resource.getDrawable(identifier,themeCache[selectedThemeRes])
    }

    fun reset() {
        LogUtil.msg("1")
        resource = mContext.resources
        skinPkgName = mContext.packageName
//        selectedThemeRes= R.style.AppTheme
    }

}