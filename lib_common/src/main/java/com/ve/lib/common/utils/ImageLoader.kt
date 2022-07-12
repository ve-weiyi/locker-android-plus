package com.ve.lib.common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.ve.lib.common.R
import com.ve.lib.common.network.util.NetWorkUtil
import com.ve.lib.common.vutils.AppContextUtils
import com.ve.lib.common.vutils.LogUtil


/**
 * Created by chenxz on 2018/6/12.
 */
object ImageLoader {

    // 1.开启无图模式 2.非WiFi环境 不加载图片
    private val isLoadImage =
        !SettingUtil.getIsNoPhotoMode() && NetWorkUtil.isConnected(AppContextUtils.mContext)

    //通过 RequestOptions 共享配置
    private val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .placeholder(R.drawable.bg_placeholder)

    /**
     * 加载图片
     * @param context
     * @param url
     * @param iv
     */
    fun load(context: Context, url: String?, iv: ImageView?) {
        if (isLoadImage) {
            iv?.apply {
                //取消加载
                Glide.with(context)
                    .clear(iv)

                Glide.with(this)
                    .load(url)
                    .apply(options)
                    .into(iv)

//                Glide.with(this)//上下文对象Context
//                    .load(imgUrl)//图片地址
//                    .placeholder(R.mipmap.ic_launcher)//加载前的占位图
//                    .error(R.mipmap.ic_launcher)//加载失败的占位图
//                    .override(150,100)//指定图片尺寸(一般不用设置，Glide会自动根据ImageView的大小来决定图片的大小)
//                    .skipMemoryCache(true)//跳过内存缓存
//                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//根据图片资源智能地选择使用哪一种缓存策略（默认选项）
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//既缓存原始图片，也缓存转换过的图片
//                    .diskCacheStrategy(DiskCacheStrategy.DATA)//只缓存原始图片
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//只缓存转换过的图片
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
//                    .priority(Priority.HIGH)//优先级
//                    .thumbnail(
//                        Glide.with(this).load(imgUrl).override(20)
//                    ) //设置缩略图
//                    .thumbnail(0.25f)//压缩原图为缩略图
//                    .into(ivGlide2)
            }
        }
    }

    /**
     * 使用本地图片显示背景图的方法
     *
     * @param context 上下文对象
     * @param view    要显示背景图的控件
     * @param url     背景图的url
     */
    suspend fun loadPicture(context: Context, url: String?): Drawable? {
       try {
            var picUrl = "https://static.ve77.cn/avatar.jpg"
            if (url != null) {
                picUrl = url
            }
            val file =
                Glide.with(context).asDrawable().load(picUrl).centerCrop().submit(100, 100).get()
            return file
        }catch (e: Exception) {
           LogUtil.d(e.message!!)
           e.printStackTrace()
           return null
//           return context.resources.getDrawable(R.drawable.bg_placeholder,null)
       }
    }


    /**
     * 使用本地图片显示背景图的方法
     *
     * @param context 上下文对象
     * @param view    要显示背景图的控件
     * @param url     背景图的url
     */
    fun loadView(context: Context, url: String?, view: View) {
        if (isLoadImage) {
            if (url != null) {
                if (url.startsWith("http")) {
                    Glide.with(context).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                        ) {
                            view.background = BitmapDrawable(null, resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })

                } else if (url.startsWith("#")) {
                    try {

                        val colorInt = Color.parseColor(url)
                        val colorStateList = ColorStateList.valueOf(colorInt)
                        view.backgroundTintList = colorStateList

//                    val gd: GradientDrawable = view.background as GradientDrawable
//                    gd.setColor(Color.parseColor(url))
//                    view.setBackgroundColor(Color.parseColor(url))
                    } catch (e: Exception) {
                        LogUtil.d(e.message!!)
                        e.printStackTrace()
                    }

                } else {
                    try {
                        val colorInt = url.toInt()
                        val colorStateList = ColorStateList.valueOf(colorInt)
                        view.backgroundTintList = colorStateList

//                    val gd: GradientDrawable = view.background as GradientDrawable
//                    gd.setColor(colorInt)
//                    gd.colorFilter=LightingColorFilter(Color.WHITE,colorInt)
//                    gd.setTint(colorInt)
//                    view.background.setTint(colorInt)
//                    view.setBackgroundColor(Color.parseColor(colorInt.toString(16).replace("-","#")))
                    } catch (e: Exception) {
                        LogUtil.d(e.message!!)
                        e.printStackTrace()
                    }
                }
            }

        }
    }
}