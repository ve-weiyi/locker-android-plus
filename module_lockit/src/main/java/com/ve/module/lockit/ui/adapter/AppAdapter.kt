package com.ve.module.lockit.ui.adapter

import com.ve.lib.common.base.adapter.BaseSlideBindingAdapter
import com.ve.lib.common.base.adapter.VBViewHolder
import com.ve.module.lockit.databinding.LockitItemAppBinding
import com.ve.module.lockit.utils.AndroidUtil

/**
 * @Author  weiyi
 * @Date 2022/5/6
 * @Description  current project lockit-android
 */
class AppAdapter : BaseSlideBindingAdapter<AndroidUtil.AppInfo, LockitItemAppBinding>() {


    override fun convert(holder: VBViewHolder<LockitItemAppBinding>, item: AndroidUtil.AppInfo) {
        holder.apply {
            vb.tvAppName.text = item.name
            vb.tvAppPackageName.text = item.packageName
            vb.ivAppIcon.setImageDrawable(item.icon)
        }
    }

}