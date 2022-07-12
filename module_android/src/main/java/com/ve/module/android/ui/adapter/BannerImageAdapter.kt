package com.ve.module.android.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ve.module.android.repository.bean.BannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * Created by yechaoa on 2021/2/5.
 * Describe :
 */
class BannerImageAdapter(imageUrls: MutableList<BannerBean>) :
    BannerAdapter<BannerBean, BannerImageAdapter.ImageHolder>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ImageHolder(imageView)
    }

    override fun onBindView(holder: ImageHolder, data: BannerBean, position: Int, size: Int) {
        Glide.with(holder.itemView).load(data.imagePath).into(holder.imageView)
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }

}