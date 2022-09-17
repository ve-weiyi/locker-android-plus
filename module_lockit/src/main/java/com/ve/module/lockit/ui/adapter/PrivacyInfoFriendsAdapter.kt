package com.ve.module.lockit.ui.adapter

import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.data.TimeUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.respository.database.entity.PrivacyFriend

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
class PrivacyInfoFriendsAdapter :
    BaseSectionQuickAdapter<PrivacyFriend, BaseViewHolder>(
        com.ve.lib.view.R.layout.item_sticky_header, R.layout.lockit_item_privacy_friends
    ), LoadMoreModule, DraggableModule, UpFetchModule {

    var isCheckMode = false
    private var isAllCheck = false
    private var mSelectList = mutableListOf<PrivacyFriend>()
    private var keywords = ""

    init {
        addChildClickViewIds(R.id.item_layout_content, R.id.item_btn_edit, R.id.item_btn_delete)
    }

    override fun convert(holder: BaseViewHolder, item: PrivacyFriend) {
        holder.apply {
            setText(R.id.tv_friends_nickname,item.nickname)
            setText(R.id.tv_friends_name,item.name)
            setText(R.id.tv_friends_phone,item.phone)
            setText(R.id.tv_friends_email,item.email)
            setText(R.id.tv_friends_qq,item.qq)
            setText(R.id.tv_friends_wechat,item.wechat)
            setText(R.id.tv_friends_address,item.address)
            setText(R.id.tv_friends_department,item.department)
            setText(R.id.tv_friends_remark,item.remark)
            val age= TimeUtil.date.substring(0,4).toInt()-item.birthday.substring(0,4).toInt()
            setText(R.id.tv_friends_birthday,age.toString()+"岁")

            if(item.sex==0){
                setBackgroundResource(R.id.iv_friends_sex, com.ve.lib.application.R.drawable.ic_sex_female_24dp )
            }else{
                setBackgroundResource(R.id.iv_friends_sex, com.ve.lib.application.R.drawable.ic_sex_male_24dp)
            }
        }

        val checkBox = holder.getView<CheckBox>(R.id.check_button)
        checkBox.setOnClickListener {
            checkBox.apply {

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

    override fun convertHeader(helper: BaseViewHolder, item: PrivacyFriend) {
        LogUtil.msg("head " + item.headerName)
        helper.setText(com.ve.lib.view.R.id.tv_header, item.headerName)
        helper.setEnabled(com.ve.lib.view.R.id.tv_header, false)
    }

    fun getSelectData(): MutableList<PrivacyFriend> {
        return mSelectList
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

}