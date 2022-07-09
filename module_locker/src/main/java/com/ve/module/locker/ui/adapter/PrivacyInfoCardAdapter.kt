package com.ve.module.locker.ui.adapter

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.utils.ImageLoader
import com.ve.lib.common.ext.spanText
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.DateTimeUtil
import com.ve.lib.common.vutils.ToastUtil
import com.ve.module.locker.R
import com.ve.module.locker.model.db.entity.PrivacyCardInfo
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.privacy.details.LockerCardDetailsSeeFragment
import com.ve.module.locker.utils.PasswordUtils
import com.ve.module.locker.utils.StickUtils

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
class PrivacyInfoCardAdapter :
    BaseSectionQuickAdapter<PrivacyCardInfo, BaseViewHolder>(
        com.ve.lib.common.R.layout.item_sticky_header, R.layout.locker_item_privacy_card
    ), LoadMoreModule, DraggableModule, UpFetchModule {

    init {
        setOnItemLongClickListener { adapter, view, position ->
            ToastUtil.showCenter("已复制卡号")
            StickUtils.copy(context, data[position].getPrivacyDetails().number)
            true
        }
    }


    var isCheckMode = false
    private var isAllCheck = false
    private var mSelectList = mutableListOf<PrivacyCardInfo>()
    private var keywords=""

    override fun convert(holder: BaseViewHolder, item: PrivacyCardInfo) {
        if(keywords.isNotEmpty()){
            LogUtil.msg("keywords= $keywords")
            holder.getView<TextView>(R.id.tv_privacy_info_name).apply {
                spanText(keywords,text.toString())
            }
            holder.getView<TextView>(R.id.tv_privacy_info_desc).apply {
                spanText(keywords,text.toString())
            }
        }

        val tvFolder=holder.getView<TextView>(R.id.tv_privacy_folder)
        tvFolder.text = item.getPrivacyFolder().folderName

        ImageLoader.loadView(context,item.getPrivacyFolder().folderCover,holder.getView<TextView>(R.id.cardView))

        val account=item.getPrivacyDetails().number
        holder.setText(R.id.tv_privacy_info_account, PasswordUtils.hidePassword(account))
        holder.setText(R.id.tv_privacy_info_name, item.privacyName)
        holder.setText(R.id.tv_privacy_info_desc, item.privacyDesc)
        holder.setText(R.id.tv_privacy_info_create_time, DateTimeUtil.formatDate(item.createTime))
        holder.setText(R.id.tv_privacy_info_update_time, item.updateTime)

        val checkBox = holder.getView<CheckBox>(R.id.check_button)
        checkBox.setOnClickListener {
            checkBox.apply {
//                isChecked=!isChecked
                if (isChecked) {
                    //选择
                    if (!mSelectList.contains(item)) {
                        LogUtil.msg("add 11 +$item}")
                        mSelectList.add(item)
                    }
                } else {
                    //取消选择
                    if (mSelectList.contains(item)) {
                        LogUtil.msg("remove 1 +${item}")
                        mSelectList.remove(item)
                    }
                }
            }
        }

        if (isCheckMode) {
            checkBox.visibility = View.VISIBLE
            checkBox.isChecked = isAllCheck
        } else {
            checkBox.visibility = View.GONE
        }
    }

    fun setKeywords(key:String){
        LogUtil.msg("keywords= $keywords")
        keywords=key
    }

    fun changeAllState() {
        isAllCheck = !isAllCheck
        if (isAllCheck) {
            data.forEach { card ->
                if (!card.isHeader&&!mSelectList.contains(card))
                    mSelectList.add(card)
            }
        } else {
            mSelectList.removeAll(mSelectList)
        }
    }


    override fun convertHeader(helper: BaseViewHolder, item: PrivacyCardInfo) {
        LogUtil.msg("head " + item.headerName)
        helper.setText(com.ve.lib.common.R.id.tv_header, item.headerName)
        helper.setEnabled(com.ve.lib.common.R.id.tv_header, false)
    }

    fun getSelectData(): MutableList<PrivacyCardInfo> {
        return mSelectList
    }


    override fun setOnItemClick(v: View, position: Int) {
        super.setOnItemChildClick(v, position)
        if(data[position].isHeader)
            return
        if (!isCheckMode) {
            val privacyInfo = data[position]
            val bundle = Bundle()
            bundle.putSerializable(LockerCardDetailsSeeFragment.PRIVACY_DATA_KEY, privacyInfo)
            LockerContainerActivity.start(
                context,
                LockerCardDetailsSeeFragment::class.java.name,
                "查看卡片：" + privacyInfo.privacyName,
                bundle
            )
        } else {
            v.findViewById<CheckBox>(R.id.check_button).apply {
                isChecked = !isChecked
                if (isChecked) {
                    //选择
                    if (!mSelectList.contains(data[position])) {
                        LogUtil.msg("add +${data[position]}")
                        mSelectList.add(data[position])
                    }
                } else {
                    //取消选择
                    if (mSelectList.contains(data[position])) {
                        LogUtil.msg("remove +${data[position]}")
                        mSelectList.remove(data[position])
                    }
                }
            }
        }
    }

}