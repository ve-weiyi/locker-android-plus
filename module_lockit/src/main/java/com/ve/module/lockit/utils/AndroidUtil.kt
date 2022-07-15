package com.ve.module.lockit.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.ve.lib.common.vutils.AppContextUtil
import com.ve.lib.common.vutils.LogUtil
import java.io.Serializable
import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.experimental.and

object AndroidUtil {

    /**
     * 获取版本名
     */
    fun getVersionName(): String {
        return try {
            val packageManager = AppContextUtil.getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(AppContextUtil.getApp().packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取版本号
     */
    fun getVersionCode(): Int {
        return try {
            val packageManager = AppContextUtil.getApp().packageManager
            val packageInfo = packageManager.getPackageInfo(AppContextUtil.getApp().packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    fun getAppIcon(context: Context, appId: String): Bitmap? {
        return try {
            val drawable = context.packageManager.getApplicationIcon(appId)
            drawableToBitmap(drawable)
        } catch (e: Exception) {
            Log.e("getAppIcon", "getAppIcon error", e)
            null
        }
    }

    fun getAppName(context: Context, appId: String): String? {
        return try {
            val appInfo =
                context.packageManager.getApplicationInfo(appId, PackageManager.GET_META_DATA)
            appInfo.loadLabel(context.packageManager).toString()
        } catch (e: Exception) {
            Log.e("getAppName", "getAppName error", e)
            return context.applicationInfo.name
        }
    }


    /**
     * 获取APP 信息，失败则返回本应用的信息
     */
    fun getAppInfo(context: Context, packageName: String): AppInfo? {
        val packageManager: PackageManager = context.packageManager
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
//            LogUtil.msg(appInfo.packageName)
            return AppInfo(
                appInfo.packageName,
                appInfo.loadLabel(packageManager).toString(),
                appInfo.loadIcon(packageManager)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return AppInfo(context.applicationInfo, packageManager)
        }
    }

    /**
     * 根据名称查找应用（忽略大小写）
     *
     * @param name  应用名
     * @param isAll 是否包含系统应用
     * @return 符合名称的所有应用
     */
    fun findAppsByName(name: String?, isAll: Boolean): ArrayList<AppInfo> {
        val result = ArrayList<AppInfo>()
        val list = getAllAppInfo(AppContextUtil.mContext, isAll)
        // 忽略大小写
        val pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE)
        for (i in list.indices) {
            val matcher = pattern.matcher(list[i].name)
            if (matcher.find()) {
                result.add(list[i])
            }
        }
        return result
    }

    /**
     * 获取当前设备上安装的所有App
     *
     */

    @SuppressLint("QueryPermissionsNeeded")
    fun getAllAppInfo(context: Context, isSystemApp: Boolean = false): MutableList<AppInfo> {

        val packageManager: PackageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(0)

        val appInfoList = mutableListOf<AppInfo>()

        for (i in packages.indices) {
            val packageInfo = packages[i]
            val appInfo = AppInfo()
            //获取应用名称
            appInfo.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
            //获取应用包名，可用于卸载和启动应用
            appInfo.packageName = packageInfo.applicationInfo.packageName
            //获取应用图标
            appInfo.icon = packageInfo.applicationInfo.loadIcon(packageManager)

            if (isSystemApp) {
                // 系统应用
                appInfoList.add(appInfo)
            } else {
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                    //非系统应用
                    appInfoList.add(appInfo)
                }
            }
        }


        return appInfoList
    }


    data class AppInfo(
        /**
         * 包名，主键
         */
        var packageName: String = "unknown",

        /**
         * 应用名，存储时忽略
         */
        var name: String = "unknown",

        /**
         * 引用对应的icon
         */
        var icon: Drawable? = null,

        ) : Serializable {

        constructor(applicationInfo: ApplicationInfo, packageManager: PackageManager) : this() {
            packageName = applicationInfo.packageName
            name = applicationInfo.loadLabel(packageManager).toString()
            icon = applicationInfo.loadIcon(packageManager)
        }

    }

    /**
     * 银行信息
     */
    data class BankInfo(

        /**
         * 名称
         */
        var name: String = "unknown",

        /**
         * icon链接
         */
        var iconUrl: String ,

        ) : Serializable {

            companion object{
                var zg=BankInfo( name = "中国银行", iconUrl = "https://static.ve77.cn/avatar/tiger.jpg")
            }
    }

    fun getCertificatesHash(context: Context, packageName: String): String {
        val pm: PackageManager = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val info: PackageInfo =
                pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val hashes = ArrayList<String>(info.signingInfo.apkContentsSigners.size)
            for (sig in info.signingInfo.apkContentsSigners) {
                val cert: ByteArray = sig.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                md.update(cert)
                hashes.add(bytesToHex(md.digest()))
            }
            hashes.sort()
            val hash = StringBuilder()
            for (i in 0 until hashes.size) {
                hash.append(hashes[i])
            }
            return hash.toString()
        } else {
            val info: PackageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val hashes = ArrayList<String>(info.signatures.size)
            for (sig in info.signatures) {
                val cert: ByteArray = sig.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                md.update(cert)
                hashes.add(bytesToHex(md.digest()))
            }
            hashes.sort()
            val hash = StringBuilder()
            for (i in 0 until hashes.size) {
                hash.append(hashes[i])
            }
            return hash.toString()
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {

        // 获取 drawable 长宽
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        drawable.setBounds(0, 0, width, height)

        // 获取drawable的颜色格式
        val config: Bitmap.Config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 创建bitmap
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, config)
        // 创建bitmap画布
        val canvas = Canvas(bitmap)
        // 将drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = (bytes[j] and 0xFF.toByte()).toInt()
            hexChars[j * 2] = HEX_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return String(hexChars)
    }


}