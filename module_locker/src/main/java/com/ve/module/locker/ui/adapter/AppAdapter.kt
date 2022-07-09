package com.ve.module.locker.ui.adapter

import com.ve.lib.common.base.adapter.BaseSlideBindingAdapter
import com.ve.lib.common.base.adapter.VBViewHolder
import com.ve.module.locker.databinding.LockerItemAppBinding
import com.ve.module.locker.utils.AndroidUtil

/**
 * @Author  weiyi
 * @Date 2022/5/6
 * @Description  current project locker-android
 */
class AppAdapter : BaseSlideBindingAdapter<AndroidUtil.AppInfo, LockerItemAppBinding>() {


    override fun convert(holder: VBViewHolder<LockerItemAppBinding>, item: AndroidUtil.AppInfo) {
        holder.apply {
            vb.tvAppName.text = item.name
            vb.tvAppPackageName.text = item.packageName
            vb.ivAppIcon.setImageDrawable(item.icon)
        }
    }

}