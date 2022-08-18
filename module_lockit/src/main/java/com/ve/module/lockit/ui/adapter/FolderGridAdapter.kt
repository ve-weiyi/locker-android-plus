package com.ve.module.lockit.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.utils.file.ImageLoader
import com.ve.lib.common.base.adapter.BaseSlideAdapter
import com.ve.module.lockit.R
import com.ve.module.lockit.respository.database.entity.PrivacyFolder


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
class FolderGridAdapter:BaseSlideAdapter<PrivacyFolder,BaseViewHolder>(R.layout.lockit_item_tag) {

    override fun convert(holder: BaseViewHolder, item: PrivacyFolder) {
        holder.setText(R.id.item_privacy_category_name,item.folderName)


        val tv = holder.getView(R.id.item_privacy_category_name) as TextView


        val url=item.folderCover
        ImageLoader.loadView(context,url,tv)
//        if (url != null) {
//            if(url.startsWith("http")){
//                ImageLoader.loadView(context,url,tv)
//            }else if( url.startsWith("#")){
//                val gd: GradientDrawable = tv.background as GradientDrawable
//                gd.setColor(Color.parseColor(item.tagCover))
//            }
//        }
    }
}