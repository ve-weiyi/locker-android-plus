package com.ve.lib.common.utils.provider

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ve.lib.common.utils.manager.ActivityController


/**
 * Created by yechao on 2020/1/7.
 * Describe : 调用系统分享图片和文字
 * 支持的类型
        text/plain
        application/
        image/
        video/
        audio/
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 */
object ShareUtil {


    /**
     * 文本类型
     */
    const val SHARE_TYPE_TEXT = "text/plain"

    const val SHARE_TYPE_APP = "application/*"
    
    const val SHARE_TYPE_IMAGE = "image/*"
    
    const val SHARE_TYPE_VIDEO = "video/*"
    
    const val SHARE_TYPE_AUDIO= "audio/*"
    
    /**
     * 调用系统分享图片
     */
    fun shareImage(title: String, uri: Uri) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = SHARE_TYPE_IMAGE
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        ActivityController.currentActivity!!.startActivity(Intent.createChooser(intent, title))
    }

    /**
     * 调用系统分享文字
     */
    fun shareText(title: String, text: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = SHARE_TYPE_TEXT
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ActivityController.currentActivity!!.startActivity(Intent.createChooser(intent, title))
    }

    /**
     * 跳转浏览器，需要 http开头
     */
    fun goUrl(context: Context, web_url: String){
        //kotlin 参数默认 const
        var url=web_url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}