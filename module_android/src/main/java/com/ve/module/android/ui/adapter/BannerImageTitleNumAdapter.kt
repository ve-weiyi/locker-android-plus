package com.ve.module.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ve.module.android.R
import com.ve.module.android.repository.model.BannerBean
import com.youth.banner.adapter.BannerAdapter

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/19
 */
/**
 * 自定义布局，图片+标题+数字指示器
 */
class BannerImageTitleNumAdapter(mDatas: List<BannerBean>) :
    BannerAdapter<BannerBean, BannerImageTitleNumAdapter.BannerViewHolder>(mDatas) {
    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        //注意布局文件，item布局文件要设置为match_parent，这个是viewpager2强制要求的
        //或者调用BannerUtils.getView(parent,R.layout.banner_image_title_num);
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.banner_image_title_num, parent, false)
        return BannerViewHolder(view)
    }

    //绑定数据
    override fun onBindView(holder: BannerViewHolder, data: BannerBean, position: Int, size: Int) {
        //holder.imageView.setImageResource(data.url)
        Glide.with(holder.itemView).load(data.imagePath).into(holder.imageView)
        holder.title.text = data.title
        //可以在布局文件中自己实现指示器，亦可以使用banner提供的方法自定义指示器，目前样式较少，后面补充
        if(isEnableNum){
            holder.numIndicator.text = (position + 1).toString() + "/" + size
        }else{
            holder.numIndicator.text =""
        }
    }

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        var imageView: ImageView
        @JvmField
        var title: TextView
        @JvmField
        var numIndicator: TextView

        init {
            imageView = view.findViewById(R.id.image)
            title = view.findViewById(R.id.bannerTitle)
            numIndicator = view.findViewById(R.id.numIndicator)
        }
    }

    var isEnableNum=true

    fun enableNum(flag:Boolean):BannerImageTitleNumAdapter{
        isEnableNum=false
        return this
    }
}