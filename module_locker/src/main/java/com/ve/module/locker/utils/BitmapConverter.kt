package com.ve.module.locker.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

/**
 * @author Liangyong Ni
 * description Bitmap、Drawable与String转换器
 * @date 2021/6/13
 */
class BitmapConverter {
    /**
     * 将字符串转化为Drawable
     *
     * @param icon 需要转化的字符串
     * @return 转化后的Drawable
     */
    @TypeConverter
    @Synchronized
    fun stringToDrawable(icon: String): Drawable? {
        val img = Base64.decode(icon.toByteArray(), Base64.DEFAULT)
        val bitmap: Bitmap
        if (img != null) {
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
            return BitmapDrawable(bitmap)
        }
        return null
    }

    /**
     * 将Drawable转化为字符串
     *
     * @param drawable 需要转化的Drawable
     * @return 转化后的字符串
     */
    @TypeConverter
    @Synchronized
    fun drawableToByte(drawable: Drawable?): String? {
        if (drawable != null) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(
                0, 0, drawable.intrinsicWidth,
                drawable.intrinsicHeight
            )
            drawable.draw(canvas)
            val size = bitmap.width * bitmap.height * 4
            // 创建一个字节数组输出流,流的大小为size
            val outputStream = ByteArrayOutputStream(size)
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            // 将字节数组输出流转化为字节数组byte[]，进而转化成Base64字符串
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
        return null
    }

    companion object {
        /**
         * 将base64字符串转化成Bitmap
         *
         * @param icon base64字符串
         * @return 转化后的Bitmap
         */
        @Synchronized
        fun stringToBitmap(icon: String): Bitmap? {
            val img = Base64.decode(icon.toByteArray(), Base64.DEFAULT)
            return if (img != null) {
                BitmapFactory.decodeByteArray(img, 0, img.size)
            } else null
        }

        /**
         * 将bitmap转为base64格式的字符串
         *
         * @param bitmap 需要转换的bitmap
         * @return 转化后的base64字符串
         */
        fun bitmapToString(bitmap: Bitmap): String {
            val bos = ByteArrayOutputStream()
            //参数100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
        }

        /**
         * 将Bitmap转化为Drawable
         *
         * @param bitmap 需要转化的Bitmap
         * @return 转换后的Drawable格式图片
         */
        fun bitmapToDrawable(bitmap: Bitmap?): Drawable {
            return BitmapDrawable(bitmap)
        }

        /**
         * 将Drawable转化为Bitmap
         *
         * @param drawable 需要转化的Drawable
         * @return 转化后的Bitmap
         */
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            return (drawable as BitmapDrawable).bitmap
        }
    }
}