package com.ve.module.lockit.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.ve.lib.common.vutils.ToastUtil;

/**
 * @Author weiyi
 * @Date 2022/4/19
 * @Description current project lockit-android
 */
public class StickUtils {

    /**
     * 实现文本复制到粘贴板功能
     * add by wangqianzhou
     * @param content

     */
    public static void copy(Context context,String content )
    {
        if(content.isEmpty()){
            ToastUtil.INSTANCE.showCenter("数据为空");
            return;
        }
        // 得到剪贴板管理器
        ClipboardManager cmb =(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        //第一个参数只是一个标记，随便传入。
        //第二个参数是要复制到剪贴版的内容
        ClipData clip = ClipData.newPlainText("lockit", content.trim());
        ToastUtil.INSTANCE.showCenter("已复制到粘贴板\n"+content);
        cmb.setPrimaryClip(clip);
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().toString().trim();
    }
}
